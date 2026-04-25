package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionRecintoRepository;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
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
class RegistrarComisionRecintoUseCaseTest {

    @Mock
    private ComisionRecintoRepository repository;

    @Mock
    private ComisionRecintoMapper mapper;

    @InjectMocks
    private RegistrarComisionRecintoUseCase useCase;

    private ComisionRecinto createComision(Long recintoId, TipoComision tipo, BigDecimal valor) {
        return new ComisionRecinto(1L, recintoId, tipo, valor);
    }

    @Test
    void execute_conRecintoExistenteYPorcentaje_RegistraCorrectamente() {
        Long recintoId = 1L;
        TipoComision tipo = TipoComision.PORCENTAJE;
        BigDecimal valor = new BigDecimal("0.12");
        
        ComisionRecinto comision = createComision(recintoId, tipo, valor);
        
        RegistrarComisionRecintoResponse responseMock = new RegistrarComisionRecintoResponse();
        responseMock.setRecintoId(recintoId);
        responseMock.setTipoComision("PORCENTAJE");
        responseMock.setValorComision(valor);
        responseMock.setMensaje("Comision registrada exitosamente");
        
        when(repository.existeRecinto(recintoId)).thenReturn(true);
        when(repository.guardar(any(ComisionRecinto.class))).thenReturn(Optional.of(comision));
        when(mapper.toResponse(comision)).thenReturn(responseMock);
        
        RegistrarComisionRecintoResponse response = useCase.execute(recintoId, tipo, valor);
        
        assertNotNull(response);
        assertEquals(recintoId, response.getRecintoId());
        assertEquals("PORCENTAJE", response.getTipoComision());
        
        verify(repository).existeRecinto(recintoId);
        verify(repository).guardar(any(ComisionRecinto.class));
    }

    @Test
    void execute_conRecintoExistenteYValorFijo_RegistraCorrectamente() {
        Long recintoId = 2L;
        TipoComision tipo = TipoComision.VALOR_FIJO;
        BigDecimal valor = new BigDecimal("5000.00");
        
        ComisionRecinto comision = createComision(recintoId, tipo, valor);
        
        RegistrarComisionRecintoResponse responseMock = new RegistrarComisionRecintoResponse();
        responseMock.setRecintoId(recintoId);
        responseMock.setTipoComision("VALOR_FIJO");
        responseMock.setValorComision(valor);
        
        when(repository.existeRecinto(recintoId)).thenReturn(true);
        when(repository.guardar(any(ComisionRecinto.class))).thenReturn(Optional.of(comision));
        when(mapper.toResponse(comision)).thenReturn(responseMock);
        
        RegistrarComisionRecintoResponse response = useCase.execute(recintoId, tipo, valor);
        
        assertNotNull(response);
        assertEquals("VALOR_FIJO", response.getTipoComision());
    }

    @Test
    void execute_conRecintoNoExistente_lanzaExcepcion() {
        Long recintoId = 999L;
        TipoComision tipo = TipoComision.PORCENTAJE;
        BigDecimal valor = new BigDecimal("0.10");
        
        when(repository.existeRecinto(recintoId)).thenReturn(false);
        
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(recintoId, tipo, valor));
        
        assertEquals(ErrorCode.RECINTO_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_conRecintoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(null, TipoComision.PORCENTAJE, new BigDecimal("0.10")));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conTipoComisionNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(1L, null, new BigDecimal("0.10")));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conValorNegativo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(1L, TipoComision.PORCENTAJE, new BigDecimal("-0.10")));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conErrorDeServicioExterno_lanzaTechnicalException() {
        Long recintoId = 1L;
        
        when(repository.existeRecinto(recintoId))
            .thenThrow(new RuntimeException("Connection refused"));
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(recintoId, TipoComision.PORCENTAJE, new BigDecimal("0.10")));
        
        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }
}