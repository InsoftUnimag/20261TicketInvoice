package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.domain.entities.TipoRecinto;
import com.ticketevents.liquidation.domain.repositories.RecintoRepository;
import com.ticketevents.liquidation.infrastructure.mappers.RecintoMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarRecintoUseCaseTest {

    @Mock
    private RecintoRepository recintoRepository;

    @Mock
    private RecintoMapper mapper;

    @InjectMocks
    private ConsultarRecintoUseCase useCase;

    private Recinto createRecinto(Long id, String nombre, TipoRecinto tipo) {
        Recinto recinto = new Recinto();
        recinto.setId(id);
        recinto.setNombre(nombre);
        recinto.setTipoRecinto(tipo);
        recinto.setTasaComision(tipo == TipoRecinto.ESTADIO 
            ? new BigDecimal("0.15") 
            : new BigDecimal("0.10"));
        recinto.setEstado("ACTIVO");
        return recinto;
    }

    @Test
    void execute_conRecintoExistente_retornaRecintoCorrectamente() {
        Long recintoId = 1L;
        Recinto recinto = createRecinto(recintoId, "Estadio Nacional", TipoRecinto.ESTADIO);
        ConsultarRecintoResponse responseMock = new ConsultarRecintoResponse();
        responseMock.setId(recintoId);
        responseMock.setNombre("Estadio Nacional");
        responseMock.setTipoRecinto("ESTADIO");
        
        when(recintoRepository.findById(recintoId)).thenReturn(recinto);
        when(mapper.toResponse(recinto)).thenReturn(responseMock);
        
        ConsultarRecintoResponse response = useCase.execute(recintoId);
        
        assertNotNull(response);
        assertEquals(recintoId, response.getId());
        assertEquals("Estadio Nacional", response.getNombre());
        assertEquals("ESTADIO", response.getTipoRecinto());
        
        verify(recintoRepository).findById(recintoId);
    }

    @Test
    void execute_conRecintoTeatro_retornaTasaCorrecta() {
        Long recintoId = 2L;
        Recinto recinto = createRecinto(recintoId, "Teatro Colón", TipoRecinto.TEATRO);
        ConsultarRecintoResponse responseMock = new ConsultarRecintoResponse();
        responseMock.setId(recintoId);
        responseMock.setTipoRecinto("TEATRO");
        responseMock.setTasaComision(new BigDecimal("0.10"));
        
        when(recintoRepository.findById(recintoId)).thenReturn(recinto);
        when(mapper.toResponse(recinto)).thenReturn(responseMock);
        
        ConsultarRecintoResponse response = useCase.execute(recintoId);
        
        assertEquals("TEATRO", response.getTipoRecinto());
        assertEquals(new BigDecimal("0.10"), response.getTasaComision());
    }

    @Test
    void execute_conRecintoNoExistente_lanzaExcepcion() {
        Long recintoId = 999L;
        
        when(recintoRepository.findById(recintoId)).thenReturn(null);
        
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(recintoId));
        
        assertEquals(ErrorCode.RECINTO_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_conRecintoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(null));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conErrorDeServicioExterno_lanzaTechnicalException() {
        Long recintoId = 1L;
        
        when(recintoRepository.findById(recintoId))
            .thenThrow(new RuntimeException("Connection refused"));
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(recintoId));
        
        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }
}