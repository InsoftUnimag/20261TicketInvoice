package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import com.ticketevents.liquidation.application.usecase.RegistrarComisionRecintoUseCase;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.RegistrarComisionRecintoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionRecintoDto;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
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

@WebMvcTest(ComisionRecintoController.class)
@Import(GlobalExceptionHandler.class)
class ComisionRecintoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase;

    @MockitoBean
    private ConsultarComisionRecintoUseCase consultarComisionRecintoUseCase;

    @MockitoBean
    private ComisionRecintoMapper mapper;

    @Test
    void consultarComision_conComisionConfigurada_retorna200() throws Exception {
        Long recintoId = 1L;
        ConsultarComisionRecintoResult result = new ConsultarComisionRecintoResult(true, "Comision configurada",
                "PORCENTAJE", new BigDecimal("0.12"));

        when(consultarComisionRecintoUseCase.ejecutar(recintoId)).thenReturn(result);

        mockMvc.perform(get("/api/v1/recintos/{id}/comision", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurada").value(true))
                .andExpect(jsonPath("$.tipoComision").value("PORCENTAJE"))
                .andExpect(jsonPath("$.valorComision").value(0.12));
    }

    @Test
    void consultarComision_sinComision_retorna200() throws Exception {
        Long recintoId = 1L;
        ConsultarComisionRecintoResult result = new ConsultarComisionRecintoResult(false,
                "El recinto no tiene una comision registrada", null, null);

        when(consultarComisionRecintoUseCase.ejecutar(recintoId)).thenReturn(result);

        mockMvc.perform(get("/api/v1/recintos/{id}/comision", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurada").value(false));
    }

    @Test
    void consultarComision_recintoNoExistente_retorna404() throws Exception {
        Long recintoId = 999L;

        when(consultarComisionRecintoUseCase.ejecutar(recintoId))
                .thenThrow(new BusinessException(ErrorCode.RECINTO_NOT_FOUND));

        mockMvc.perform(get("/api/v1/recintos/{id}/comision", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RECINTO_NOT_FOUND"));
    }

    @Test
    void registrarComisionPost_conDatosValidos_retorna201() throws Exception {
        Long recintoId = 1L;
        ComisionRecintoDto dto = new ComisionRecintoDto(1L, recintoId, "PORCENTAJE", new BigDecimal("0.12"),
                LocalDateTime.now());
        RegistrarComisionRecintoResponse response = new RegistrarComisionRecintoResponse();
        response.setId(1L);
        response.setRecintoId(recintoId);
        response.setTipoComision("PORCENTAJE");
        response.setValorComision(new BigDecimal("0.12"));

        when(registrarComisionRecintoUseCase.execute(eq(recintoId), eq(TipoComision.PORCENTAJE),
                eq(new BigDecimal("0.12")))).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        String requestJson = """
                {
                    "tipoComision": "PORCENTAJE",
                    "valorComision": 0.12
                }
                """;

        mockMvc.perform(post("/api/v1/recintos/{id}/comision", recintoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recintoId").value(recintoId))
                .andExpect(jsonPath("$.tipoComision").value("PORCENTAJE"))
                .andExpect(jsonPath("$.valorComision").value(0.12));
    }

    @Test
    void registrarComisionPost_conValorNegativo_retorna400() throws Exception {
        String requestJson = """
                {
                    "tipoComision": "PORCENTAJE",
                    "valorComision": -0.10
                }
                """;

        mockMvc.perform(post("/api/v1/recintos/1/comision")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void registrarComisionPost_sinTipoComision_retorna400() throws Exception {
        String requestJson = """
                {
                    "valorComision": 0.12
                }
                """;

        mockMvc.perform(post("/api/v1/recintos/1/comision")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void registrarComisionGet_conParametrosValidos_retorna201() throws Exception {
        Long recintoId = 1L;
        ComisionRecintoDto dto = new ComisionRecintoDto(1L, recintoId, "VALOR_FIJO", new BigDecimal("5000.00"),
                LocalDateTime.now());
        RegistrarComisionRecintoResponse response = new RegistrarComisionRecintoResponse();
        response.setId(1L);
        response.setRecintoId(recintoId);
        response.setTipoComision("VALOR_FIJO");
        response.setValorComision(new BigDecimal("5000.00"));

        when(registrarComisionRecintoUseCase.execute(eq(recintoId), eq(TipoComision.VALOR_FIJO),
                eq(new BigDecimal("5000.00")))).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/recintos/{id}/comision/configurar", recintoId)
                .param("tipoComision", "VALOR_FIJO")
                .param("valorComision", "5000.00")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recintoId").value(recintoId))
                .andExpect(jsonPath("$.tipoComision").value("VALOR_FIJO"));
    }
}
