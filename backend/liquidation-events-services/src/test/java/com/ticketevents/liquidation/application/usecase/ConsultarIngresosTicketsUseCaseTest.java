package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.IngresosTicketsDto;
import com.ticketevents.liquidation.domain.entities.EstadoFinanciero;
import com.ticketevents.liquidation.domain.entities.IngresosEvento;
import com.ticketevents.liquidation.domain.repositories.IngresosConsultaRepository;
import com.ticketevents.liquidation.infrastructure.mappers.IngresosMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarIngresosTicketsUseCaseTest {

    @Mock
    private IngresosConsultaRepository ingresosRepository;

    @Mock
    private IngresosMapper mapper;

    @InjectMocks
    private ConsultarIngresosTicketsUseCase useCase;

    private IngresosEvento createIngresos(Long eventoId, int validados, int cancelados, int cortesia) {
        IngresosEvento ingresos = new IngresosEvento(eventoId);
        for (int i = 0; i < validados; i++) {
            ingresos.agregarTicket(EstadoFinanciero.VALIDADO, new BigDecimal("500.00"));
        }
        for (int i = 0; i < cancelados; i++) {
            ingresos.agregarTicket(EstadoFinanciero.CANCELADO, new BigDecimal("500.00"));
        }
        for (int i = 0; i < cortesia; i++) {
            ingresos.agregarTicket(EstadoFinanciero.CORTESIA, BigDecimal.ZERO);
        }
        return ingresos;
    }

    @Test
    void execute_conEventoExistente_retornaIngresosCorrectamente() {
        Long eventoId = 1L;

        IngresosTicketsDto dtoMock = new IngresosTicketsDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setTotalTicketsVendidos(5);
        dtoMock.setTotalTicketsValidados(3);
        dtoMock.setTotalTicketsCancelados(1);
        dtoMock.setTotalCortesias(1);
        dtoMock.setTotalRecaudoBruto(new BigDecimal("1500.00"));

        when(ingresosRepository.existeEvento(eventoId)).thenReturn(true);

        List<Object[]> ticketsList = new ArrayList<>();
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.CANCELADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.CORTESIA, BigDecimal.ZERO});

        when(ingresosRepository.obtenerTicketsAgrupados(eventoId)).thenReturn(ticketsList);
        when(mapper.toDto(any(IngresosEvento.class))).thenReturn(dtoMock);

        IngresosTicketsDto result = useCase.execute(eventoId);

        assertNotNull(result);
        assertEquals(eventoId, result.getEventoId());
        assertEquals(5, result.getTotalTicketsVendidos());

        verify(ingresosRepository).existeEvento(eventoId);
        verify(ingresosRepository).obtenerTicketsAgrupados(eventoId);
    }

    @Test
    void execute_conEventoNoEncontrado_lanzaExcepcion() {
        Long eventoId = 999L;

        when(ingresosRepository.existeEvento(eventoId)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EVENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_conEventoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class,
            () -> useCase.execute(null));

        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conErrorDeServicioExterno_lanzaTechnicalException() {
        Long eventoId = 1L;

        when(ingresosRepository.existeEvento(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));

        TechnicalException exception = assertThrows(TechnicalException.class,
            () -> useCase.execute(eventoId));

        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }

    @Test
    void execute_conTicketsSinEstado_marcaInconsistencias() {
        Long eventoId = 3L;

        IngresosTicketsDto dtoMock = new IngresosTicketsDto();
        dtoMock.setEventoId(eventoId);
        dtoMock.setTotalTicketsVendidos(1);
        dtoMock.setHasInconsistencias(true);

        List<Object[]> ticketsWithNull = new ArrayList<>();
        ticketsWithNull.add(new Object[]{null, new BigDecimal("1000.00")});

        when(ingresosRepository.existeEvento(eventoId)).thenReturn(true);
        when(ingresosRepository.obtenerTicketsAgrupados(eventoId)).thenReturn(ticketsWithNull);
        when(mapper.toDto(any(IngresosEvento.class))).thenReturn(dtoMock);

        IngresosTicketsDto result = useCase.execute(eventoId);

        assertNotNull(result);
        assertTrue(result.isHasInconsistencias());
    }
}