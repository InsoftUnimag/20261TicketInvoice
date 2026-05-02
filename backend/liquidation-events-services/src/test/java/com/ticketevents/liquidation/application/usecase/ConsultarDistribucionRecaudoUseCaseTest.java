package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.domain.repositories.DistribucionRecaudoRepository;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.ConsultarDistribucionMapper;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarDistribucionRecaudoUseCaseTest {

    @Mock
    private DistribucionRecaudoRepository distribucionRepository;

    @Mock
    private DistribucionRecaudoMapper distribucionMapper;

    @Mock
    private ConsultarDistribucionMapper consultarMapper;

    @InjectMocks
    private ConsultarDistribucionRecaudoUseCase useCase;

    private DistribucionRecaudo createDistribucionPreliminar(Long eventoId) {
        DistribucionRecaudo distribucion = new DistribucionRecaudo(eventoId, "Concierto Test", new BigDecimal("65000.00"));
        distribucion.setTotalNetoPreliminar(new BigDecimal("56000.00"));
        distribucion.setTotalDistribuible(new BigDecimal("48200.00"));
        distribucion.setComisionPlataforma(new BigDecimal("6500.00"));
        distribucion.setEstado("PRELIMINAR");
        distribucion.setFechaCalculo(LocalDateTime.now());
        return distribucion;
    }

    private DistribucionRecaudo createDistribucionLiquidado(Long eventoId) {
        DistribucionRecaudo distribucion = createDistribucionPreliminar(eventoId);
        distribucion.setEstado("LIQUIDADO");
        distribucion.setTotalPagoPromotor(new BigDecimal("48200.00"));
        distribucion.setFechaLiquidacion(LocalDateTime.now());
        return distribucion;
    }

    @Test
    void execute_conDistribucionPreliminar_consultaYMarcaComoLiquidado() {
        Long eventoId = 1L;
        DistribucionRecaudo distribucion = createDistribucionPreliminar(eventoId);

        DistribucionRecaudoDto dtoMock = new DistribucionRecaudoDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setNombreEvento("Concierto Test");
        dtoMock.setTotalBruto(new BigDecimal("65000.00"));
        dtoMock.setTotalPagoPromotor(new BigDecimal("48200.00"));
        dtoMock.setEstado("LIQUIDADO");

        when(distribucionRepository.findByEventoId(eventoId)).thenReturn(Optional.of(distribucion));
        when(distribucionRepository.guardar(any(DistribucionRecaudo.class))).thenReturn(distribucion);
        when(distribucionMapper.toDto(any(DistribucionRecaudo.class))).thenReturn(dtoMock);

        DistribucionRecaudoDto resultado = useCase.execute(eventoId);

        assertNotNull(resultado);
        assertEquals(eventoId, resultado.getEventoId());

        verify(distribucionRepository).findByEventoId(eventoId);
        verify(distribucionRepository).guardar(any(DistribucionRecaudo.class));
    }

    @Test
    void execute_conDistribucionLiquidado_consultaExitosamente() {
        Long eventoId = 2L;
        DistribucionRecaudo distribucion = createDistribucionLiquidado(eventoId);

        DistribucionRecaudoDto dtoMock = new DistribucionRecaudoDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setEstado("LIQUIDADO");
        dtoMock.setTotalPagoPromotor(new BigDecimal("48200.00"));

        when(distribucionRepository.findByEventoId(eventoId)).thenReturn(Optional.of(distribucion));
        when(distribucionRepository.guardar(any(DistribucionRecaudo.class))).thenReturn(distribucion);
        when(distribucionMapper.toDto(any(DistribucionRecaudo.class))).thenReturn(dtoMock);

        DistribucionRecaudoDto resultado = useCase.execute(eventoId);

        assertNotNull(resultado);
        assertEquals("LIQUIDADO", resultado.getEstado());

        verify(distribucionRepository).findByEventoId(eventoId);
    }

    @Test
    void execute_conEventoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(null));

        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conDistribucionNoEncontrada_lanzaExcepcion() {
        Long eventoId = 999L;

        when(distribucionRepository.findByEventoId(eventoId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.DISTRIBUTION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_conEventoSinRecaudo_lanzaExcepcion() {
        Long eventoId = 5L;
        DistribucionRecaudo distribucion = new DistribucionRecaudo(eventoId, "Evento Sin Ventas", BigDecimal.ZERO);
        distribucion.marcarSinRecaudo();

        when(distribucionRepository.findByEventoId(eventoId)).thenReturn(Optional.of(distribucion));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.DISTRIBUTION_NOT_LIQUIDATED, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("no tiene liquidacion disponible"));
    }

    @Test
    void execute_conErrorServicioExterno_lanzaTechnicalException() {
        Long eventoId = 1L;

        when(distribucionRepository.findByEventoId(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));

        TechnicalException exception = assertThrows(TechnicalException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }
}
