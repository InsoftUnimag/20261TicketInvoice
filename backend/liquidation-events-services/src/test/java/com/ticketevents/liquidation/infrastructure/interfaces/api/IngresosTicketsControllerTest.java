package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarIngresosTicketsUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.IngresosTicketsDto;
import com.ticketevents.liquidation.infrastructure.mappers.IngresosMapper;
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

@WebMvcTest(IngresosTicketsController.class)
@Import(GlobalExceptionHandler.class)
class IngresosTicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarIngresosTicketsUseCase consultarIngresosTicketsUseCase;

    @MockitoBean
    private IngresosMapper mapper;

    @Test
    void consultarIngresos_conEventoExistente_retorna200() throws Exception {
        Long eventoId = 1L;
        IngresosTicketsDto dto = createDto(eventoId, 145, 100, 5, 10, 30, new BigDecimal("62500.00"), false);
        ConsultarIngresosResponse response = createResponse(eventoId, 145, 100, 5, 10, 30, new BigDecimal("62500.00"), false);

        when(consultarIngresosTicketsUseCase.execute(eventoId)).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/ingresos", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.totalTicketsVendidos").value(145))
                .andExpect(jsonPath("$.totalRecaudoBruto").value(62500.00))
                .andExpect(jsonPath("$.hasInconsistencies").value(false));
    }

    @Test
    void consultarIngresos_conEventoNoExistente_retorna404() throws Exception {
        Long eventoId = 999L;

        when(consultarIngresosTicketsUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/eventos/{id}/ingresos", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EVENT_NOT_FOUND"));
    }

    @Test
    void consultarIngresos_conErrorServicioExterno_retorna502() throws Exception {
        Long eventoId = 1L;

        when(consultarIngresosTicketsUseCase.execute(eventoId))
                .thenThrow(new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                        new RuntimeException("Connection refused")));

        mockMvc.perform(get("/api/v1/eventos/{id}/ingresos", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"));
    }

    @Test
    void consultarIngresos_conInconsistencias_retorna200ConWarning() throws Exception {
        Long eventoId = 3L;
        IngresosTicketsDto dto = createDto(eventoId, 1, 0, 0, 0, 0, new BigDecimal("1000.00"), true);
        ConsultarIngresosResponse response = createResponse(eventoId, 1, 0, 0, 0, 0, new BigDecimal("1000.00"), true);

        when(consultarIngresosTicketsUseCase.execute(eventoId)).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/ingresos", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasInconsistencies").value(true));
    }

    private IngresosTicketsDto createDto(Long id, int vendidos, int validados, int cancelados, int cortesias,
                                          int noAsistieron, BigDecimal recaudo, boolean inconsistencias) {
        IngresosTicketsDto dto = new IngresosTicketsDto();
        dto.setEventoId(id);
        dto.setTotalTicketsVendidos(vendidos);
        dto.setTotalTicketsValidados(validados);
        dto.setTotalTicketsCancelados(cancelados);
        dto.setTotalCortesias(cortesias);
        dto.setTotalNoAsistieron(noAsistieron);
        dto.setTotalRecaudoBruto(recaudo);
        dto.setHasInconsistencias(inconsistencias);
        dto.setTicketsPorEstado(Map.of("VALIDADO", validados, "CANCELADO", cancelados, "CORTESIA", cortesias));
        return dto;
    }

    private ConsultarIngresosResponse createResponse(Long id, int vendidos, int validados, int cancelados, int cortesias,
                                                      int noAsistieron, BigDecimal recaudo, boolean inconsistencias) {
        ConsultarIngresosResponse response = new ConsultarIngresosResponse();
        response.setEventoId(id);
        response.setTotalTicketsVendidos(vendidos);
        response.setTotalTicketsValidados(validados);
        response.setTotalTicketsCancelados(cancelados);
        response.setTotalCortesias(cortesias);
        response.setTotalNoAsistieron(noAsistieron);
        response.setTotalRecaudoBruto(recaudo);
        response.setHasInconsistencies(inconsistencias);
        response.setTicketsPorEstado(Map.of("VALIDADO", validados, "CANCELADO", cancelados, "CORTESIA", cortesias));
        return response;
    }
}
