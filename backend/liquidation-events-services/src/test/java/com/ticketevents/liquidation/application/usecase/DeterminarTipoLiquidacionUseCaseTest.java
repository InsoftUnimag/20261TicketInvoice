package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ConfiguracionLiquidacionDto;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.infrastructure.mappers.ConfiguracionLiquidacionMapper;
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
class DeterminarTipoLiquidacionUseCaseTest {

    @Mock
    private ConfiguracionLiquidacionRepository repository;

    @Mock
    private ConfiguracionLiquidacionMapper mapper;

    @InjectMocks
    private DeterminarTipoLiquidacionUseCase useCase;

    private ConfiguracionLiquidacion createConfiguracion(Long eventoId, TipoLiquidacion tipo) {
        return new ConfiguracionLiquidacion(1L, eventoId, tipo, new BigDecimal("1000.00"), new BigDecimal("0.10"));
    }

    @Test
    void execute_conEventoExistenteYTarifaPlana_ConfiguraCorrectamente() {
        Long eventoId = 1L;
        TipoLiquidacion tipo = TipoLiquidacion.TARIFA_PLANA;
        BigDecimal valor = new BigDecimal("5000.00");
        BigDecimal porcentaje = null;

        ConfiguracionLiquidacion configuracion = createConfiguracion(eventoId, tipo);

        ConfiguracionLiquidacionDto dtoMock = new ConfiguracionLiquidacionDto(
            1L, eventoId, "TARIFA_PLANA", valor, BigDecimal.ZERO);

        when(repository.existeEvento(eventoId)).thenReturn(true);
        when(repository.guardar(any(ConfiguracionLiquidacion.class))).thenReturn(Optional.of(configuracion));
        when(mapper.toDto(configuracion)).thenReturn(dtoMock);

        ConfiguracionLiquidacionDto response = useCase.execute(eventoId, tipo, valor, porcentaje);

        assertNotNull(response);
        assertEquals(eventoId, response.getEventoId());
        assertEquals("TARIFA_PLANA", response.getTipoLiquidacion());

        verify(repository).existeEvento(eventoId);
        verify(repository).guardar(any(ConfiguracionLiquidacion.class));
    }

    @Test
    void execute_conEventoExistenteYRepartoIngresos_ConfiguraCorrectamente() {
        Long eventoId = 2L;
        TipoLiquidacion tipo = TipoLiquidacion.REPARTO_INGRESOS;
        BigDecimal valor = null;
        BigDecimal porcentaje = new BigDecimal("0.15");

        ConfiguracionLiquidacion configuracion = createConfiguracion(eventoId, tipo);

        ConfiguracionLiquidacionDto dtoMock = new ConfiguracionLiquidacionDto(
            1L, eventoId, "REPARTO_INGRESOS", null, porcentaje);

        when(repository.existeEvento(eventoId)).thenReturn(true);
        when(repository.guardar(any(ConfiguracionLiquidacion.class))).thenReturn(Optional.of(configuracion));
        when(mapper.toDto(configuracion)).thenReturn(dtoMock);

        ConfiguracionLiquidacionDto response = useCase.execute(eventoId, tipo, valor, porcentaje);

        assertNotNull(response);
        assertEquals("REPARTO_INGRESOS", response.getTipoLiquidacion());
    }

    @Test
    void execute_conEventoNoExistente_lanzaExcepcion() {
        Long eventoId = 999L;
        TipoLiquidacion tipo = TipoLiquidacion.TARIFA_PLANA;
        
        when(repository.existeEvento(eventoId)).thenReturn(false);
        
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(eventoId, tipo, new BigDecimal("1000.00"), null));
        
        assertEquals(ErrorCode.EVENT_NOT_FOUND, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("no existente"));
    }

    @Test
    void execute_conEventoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(null, TipoLiquidacion.TARIFA_PLANA, new BigDecimal("1000.00"), null));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conTipoLiquidacionNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(1L, null, new BigDecimal("1000.00"), null));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void execute_conErrorDeServicioExterno_lanzaTechnicalException() {
        Long eventoId = 1L;
        
        when(repository.existeEvento(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(eventoId, TipoLiquidacion.TARIFA_PLANA, new BigDecimal("1000.00"), null));
        
        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }
}