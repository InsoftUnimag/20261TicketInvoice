package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
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
import java.util.Arrays;
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
        IngresosEvento ingresos = createIngresos(eventoId, 3, 1, 1);
        
        ConsultarIngresosResponse responseMock = new ConsultarIngresosResponse();
        responseMock.setEventoId(eventoId);
        responseMock.setTotalTicketsVendidos(5);
        responseMock.setTotalTicketsValidados(3);
        responseMock.setTotalTicketsCancelados(1);
        responseMock.setTotalCortesias(1);
        responseMock.setTotalRecaudoBruto(new BigDecimal("1500.00"));
        
        when(ingresosRepository.existeEvento(eventoId)).thenReturn(true);
        
        List<Object[]> ticketsList = new java.util.ArrayList<>();
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.CANCELADO, new BigDecimal("500.00")});
        ticketsList.add(new Object[]{EstadoFinanciero.CORTESIA, BigDecimal.ZERO});
        
        when(ingresosRepository.obtenerTicketsAgrupados(eventoId)).thenReturn(ticketsList);
        when(mapper.toResponse(any(IngresosEvento.class))).thenReturn(responseMock);
        
        ConsultarIngresosResponse response = useCase.execute(eventoId);
        
        assertNotNull(response);
        assertEquals(eventoId, response.getEventoId());
        assertEquals(5, response.getTotalTicketsVendidos());
        
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
        
        ConsultarIngresosResponse responseMock = new ConsultarIngresosResponse();
        responseMock.setEventoId(eventoId);
        responseMock.setTotalTicketsVendidos(1);
        responseMock.setHasInconsistencies(true);
        
        List<Object[]> ticketsWithNull = new java.util.ArrayList<>();
        ticketsWithNull.add(new Object[]{null, new BigDecimal("1000.00")});
        
        when(ingresosRepository.existeEvento(eventoId)).thenReturn(true);
        when(ingresosRepository.obtenerTicketsAgrupados(eventoId)).thenReturn(ticketsWithNull);
        when(mapper.toResponse(any(IngresosEvento.class))).thenReturn(responseMock);
        
        ConsultarIngresosResponse response = useCase.execute(eventoId);
        
        assertNotNull(response);
        assertTrue(response.isHasInconsistencies());
    }
}