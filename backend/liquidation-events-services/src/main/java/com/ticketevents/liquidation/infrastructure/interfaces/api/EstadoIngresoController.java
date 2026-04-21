package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarEstadoIngresoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.ConsultarEstadoIngresoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarEstadoIngresoResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class EstadoIngresoController {

    private static final Logger log = LoggerFactory.getLogger(EstadoIngresoController.class);

    private final ConsultarEstadoIngresoUseCase consultarEstadoIngresoUseCase;

    public EstadoIngresoController(ConsultarEstadoIngresoUseCase consultarEstadoIngresoUseCase) {
        this.consultarEstadoIngresoUseCase = consultarEstadoIngresoUseCase;
    }

    @GetMapping("/{id}/estado-ingreso")
    public ResponseEntity<ConsultarEstadoIngresoResponse> consultarEstadoIngreso(
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de estado de ingreso para evento: {}", eventoId);
        
        ConsultarEstadoIngresoRequest request = new ConsultarEstadoIngresoRequest(eventoId);
        ConsultarEstadoIngresoResponse response = consultarEstadoIngresoUseCase.execute(request.getEventoId());
        
        return ResponseEntity.ok(response);
    }
}