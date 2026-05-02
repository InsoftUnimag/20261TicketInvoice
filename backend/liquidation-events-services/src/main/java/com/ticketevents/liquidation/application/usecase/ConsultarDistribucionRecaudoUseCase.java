package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.domain.repositories.DistribucionRecaudoRepository;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
import com.ticketevents.liquidation.infrastructure.mappers.ConsultarDistribucionMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsultarDistribucionRecaudoUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConsultarDistribucionRecaudoUseCase.class);

    private final DistribucionRecaudoRepository distribucionRepository;
    private final DistribucionRecaudoMapper distribucionMapper;
    private final ConsultarDistribucionMapper consultarMapper;

    public ConsultarDistribucionRecaudoUseCase(DistribucionRecaudoRepository distribucionRepository,
                                                 DistribucionRecaudoMapper distribucionMapper,
                                                 ConsultarDistribucionMapper consultarMapper) {
        this.distribucionRepository = distribucionRepository;
        this.distribucionMapper = distribucionMapper;
        this.consultarMapper = consultarMapper;
    }

    public DistribucionRecaudoDto execute(Long eventoId) {
        log.info("Iniciando consulta de distribucion del recaudo para evento: {}", eventoId);

        if (eventoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del evento es requerido");
        }

        DistribucionRecaudo distribucion = obtenerDistribucion(eventoId);

        validarEstadoLiquidado(distribucion);

        distribucion.marcarLiquidado();

        DistribucionRecaudo distribucionActualizada = distribucionRepository.guardar(distribucion);

        DistribucionRecaudoDto resultado = distribucionMapper.toDto(distribucionActualizada);

        log.info("Distribucion consultada exitosamente para evento: {} - Estado: {} - Total Pago Promotor: {}",
                eventoId, resultado.getEstado(), resultado.getTotalPagoPromotor());

        return resultado;
    }

    private DistribucionRecaudo obtenerDistribucion(Long eventoId) {
        DistribucionRecaudo distribucion;
        try {
            distribucion = distribucionRepository.findByEventoId(eventoId)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error al consultar distribucion del evento {}: {}", eventoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (distribucion == null) {
            log.warn("Distribucion no encontrada para evento {}", eventoId);
            throw new BusinessException(ErrorCode.DISTRIBUTION_NOT_FOUND,
                    "No se encontro la distribucion del recaudo para el evento. Debe calcular la distribucion primero.");
        }

        return distribucion;
    }

    private void validarEstadoLiquidado(DistribucionRecaudo distribucion) {
        String estado = distribucion.getEstado();
        if (estado == null || (!estado.equalsIgnoreCase("LIQUIDADO") && !estado.equalsIgnoreCase("PRELIMINAR"))) {
            log.warn("Evento {} no tiene liquidacion disponible. Estado actual: {}", distribucion.getEventoId(), estado);
            throw new BusinessException(ErrorCode.DISTRIBUTION_NOT_LIQUIDATED,
                    "El evento aun no tiene liquidacion disponible. Estado actual: " + estado);
        }
    }
}
