ackage com.liquidacion.resumenVentas.application.usecase;

import com.liquidacion.resumenVentas.application.dto.ConsultarResumenVentasResponse;
import com.liquidacion.resumenVentas.domain.entities.CondicionLiquidacion;
import com.liquidacion.resumenVentas.domain.entities.ResumenVentasEvento;
import com.liquidacion.resumenVentas.domain.repositories.EventSnapshotRepository;
import com.liquidacion.resumenVentas.shared.errors.BusinessException;
import com.liquidacion.resumenVentas.shared.errors.ErrorCode;
import com.liquidacion.resumenVentas.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarResumenVentasUseCaseTest {

    @Mock
    private EventSnapshotRepository eventSnapshotRepository;

    @InjectMocks
    private ConsultarResumenVentasUseCase useCase;

    private ResumenVentasEvento createSnapshot(Long id, String estado) {
        ResumenVentasEvento snapshot = new ResumenVentasEvento();
        snapshot.setIdEvento(id);
        snapshot.setNombreEvento("Concierto Rock 2026");
        snapshot.setEstadoEvento(estado);
        snapshot.setTotalTicketsVendidos(100);
        snapshot.setTotalTicketsValidados(80);
        snapshot.setTotalTicketsCancelados(5);
        snapshot.setTotalTicketsCortesia(15);
        snapshot.setTotalRecaudoBruto(new BigDecimal("50000.00"));
        
        Map<CondicionLiquidacion, Integer> ticketsPorCondicion = new HashMap<>();
        ticketsPorCondicion.put(CondicionLiquidacion.VALIDADO, 80);
        ticketsPorCondicion.put(CondicionLiquidacion.VENDIDO, 5);
        ticketsPorCondicion.put(CondicionLiquidacion.CANCELADO, 5);
        ticketsPorCondicion.put(CondicionLiquidacion.CORTESIA, 15);
        snapshot.setTicketsPorCondicion(ticketsPorCondicion);
        
        Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion = new HashMap<>();
        recaudoPorCondicion.put(CondicionLiquidacion.VALIDADO, new BigDecimal("40000.00"));
        recaudoPorCondicion.put(CondicionLiquidacion.VENDIDO, new BigDecimal("2500.00"));
        recaudoPorCondicion.put(CondicionLiquidacion.CANCELADO, new BigDecimal("-2500.00"));
        recaudoPorCondicion.put(CondicionLiquidacion.CORTESIA, BigDecimal.ZERO);
        snapshot.setRecaudoPorCondicion(recaudoPorCondicion);
        
        return snapshot;
    }

    @Test
    void execute_conEventoCerrado_retornaResumenCorrectamente() {
        Long eventoId = 1L;
        ResumenVentasEvento snapshot = createSnapshot(eventoId, "CERRADO");
        
        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(snapshot);
        
        ConsultarResumenVentasResponse response = useCase.execute(eventoId);
        
        assertNotNull(response);
        assertEquals(eventoId, response.getEventoId());
        assertEquals("CERRADO", response.getEstadoEvento());
        assertEquals(100, response.getTotalTicketsVendidos());
        assertEquals(80, response.getTotalTicketsValidados());
        assertEquals(5, response.getTotalTicketsCancelados());
        assertEquals(15, response.getTotalTicketsCortesia());
        assertEquals(new BigDecimal("50000.00"), response.getTotalRecaudoBruto());
        
        verify(eventSnapshotRepository).getSnapshot(eventoId);
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
        Long eventoId = 1L;
        ResumenVentasEvento snapshot = createSnapshot(eventoId, "EN_CURSO");
        
        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(snapshot);
        
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(eventoId));
        
        assertEquals(ErrorCode.EVENT_NOT_CLOSED, exception.getErrorCode());
    }

    @Test
    void execute_conEventoProgramado_lanzaExcepcion() {
        Long eventoId = 1L;
        ResumenVentasEvento snapshot = createSnapshot(eventoId, "PROGRAMADO");
        
        when(eventSnapshotRepository.getSnapshot(eventoId)).thenReturn(snapshot);
        
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(eventoId));
        
        assertEquals(ErrorCode.EVENT_NOT_CLOSED, exception.getErrorCode());
    }

    @Test
    void execute_conErrorDeServicioExterno_lanzaTechnicalException() {
        Long eventoId = 1L;
        
        when(eventSnapshotRepository.getSnapshot(eventoId))
            .thenThrow(new RuntimeException("Connection refused"));
        
        TechnicalException exception = assertThrows(TechnicalException.class, 
            () -> useCase.execute(eventoId));
        
        assertEquals(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, exception.getErrorCode());
    }

    @Test
    void execute_conEventoIdNulo_lanzaExcepcion() {
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> useCase.execute(null));
        
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }
}
