package com.ticketevents.liquidation.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionConsultaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultarComisionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComisionConsultaRepository repository;

    @Test
    void debeResolverTresEscenarios() throws Exception {
        when(repository.existsRecintoById(1L)).thenReturn(true);
        when(repository.findComisionByRecintoId(1L))
                .thenReturn(Optional.of(new ComisionConfig(true, TipoComision.PORCENTUAL, new BigDecimal("12.00"))));
        when(repository.existsRecintoById(2L)).thenReturn(true);
        when(repository.findComisionByRecintoId(2L)).thenReturn(Optional.empty());
        when(repository.existsRecintoById(999L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/recintos/1/comision"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurada").value(true))
                .andExpect(jsonPath("$.tipoComision").value("PORCENTUAL"))
                .andExpect(jsonPath("$.valorComision").value(12.00));

        mockMvc.perform(get("/api/v1/recintos/2/comision"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurada").value(false))
                .andExpect(jsonPath("$.mensaje").value("El recinto no tiene una comision registrada"))
                .andExpect(jsonPath("$.tipoComision").doesNotExist());

        mockMvc.perform(get("/api/v1/recintos/999/comision"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("VENUE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("El recinto no esta registrado"));
    }
}
