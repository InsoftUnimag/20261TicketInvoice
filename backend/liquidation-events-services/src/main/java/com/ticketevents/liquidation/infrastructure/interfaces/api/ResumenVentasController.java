package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarResumenVentasUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class ResumenVentasController {

    private static final Logger log = LoggerFactory.getLogger(ResumenVentasController.class);

    private final ConsultarResumenVentasUseCase consultarResumenVentasUseCase;

    public ResumenVentasController(ConsultarResumenVentasUseCase consultarResumenVentasUseCase) {
        this.consultarResumenVentasUseCase = consultarResumenVentasUseCase;
    }

    @GetMapping("/{id}/resumen-ventas")
    public ResponseEntity<ConsultarResumenVentasResponse> consultarResumenVentas(
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de resumen de ventas para evento: {}", eventoId);
        ConsultarResumenVentasResponse response = consultarResumenVentasUseCase.execute(eventoId);
        return ResponseEntity.ok(response);
    }
}