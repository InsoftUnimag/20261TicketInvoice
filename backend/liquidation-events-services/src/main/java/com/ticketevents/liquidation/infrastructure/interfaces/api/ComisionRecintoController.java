package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.RegistrarComisionRecintoUseCase;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.RegistrarComisionRecintoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/recintos")
public class ComisionRecintoController {

    private static final Logger log = LoggerFactory.getLogger(ComisionRecintoController.class);

    private final RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase;

    public ComisionRecintoController(RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase) {
        this.registrarComisionRecintoUseCase = registrarComisionRecintoUseCase;
    }

    @PostMapping("/{id}/comision")
    public ResponseEntity<RegistrarComisionRecintoResponse> registrarComision(
            @PathVariable("id") Long recintoId,
            @Valid @RequestBody RegistrarComisionRecintoRequest request) {
        log.info("Solicitud de registro de comision para recinto: {}", recintoId);
        
        RegistrarComisionRecintoResponse response = registrarComisionRecintoUseCase.execute(
            recintoId,
            request.getTipoComision(),
            request.getValorComision()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}