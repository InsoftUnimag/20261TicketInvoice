package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.DeterminarTipoLiquidacionUseCase;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.DeterminarTipoLiquidacionRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ConfiguracionLiquidacionDto;
import com.ticketevents.liquidation.infrastructure.mappers.ConfiguracionLiquidacionMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfiguracionLiquidacionController.class)
@Import(GlobalExceptionHandler.class)
class ConfiguracionLiquidacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeterminarTipoLiquidacionUseCase determinarTipoLiquidacionUseCase;

    @MockitoBean
    private ConfiguracionLiquidacionRepository repository;

    @MockitoBean
    private ConfiguracionLiquidacionMapper mapper;

    @Test
    void consultarConfiguracion_conConfigExistente_retorna200() throws Exception {
        Long eventoId = 1L;
        ConfiguracionLiquidacion config = new ConfiguracionLiquidacion(1L, eventoId, TipoLiquidacion.TARIFA_PLANA,
                new BigDecimal("5000.00"), null);
        ConfiguracionLiquidacionDto dto = new ConfiguracionLiquidacionDto(1L, eventoId, "TARIFA_PLANA",
                new BigDecimal("5000.00"), BigDecimal.ZERO);
        DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
        response.setEventoId(eventoId);
        response.setTipoLiquidacion("TARIFA_PLANA");
        response.setValorComision(new BigDecimal("5000.00"));

        when(repository.findByEventoId(eventoId)).thenReturn(Optional.of(config));
        when(mapper.toDto(config)).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/configuracion-liquidacion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.tipoLiquidacion").value("TARIFA_PLANA"));
    }

    @Test
    void consultarConfiguracion_sinConfig_retorna200ConMensaje() throws Exception {
        Long eventoId = 1L;

        when(repository.findByEventoId(eventoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/eventos/{id}/configuracion-liquidacion", eventoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.mensaje").value("El evento no tiene configuracion de liquidacion registrada"));
    }

    @Test
    void configurarLiquidacionPost_conDatosValidos_retorna201() throws Exception {
        Long eventoId = 1L;
        ConfiguracionLiquidacionDto dto = new ConfiguracionLiquidacionDto(1L, eventoId, "REPARTO_INGRESOS", null,
                new BigDecimal("0.15"));
        DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
        response.setEventoId(eventoId);
        response.setTipoLiquidacion("REPARTO_INGRESOS");
        response.setPorcentaje(new BigDecimal("0.15"));
        response.setMensaje("Tipo de liquidacion configurado exitosamente");

        when(determinarTipoLiquidacionUseCase.execute(eq(eventoId), eq(TipoLiquidacion.REPARTO_INGRESOS),
                isNull(), eq(new BigDecimal("0.15")))).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        String requestJson = """
                {
                    "tipoLiquidacion": "REPARTO_INGRESOS",
                    "porcentaje": 0.15
                }
                """;

        mockMvc.perform(post("/api/v1/eventos/{id}/configuracion-liquidacion", eventoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.tipoLiquidacion").value("REPARTO_INGRESOS"))
                .andExpect(jsonPath("$.porcentaje").value(0.15));
    }

    @Test
    void configurarLiquidacionPost_sinTipoLiquidacion_retorna400() throws Exception {
        String requestJson = """
                {
                    "valorComision": 5000.00
                }
                """;

        mockMvc.perform(post("/api/v1/eventos/1/configuracion-liquidacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void configurarLiquidacionGet_conParametrosValidos_retorna201() throws Exception {
        Long eventoId = 1L;
        ConfiguracionLiquidacionDto dto = new ConfiguracionLiquidacionDto(1L, eventoId, "TARIFA_PLANA",
                new BigDecimal("5000.00"), BigDecimal.ZERO);
        DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
        response.setEventoId(eventoId);
        response.setTipoLiquidacion("TARIFA_PLANA");
        response.setValorComision(new BigDecimal("5000.00"));

        when(determinarTipoLiquidacionUseCase.execute(eq(eventoId), eq(TipoLiquidacion.TARIFA_PLANA),
                eq(new BigDecimal("5000.00")), isNull())).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/eventos/{id}/configuracion-liquidacion/configurar", eventoId)
                .param("tipoLiquidacion", "TARIFA_PLANA")
                .param("valorComision", "5000.00")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventoId").value(eventoId))
                .andExpect(jsonPath("$.tipoLiquidacion").value("TARIFA_PLANA"));
    }

    @Test
    void configurarLiquidacionGet_sinTipoLiquidacion_retorna400() throws Exception {
        mockMvc.perform(get("/api/v1/eventos/1/configuracion-liquidacion/configurar")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
