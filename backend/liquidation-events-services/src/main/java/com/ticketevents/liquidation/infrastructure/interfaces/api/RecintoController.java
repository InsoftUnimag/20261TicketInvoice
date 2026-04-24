package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recintos")
public class RecintoController {

    private static final Logger log = LoggerFactory.getLogger(RecintoController.class);

    private final ConsultarRecintoUseCase consultarRecintoUseCase;

    public RecintoController(ConsultarRecintoUseCase consultarRecintoUseCase) {
        this.consultarRecintoUseCase = consultarRecintoUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultarRecintoResponse> consultarRecinto(
            @PathVariable("id") Long id) {
        log.info("Solicitud de recinto: {}", id);
        
        ConsultarRecintoResponse response = consultarRecintoUseCase.execute(id);
        
        return ResponseEntity.ok(response);
    }
}