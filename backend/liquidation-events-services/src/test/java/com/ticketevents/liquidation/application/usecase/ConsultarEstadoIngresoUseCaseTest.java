package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarEstadoIngresoResponse;
import com.ticketevents.liquidation.domain.entities.EstadoIngreso;
import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import com.ticketevents.liquidation.domain.repositories.AccessControlRepository;
import com.ticketevents.liquidation.infrastructure.mappers.EstadoIngresoMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarEstadoIngresoUseCaseTest {

    @Mock
    private AccessControlRepository accessControlRepository;

    @Mock
    private EstadoIngresoMapper mapper;

    @InjectMocks
    private ConsultarEstadoIngresoUseCase useCase;

    private List<RegistroIngreso> crearRegistros(int checkeados, int noAsistieron) {
        List<RegistroIngreso> registros = new ArrayList<>();
        
        for (int i = 1; i <= checkeados; i++) {
            registros.add(new RegistroIngreso(
                (long) i,
                1L,
                LocalDateTime.now(),
                EstadoIngreso.CHECKED_IN,
                "INGRESO"
            ));
        }
        
        for (int i = checkeados + 1; i <= checkeados + noAsistieron; i++) {
            registros.add(new RegistroIngreso(
                (long) i,
                1L,
                null,
                EstadoIngreso.NOT_ATTENDED,
                null
            ));
        }
        
        return registros;
    }

    @Test
    void execute_conEventoCerrado_retornaResumenCorrectamente() {
        Long eventoId = 1L;
        List<RegistroIngreso> registros = crearRegistros(100, 30);
        
        ConsultarEstadoIngresoResponse responseMock = new ConsultarEstadoIngresoResponse();
        responseMock.setEventoId(eventoId);
        responseMock.setNombreEvento("Concierto Rock 2026");
        responseMock.setTotalTickets(130);
        responseMock.setTotalCheckeados(100);
        responseMock.setTotalNoAsistieron(30);
        
        when(accessControlRepository.getIngresosByEvento(eventoId)).thenReturn(registros);
        when(mapper.toResponse(eq(eventoId), anyString(), eq(registros))).thenReturn(responseMock);
        
        ConsultarEstadoIngresoResponse response = useCase.execute(eventoId);
        
        assertNotNull(response);
        assertEquals(eventoId, response.getEventoId());
        
        verify(accessControlRepository).getIngresosByEvento(eventoId);
    }

    @Test
    void execute_conEventoNoEncontrado_lanzaExcepcion() {
        Long eventoId = 999L;
        
        when(accessControlRepository.getIngresosByEvento(eventoId)).thenReturn(new ArrayList<>());
        
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
        
        when(accessControlRepository.getIngresosByEvento(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(eventoId));
        
        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }

    @Test
    void execute_conRegistroNulo_lanzaExcepcion() {
        Long eventoId = 1L;
        List<RegistroIngreso> registros = crearRegistros(5, 5);
        registros.add(null);
        
        when(accessControlRepository.getIngresosByEvento(eventoId)).thenReturn(registros);
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(eventoId));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }
}