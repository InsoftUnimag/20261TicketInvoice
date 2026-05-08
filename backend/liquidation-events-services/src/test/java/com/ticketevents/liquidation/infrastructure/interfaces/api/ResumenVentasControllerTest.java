package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarResumenVentasUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.EventSnapshotDto;
import com.ticketevents.liquidation.infrastructure.mappers.ResumenVentasMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResumenVentasController.class)
@Import(GlobalExceptionHandler.class)
class ResumenVentasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarResumenVentasUseCase consultarResumenVentasUseCase;

    @MockitoBean
    private ResumenVentasMapper mapper;

    @Test
    void consultarResumenVentas_conEventoExistente_retorna200() throws Exception {
        Long eventoId = 1L;
        EventSnapshotDto dto = createDto(eventoId, "CERRADO", 145, new BigDecimal("62500.00"));
        ConsultarResumenVentasResponse response = createResponse(eventoId, "CERRADO", 145, new BigDecimal("62500.00"));

        when(consultarResumenVentasUseCase.execute(eventoId)).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/resumen-ventas", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.estadoEvento").value("CERRADO"))
                .andExpect(jsonPath("$.totalTicketsVendidos").value(145))
                .andExpect(jsonPath("$.totalRecaudoBruto").value(62500.00));
    }

    @Test
    void consultarResumenVentas_conEventoNoExistente_retorna404() throws Exception {
        Long eventoId = 999L;

        when(consultarResumenVentasUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/eventos/{id}/resumen-ventas", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EVENT_NOT_FOUND"));
    }

    @Test
    void consultarResumenVentas_conEventoNoCerrado_retorna409() throws Exception {
        Long eventoId = 1L;

        when(consultarResumenVentasUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.EVENT_NOT_CLOSED,
                        "El evento aún no ha sido cerrado. Estado actual: EN_CURSO"));

        mockMvc.perform(get("/api/v1/eventos/{id}/resumen-ventas", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EVENT_NOT_CLOSED"));
    }

    @Test
    void consultarResumenVentas_conErrorServicioExterno_retorna502() throws Exception {
        Long eventoId = 1L;

        when(consultarResumenVentasUseCase.execute(eventoId))
                .thenThrow(new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                        new RuntimeException("Connection refused")));

        mockMvc.perform(get("/api/v1/eventos/{id}/resumen-ventas", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"));
    }

    @Test
    void consultarResumenVentas_conIdInvalido_retorna400() throws Exception {
        when(consultarResumenVentasUseCase.execute(null))
                .thenThrow(new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del evento es requerido"));

        mockMvc.perform(get("/api/v1/eventos/{id}/resumen-ventas", "invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private EventSnapshotDto createDto(Long id, String estado, int tickets, BigDecimal recaudo) {
        EventSnapshotDto dto = new EventSnapshotDto();
        dto.setIdEvento(id);
        dto.setNombreEvento("Concierto Rock 2026");
        dto.setEstadoEvento(estado);
        dto.setTotalTicketsVendidos(tickets);
        dto.setTotalRecaudoBruto(recaudo);
        dto.setTicketsPorCondicion(Map.of("VALIDADO", 100, "VENDIDO", 30, "CANCELADO", 5, "CORTESIA", 10));
        dto.setRecaudoPorCondicion(Map.of("VALIDADO", new BigDecimal("50000.00"), "VENDIDO", new BigDecimal("15000.00"),
                "CANCELADO", new BigDecimal("-2500.00"), "CORTESIA", BigDecimal.ZERO));
        return dto;
    }

    private ConsultarResumenVentasResponse createResponse(Long id, String estado, int tickets, BigDecimal recaudo) {
        ConsultarResumenVentasResponse response = new ConsultarResumenVentasResponse();
        response.setEventoId(id);
        response.setNombreEvento("Concierto Rock 2026");
        response.setEstadoEvento(estado);
        response.setTotalTicketsVendidos(tickets);
        response.setTotalRecaudoBruto(recaudo);
        response.setTicketsPorCondicion(Map.of("VALIDADO", 100, "VENDIDO", 30, "CANCELADO", 5, "CORTESIA", 10));
        response.setRecaudoPorCondicion(Map.of("VALIDADO", new BigDecimal("50000.00"), "VENDIDO", new BigDecimal("15000.00"),
                "CANCELADO", new BigDecimal("-2500.00"), "CORTESIA", BigDecimal.ZERO));
        return response;
    }
}
