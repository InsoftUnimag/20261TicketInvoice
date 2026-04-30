package com.ticketevents.liquidation.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class ComisionConsultaApiContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultarComisionRecintoUseCase useCase;

    @Test
    void contratoDebeResponder200ConPayloadEsperado() throws Exception {
        when(useCase.ejecutar(1L))
                .thenReturn(new ConsultarComisionRecintoResult(true, "Comision configurada", "PORCENTUAL", new BigDecimal("7.50")));

        mockMvc.perform(get("/api/v1/recintos/1/comision"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurada").value(true))
                .andExpect(jsonPath("$.tipoComision").value("PORCENTUAL"))
                .andExpect(jsonPath("$.valorComision").value(7.50));
    }
}
