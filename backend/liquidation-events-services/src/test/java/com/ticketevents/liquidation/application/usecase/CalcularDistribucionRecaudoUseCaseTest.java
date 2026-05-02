package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcularDistribucionRecaudoUseCaseTest {

    @Mock
    private EventSnapshotRepository eventSnapshotRepository;

    @Mock
    private RecintoRepository recintoRepository;

    @Mock
    private ConfiguracionLiquidacionRepository configuracionRepository;

    @Mock
    private DistribucionRecaudoRepository distribucionRepository;

    @Mock
    private ResumenVentasMapper resumenMapper;

    @Mock
    private DistribucionRecaudoMapper distribucionMapper;

    @InjectMocks
    private CalcularDistribucionRecaudoUseCase useCase;

    private ResumenVentasEvento createResumenCerrado(Long eventoId, BigDecimal totalBruto) {
        ResumenVentasEvento resumen = new ResumenVentasEvento();
        resumen.setIdEvento(eventoId);
        resumen.setNombreEvento("Concierto Test");
        resumen.setEstadoEvento("CERRADO");
        resumen.setTotalRecaudoBruto(totalBruto);

        Map<CondicionLiquidacion, Integer> tickets = new HashMap<>();
        tickets.put(CondicionLiquidacion.VALIDADO, 100);
        tickets.put(CondicionLiquidacion.VENDIDO, 30);
        tickets.put(CondicionLiquidacion.CANCELADO, 5);
        tickets.put(CondicionLiquidacion.CORTESIA, 10);
        resumen.setTicketsPorCondicion(tickets);

        Map<CondicionLiquidacion, BigDecimal> recaudo = new HashMap<>();
        recaudo.put(CondicionLiquidacion.VALIDADO, new BigDecimal("50000.00"));
        recaudo.put(CondicionLiquidacion.VENDIDO, new BigDecimal("15000.00"));
        recaudo.put(CondicionLiquidacion.CANCELADO, new BigDecimal("-2500.00"));
        recaudo.put(CondicionLiquidacion.CORTESIA, BigDecimal.ZERO);
        resumen.setRecaudoPorCondicion(recaudo);

        return resumen;
    }

    private Recinto createRecinto() {
        return new Recinto(1L, "Teatro Test", TipoRecinto.TEATRO, new BigDecimal("0.08"));
    }

    private ConfiguracionLiquidacion createConfiguracion(Long eventoId) {
        return new ConfiguracionLiquidacion(1L, eventoId, TipoLiquidacion.REPARTO_INGRESOS, null, new BigDecimal("0.10"));
    }

    private DistribucionRecaudo createDistribucion(Long eventoId) {
        return new DistribucionRecaudo(eventoId, "Concierto Test", new BigDecimal("65000.00"));
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_conEventoCerradoYConfiguracionCompleta_calculaDistribucionCorrectamente() {
        Long eventoId = 1L;
        ResumenVentasEvento resumen = createResumenCerrado(eventoId, new BigDecimal("62500.00"));
        Recinto recinto = createRecinto();
        ConfiguracionLiquidacion configuracion = createConfiguracion(eventoId);
        DistribucionRecaudo distribucion = createDistribucion(eventoId);
        DistribucionRecaudoDto dtoMock = new DistribucionRecaudoDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setNombreEvento("Concierto Test");
        dtoMock.setTotalBruto(new BigDecimal("65000.00"));
        dtoMock.setTotalNetoPreliminar(new BigDecimal("58500.00"));
        dtoMock.setTotalDistribuible(new BigDecimal("53300.00"));
        dtoMock.setEstado("PRELIMINAR");

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(resumen);
        when(recintoRepository.findById(eventoId)).thenReturn(recinto);
        when(configuracionRepository.findByEventoId(eventoId)).thenReturn(Optional.of(configuracion));
        when(distribucionRepository.guardar(any(DistribucionRecaudo.class))).thenReturn(distribucion);
        when(distribucionMapper.toDto(any(DistribucionRecaudo.class))).thenReturn(dtoMock);

        DistribucionRecaudoDto resultado = useCase.execute(eventoId);

        assertNotNull(resultado);
        assertEquals(eventoId, resultado.getEventoId());
        assertEquals("PRELIMINAR", resultado.getEstado());

        verify(eventSnapshotRepository).getSnapshot(eventoId);
        verify(recintoRepository).findById(eventoId);
        verify(configuracionRepository).findByEventoId(eventoId);
        verify(distribucionRepository).guardar(any(DistribucionRecaudo.class));
    }

    @Test
    void execute_conEventoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(null));

        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conEventoNoEncontrado_lanzaExcepcion() {
        Long eventoId = 999L;

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EVENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_conEventoNoCerrado_lanzaExcepcion() {
        Long eventoId = 4L;
        ResumenVentasEvento resumen = new ResumenVentasEvento();
        resumen.setIdEvento(eventoId);
        resumen.setNombreEvento("Evento En Curso");
        resumen.setEstadoEvento("EN_CURSO");
        resumen.setTotalRecaudoBruto(new BigDecimal("1000.00"));

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(resumen);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EVENT_NOT_CLOSED, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("no ha sido cerrado"));
    }

    @Test
    void execute_conEventoSinVentas_registraSinRecaudo() {
        Long eventoId = 5L;
        ResumenVentasEvento resumen = new ResumenVentasEvento();
        resumen.setIdEvento(eventoId);
        resumen.setNombreEvento("Evento Sin Ventas");
        resumen.setEstadoEvento("CERRADO");
        resumen.setTotalRecaudoBruto(BigDecimal.ZERO);

        DistribucionRecaudo distribucion = new DistribucionRecaudo(eventoId, "Evento Sin Ventas", BigDecimal.ZERO);
        distribucion.marcarSinRecaudo();

        DistribucionRecaudoDto dtoMock = new DistribucionRecaudoDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setEstado("SIN_RECAUDO");
        dtoMock.setTotalBruto(BigDecimal.ZERO);

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(resumen);
        when(distribucionRepository.guardar(any(DistribucionRecaudo.class))).thenReturn(distribucion);
        when(distribucionMapper.toDto(any(DistribucionRecaudo.class))).thenReturn(dtoMock);

        DistribucionRecaudoDto resultado = useCase.execute(eventoId);

        assertNotNull(resultado);
        assertEquals("SIN_RECAUDO", resultado.getEstado());
        assertEquals(BigDecimal.ZERO, resultado.getTotalBruto());

        verify(distribucionRepository).guardar(any(DistribucionRecaudo.class));
    }

    @Test
    void execute_sinConfiguracionLiquidacion_lanzaExcepcion() {
        Long eventoId = 1L;
        ResumenVentasEvento resumen = createResumenCerrado(eventoId, new BigDecimal("62500.00"));
        Recinto recinto = createRecinto();

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(resumen);
        when(recintoRepository.findById(eventoId)).thenReturn(recinto);
        when(configuracionRepository.findByEventoId(eventoId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("configuracion de liquidacion"));
    }

    @Test
    void execute_conErrorServicioExterno_lanzaTechnicalException() {
        Long eventoId = 1L;

        when(eventSnapshotRepository.getSnapshot(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));

        TechnicalException exception = assertThrows(TechnicalException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }

    @Test
    void execute_conRecintoNoEncontrado_lanzaExcepcion() {
        Long eventoId = 1L;
        ResumenVentasEvento resumen = createResumenCerrado(eventoId, new BigDecimal("62500.00"));

        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(resumen);
        when(recintoRepository.findById(eventoId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.RECINTO_NOT_FOUND, exception.getErrorCode());
    }
}
