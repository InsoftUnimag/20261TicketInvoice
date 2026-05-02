package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.CondicionLiquidacion;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.domain.repositories.DistribucionRecaudoRepository;
import com.ticketevents.liquidation.domain.repositories.EventSnapshotRepository;
import com.ticketevents.liquidation.domain.repositories.RecintoRepository;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
import com.ticketevents.liquidation.infrastructure.mappers.ResumenVentasMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CalcularDistribucionRecaudoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CalcularDistribucionRecaudoUseCase.class);

    private final EventSnapshotRepository eventSnapshotRepository;
    private final RecintoRepository recintoRepository;
    private final ConfiguracionLiquidacionRepository configuracionRepository;
    private final DistribucionRecaudoRepository distribucionRepository;
    private final ResumenVentasMapper resumenMapper;
    private final DistribucionRecaudoMapper distribucionMapper;

    public CalcularDistribucionRecaudoUseCase(EventSnapshotRepository eventSnapshotRepository,
                                               RecintoRepository recintoRepository,
                                               ConfiguracionLiquidacionRepository configuracionRepository,
                                               DistribucionRecaudoRepository distribucionRepository,
                                               ResumenVentasMapper resumenMapper,
                                               DistribucionRecaudoMapper distribucionMapper) {
        this.eventSnapshotRepository = eventSnapshotRepository;
        this.recintoRepository = recintoRepository;
        this.configuracionRepository = configuracionRepository;
        this.distribucionRepository = distribucionRepository;
        this.resumenMapper = resumenMapper;
        this.distribucionMapper = distribucionMapper;
    }

    public DistribucionRecaudoDto execute(Long eventoId) {
        log.info("Iniciando calculo de distribucion del recaudo para evento: {}", eventoId);

        if (eventoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del evento es requerido");
        }

        ResumenVentasEvento resumen = obtenerResumenVentas(eventoId);

        validarEventoCerrado(resumen);

        if (esSinRecaudo(resumen)) {
            return calcularSinRecaudo(resumen);
        }

        Recinto recinto = obtenerRecinto(resumen);

        ConfiguracionLiquidacion configuracion = obtenerConfiguracionLiquidacion(eventoId);

        BigDecimal totalBruto = calcularTotalBruto(resumen);

        BigDecimal descuentoCancelados = obtenerDescuentoCancelados(resumen);
        BigDecimal descuentoCortesia = obtenerDescuentoCortesia(resumen);
        BigDecimal totalDescuentos = descuentoCancelados.add(descuentoCortesia);

        BigDecimal comisionPlataforma = calcularComisionPlataforma(totalBruto, recinto, configuracion);

        DistribucionRecaudo distribucion = new DistribucionRecaudo(
                eventoId,
                resumen.getNombreEvento(),
                totalBruto
        );

        distribucion.setDescuentoCancelados(descuentoCancelados);
        distribucion.setDescuentoCortesia(descuentoCortesia);

        distribucion.calcularNetoPreliminar(comisionPlataforma, totalDescuentos);

        BigDecimal comisionesAcordadas = calcularComisionesAcordadas(totalBruto, recinto);
        distribucion.calcularTotalDistribuible(comisionesAcordadas);

        distribucion.validar();

        DistribucionRecaudo distribucionGuardada = distribucionRepository.guardar(distribucion);

        DistribucionRecaudoDto resultado = distribucionMapper.toDto(distribucionGuardada);

        log.info("Distribucion del recaudo calculada exitosamente para evento: {} - Total Bruto: {} - Neto Preliminar: {} - Total Distribuible: {}",
                eventoId, resultado.getTotalBruto(), resultado.getTotalNetoPreliminar(), resultado.getTotalDistribuible());

        return resultado;
    }

    private ResumenVentasEvento obtenerResumenVentas(Long eventoId) {
        ResumenVentasEvento resumen;
        try {
            resumen = eventSnapshotRepository.getSnapshot(eventoId);
        } catch (Exception e) {
            log.error("Error al obtener snapshot del evento {}: {}", eventoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (resumen == null) {
            log.warn("Evento {} no encontrado", eventoId);
            throw new BusinessException(ErrorCode.EVENT_NOT_FOUND);
        }

        return resumen;
    }

    private void validarEventoCerrado(ResumenVentasEvento resumen) {
        String estado = resumen.getEstadoEvento();
        if (estado == null || !estado.equalsIgnoreCase("CERRADO")) {
            log.warn("Evento {} no esta cerrado. Estado actual: {}", resumen.getIdEvento(), estado);
            throw new BusinessException(ErrorCode.EVENT_NOT_CLOSED,
                    "El evento aun no ha sido cerrado. Estado actual: " + estado);
        }
    }

    private boolean esSinRecaudo(ResumenVentasEvento resumen) {
        BigDecimal totalBruto = resumen.getTotalRecaudoBruto();
        return totalBruto == null || totalBruto.compareTo(BigDecimal.ZERO) == 0;
    }

    private DistribucionRecaudoDto calcularSinRecaudo(ResumenVentasEvento resumen) {
        log.info("Evento {} sin recaudo. Registrando distribucion en cero.", resumen.getIdEvento());

        DistribucionRecaudo distribucion = new DistribucionRecaudo(
                resumen.getIdEvento(),
                resumen.getNombreEvento(),
                BigDecimal.ZERO
        );
        distribucion.marcarSinRecaudo();
        distribucion.validar();

        DistribucionRecaudo guardada = distribucionRepository.guardar(distribucion);
        return distribucionMapper.toDto(guardada);
    }

    private Recinto obtenerRecinto(ResumenVentasEvento resumen) {
        Long recintoId = resumen.getIdEvento();
        Recinto recinto = recintoRepository.findById(recintoId);

        if (recinto == null) {
            log.warn("Recinto no encontrado para evento {}", resumen.getIdEvento());
            throw new BusinessException(ErrorCode.RECINTO_NOT_FOUND,
                    "No se encontro el recinto asociado al evento");
        }

        return recinto;
    }

    private ConfiguracionLiquidacion obtenerConfiguracionLiquidacion(Long eventoId) {
        ConfiguracionLiquidacion configuracion = configuracionRepository.findByEventoId(eventoId)
                .orElse(null);

        if (configuracion == null) {
            log.warn("Configuracion de liquidacion no definida para evento {}", eventoId);
            throw new BusinessException(ErrorCode.INVALID_REQUEST,
                    "No se ha definido la configuracion de liquidacion para el evento. Debe establecer el tipo de liquidacion antes de calcular la distribucion.");
        }

        return configuracion;
    }

    private BigDecimal calcularTotalBruto(ResumenVentasEvento resumen) {
        Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion = resumen.getRecaudoPorCondicion();

        if (recaudoPorCondicion == null || recaudoPorCondicion.isEmpty()) {
            return resumen.getTotalRecaudoBruto() != null ? resumen.getTotalRecaudoBruto() : BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<CondicionLiquidacion, BigDecimal> entry : recaudoPorCondicion.entrySet()) {
            CondicionLiquidacion condicion = entry.getKey();
            BigDecimal valor = entry.getValue();

            if (valor != null && condicion != CondicionLiquidacion.CANCELADO && condicion != CondicionLiquidacion.CORTESIA) {
                total = total.add(valor);
            }
        }

        return total;
    }

    private BigDecimal obtenerDescuentoCancelados(ResumenVentasEvento resumen) {
        Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion = resumen.getRecaudoPorCondicion();
        if (recaudoPorCondicion == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal cancelados = recaudoPorCondicion.get(CondicionLiquidacion.CANCELADO);
        if (cancelados != null && cancelados.compareTo(BigDecimal.ZERO) < 0) {
            return cancelados.abs();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal obtenerDescuentoCortesia(ResumenVentasEvento resumen) {
        Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion = resumen.getRecaudoPorCondicion();
        if (recaudoPorCondicion == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal cortesia = recaudoPorCondicion.get(CondicionLiquidacion.CORTESIA);
        return cortesia != null ? cortesia.abs() : BigDecimal.ZERO;
    }

    private BigDecimal calcularComisionPlataforma(BigDecimal totalBruto, Recinto recinto, ConfiguracionLiquidacion configuracion) {
        BigDecimal comision;

        switch (configuracion.getTipoLiquidacion()) {
            case TARIFA_PLANA:
                comision = configuracion.getValorComision() != null ? configuracion.getValorComision() : BigDecimal.ZERO;
                break;
            case REPARTO_INGRESOS:
                BigDecimal porcentaje = configuracion.getPorcentaje() != null ? configuracion.getPorcentaje() : BigDecimal.ZERO;
                comision = totalBruto.multiply(porcentaje);
                break;
            default:
                log.warn("Tipo de liquidacion no reconocido, usando tasa del recinto");
                comision = totalBruto.multiply(recinto.getTasaComision());
        }

        log.debug("Comision de plataforma calculada: {}", comision);
        return comision;
    }

    private BigDecimal calcularComisionesAcordadas(BigDecimal totalBruto, Recinto recinto) {
        BigDecimal comisionRecinto = totalBruto.multiply(recinto.getTasaComision());
        log.debug("Comision acordada para recinto {}: {}", recinto.getNombre(), comisionRecinto);
        return comisionRecinto;
    }
}
