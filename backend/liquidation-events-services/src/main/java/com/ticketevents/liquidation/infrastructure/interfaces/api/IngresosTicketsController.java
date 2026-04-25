package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarIngresosTicketsUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class IngresosTicketsController {

    private static final Logger log = LoggerFactory.getLogger(IngresosTicketsController.class);

    private final ConsultarIngresosTicketsUseCase consultarIngresosTicketsUseCase;

    public IngresosTicketsController(ConsultarIngresosTicketsUseCase consultarIngresosTicketsUseCase) {
        this.consultarIngresosTicketsUseCase = consultarIngresosTicketsUseCase;
    }

    @GetMapping("/{id}/ingresos")
    public ResponseEntity<ConsultarIngresosResponse> consultarIngresos(
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de ingresos de tickets para evento: {}", eventoId);
        ConsultarIngresosResponse response = consultarIngresosTicketsUseCase.execute(eventoId);
        return ResponseEntity.ok(response);
    }
}