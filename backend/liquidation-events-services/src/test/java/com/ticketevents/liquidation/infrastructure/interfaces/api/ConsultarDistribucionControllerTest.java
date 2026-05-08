package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.ConsultarDistribucionMapper;
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
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultarDistribucionController.class)
@Import(GlobalExceptionHandler.class)
class ConsultarDistribucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarDistribucionRecaudoUseCase consultarDistribucionUseCase;

    @MockitoBean
    private ConsultarDistribucionMapper consultarMapper;

    @Test
    void consultarDistribucion_conEventoLiquidado_retorna200() throws Exception {
        Long eventoId = 1L;
        DistribucionRecaudoDto dto = createDto(eventoId, "Concierto Rock 2026", new BigDecimal("62500.00"),
                new BigDecimal("48200.00"), new BigDecimal("6500.00"), "LIQUIDADO");
        ConsultarDistribucionResponse response = createResponse(eventoId, "Concierto Rock 2026",
                new BigDecimal("62500.00"), new BigDecimal("48200.00"), new BigDecimal("6500.00"), "LIQUIDADO");

        when(consultarDistribucionUseCase.execute(eventoId)).thenReturn(dto);
        when(consultarMapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/distribucion-recaudo", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.estado").value("LIQUIDADO"))
                .andExpect(jsonPath("$.totalPagoPromotor").value(48200.00))
                .andExpect(jsonPath("$.totalComisionPlataforma").value(6500.00));
    }

    @Test
    void consultarDistribucion_conDistribucionNoEncontrada_retorna404() throws Exception {
        Long eventoId = 999L;

        when(consultarDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.DISTRIBUTION_NOT_FOUND));

        mockMvc.perform(get("/api/v1/eventos/{id}/distribucion-recaudo", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("DISTRIBUTION_NOT_FOUND"));
    }

    @Test
    void consultarDistribucion_conEventoNoLiquidado_retorna409() throws Exception {
        Long eventoId = 1L;

        when(consultarDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.DISTRIBUTION_NOT_LIQUIDATED,
                        "El evento aun no tiene liquidacion disponible"));

        mockMvc.perform(get("/api/v1/eventos/{id}/distribucion-recaudo", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DISTRIBUTION_NOT_LIQUIDATED"));
    }

    @Test
    void consultarDistribucion_conErrorServicioExterno_retorna502() throws Exception {
        Long eventoId = 1L;

        when(consultarDistribucionUseCase.execute(eventoId))
                .thenThrow(new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                        new RuntimeException("Connection refused")));

        mockMvc.perform(get("/api/v1/eventos/{id}/distribucion-recaudo", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"));
    }

    private DistribucionRecaudoDto createDto(Long id, String nombre, BigDecimal bruto,
                                              BigDecimal pagoPromotor, BigDecimal comision, String estado) {
        DistribucionRecaudoDto dto = new DistribucionRecaudoDto();
        dto.setEventoId(id);
        dto.setNombreEvento(nombre);
        dto.setTotalBruto(bruto);
        dto.setTotalPagoPromotor(pagoPromotor);
        dto.setComisionPlataforma(comision);
        dto.setTotalDistribuible(pagoPromotor);
        dto.setEstado(estado);
        dto.setFechaCalculo(LocalDateTime.now());
        dto.setFechaLiquidacion(LocalDateTime.now());
        return dto;
    }

    private ConsultarDistribucionResponse createResponse(Long id, String nombre, BigDecimal bruto,
                                                          BigDecimal pagoPromotor, BigDecimal comision, String estado) {
        ConsultarDistribucionResponse response = new ConsultarDistribucionResponse();
        response.setEventoId(id);
        response.setNombreEvento(nombre);
        response.setTotalBruto(bruto);
        response.setTotalPagoPromotor(pagoPromotor);
        response.setTotalComisionPlataforma(comision);
        response.setTotalDistribuible(pagoPromotor);
        response.setEstado(estado);
        response.setFechaCalculo(LocalDateTime.now());
        response.setFechaLiquidacion(LocalDateTime.now());
        return response;
    }
}
