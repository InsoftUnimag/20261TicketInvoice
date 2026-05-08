package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.RecintoDto;
import com.ticketevents.liquidation.infrastructure.mappers.RecintoMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecintoController.class)
@Import(GlobalExceptionHandler.class)
class RecintoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarRecintoUseCase consultarRecintoUseCase;

    @MockitoBean
    private RecintoMapper mapper;

    @Test
    void consultarRecinto_conRecintoExistente_retorna200() throws Exception {
        Long recintoId = 1L;
        RecintoDto dto = new RecintoDto(recintoId, "Estadio Nacional", "ESTADIO", new BigDecimal("0.15"), null, "ACTIVO");
        ConsultarRecintoResponse response = new ConsultarRecintoResponse();
        response.setId(recintoId);
        response.setNombre("Estadio Nacional");
        response.setTipoRecinto("ESTADIO");
        response.setTasaComision(new BigDecimal("0.15"));
        response.setEstado("ACTIVO");

        when(consultarRecintoUseCase.execute(recintoId)).thenReturn(dto);
        when(mapper.toResponse(dto)).thenReturn(response);

        mockMvc.perform(get("/api/v1/recintos/{id}", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recintoId))
                .andExpect(jsonPath("$.tipoRecinto").value("ESTADIO"))
                .andExpect(jsonPath("$.tasaComision").value(0.15));
    }

    @Test
    void consultarRecinto_conRecintoNoExistente_retorna404() throws Exception {
        Long recintoId = 999L;

        when(consultarRecintoUseCase.execute(recintoId))
                .thenThrow(new BusinessException(ErrorCode.RECINTO_NOT_FOUND));

        mockMvc.perform(get("/api/v1/recintos/{id}", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RECINTO_NOT_FOUND"));
    }

    @Test
    void consultarRecinto_conErrorServicioExterno_retorna502() throws Exception {
        Long recintoId = 1L;

        when(consultarRecintoUseCase.execute(recintoId))
                .thenThrow(new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                        new RuntimeException("Connection refused")));

        mockMvc.perform(get("/api/v1/recintos/{id}", recintoId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"));
    }
}
