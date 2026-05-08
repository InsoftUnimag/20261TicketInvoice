package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.CalcularDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.CalcularDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalcularDistribucionController.class)
@Import(GlobalExceptionHandler.class)
class CalcularDistribucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalcularDistribucionRecaudoUseCase calcularDistribucionUseCase;

    @MockitoBean
    private DistribucionRecaudoMapper distribucionMapper;

    @Test
    void calcularDistribucionPost_conEventoValido_retorna201() throws Exception {
        Long eventoId = 1L;
        DistribucionRecaudoDto dto = createDto(eventoId, "Concierto Rock 2026", new BigDecimal("62500.00"),
                new BigDecimal("56000.00"), new BigDecimal("48200.00"), "PRELIMINAR");
        CalcularDistribucionResponse response = createResponse(eventoId, "Concierto Rock 2026",
                new BigDecimal("62500.00"), new BigDecimal("56000.00"), new BigDecimal("48200.00"), "PRELIMINAR");

        when(calcularDistribucionUseCase.execute(eventoId)).thenReturn(dto);
        when(distribucionMapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.estado").value("PRELIMINAR"))
                .andExpect(jsonPath("$.totalBruto").value(62500.00));
    }

    @Test
    void calcularDistribucionGet_conEventoValido_retorna201() throws Exception {
        Long eventoId = 1L;
        DistribucionRecaudoDto dto = createDto(eventoId, "Concierto Rock 2026", new BigDecimal("62500.00"),
                new BigDecimal("56000.00"), new BigDecimal("48200.00"), "PRELIMINAR");
        CalcularDistribucionResponse response = createResponse(eventoId, "Concierto Rock 2026",
                new BigDecimal("62500.00"), new BigDecimal("56000.00"), new BigDecimal("48200.00"), "PRELIMINAR");

        when(calcularDistribucionUseCase.execute(eventoId)).thenReturn(dto);
        when(distribucionMapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventoId").value(eventoId));
    }

    @Test
    void calcularDistribucion_conEventoNoExistente_retorna404() throws Exception {
        Long eventoId = 999L;

        when(calcularDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EVENT_NOT_FOUND"));
    }

    @Test
    void calcularDistribucion_conEventoNoCerrado_retorna409() throws Exception {
        Long eventoId = 1L;

        when(calcularDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.EVENT_NOT_CLOSED,
                        "El evento aun no ha sido cerrado. Estado actual: EN_CURSO"));

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EVENT_NOT_CLOSED"));
    }

    @Test
    void calcularDistribucion_conRecintoNoEncontrado_retorna404() throws Exception {
        Long eventoId = 1L;

        when(calcularDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.RECINTO_NOT_FOUND));

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RECINTO_NOT_FOUND"));
    }

    @Test
    void calcularDistribucion_sinConfiguracion_retorna400() throws Exception {
        Long eventoId = 1L;

        when(calcularDistribucionUseCase.execute(eventoId))
                .thenThrow(new BusinessException(ErrorCode.INVALID_REQUEST,
                        "No se ha definido la configuracion de liquidacion para el evento"));

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void calcularDistribucion_conErrorServicioExterno_retorna502() throws Exception {
        Long eventoId = 1L;

        when(calcularDistribucionUseCase.execute(eventoId))
                .thenThrow(new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                        new RuntimeException("Connection refused")));

        mockMvc.perform(post("/api/v1/eventos/{id}/calcular-distribucion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"));
    }

    private DistribucionRecaudoDto createDto(Long id, String nombre, BigDecimal bruto,
                                              BigDecimal neto, BigDecimal distribuible, String estado) {
        DistribucionRecaudoDto dto = new DistribucionRecaudoDto();
        dto.setEventoId(id);
        dto.setNombreEvento(nombre);
        dto.setTotalBruto(bruto);
        dto.setTotalNetoPreliminar(neto);
        dto.setTotalDistribuible(distribuible);
        dto.setComisionPlataforma(new BigDecimal("6500.00"));
        dto.setEstado(estado);
        dto.setFechaCalculo(LocalDateTime.now());
        return dto;
    }

    private CalcularDistribucionResponse createResponse(Long id, String nombre, BigDecimal bruto,
                                                         BigDecimal neto, BigDecimal distribuible, String estado) {
        CalcularDistribucionResponse response = new CalcularDistribucionResponse();
        response.setEventoId(id);
        response.setNombreEvento(nombre);
        response.setTotalBruto(bruto);
        response.setTotalNetoPreliminar(neto);
        response.setTotalDistribuible(distribuible);
        response.setComisionPlataforma(new BigDecimal("6500.00"));
        response.setEstado(estado);
        response.setFechaCalculo(LocalDateTime.now());
        response.setMensaje("Distribucion del recaudo calculada exitosamente");
        return response;
    }
}
