package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionRecintoDto;
import com.ticketevents.liquidation.application.usecase.RegistrarComisionRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.RegistrarComisionRecintoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
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
    private final ComisionRecintoMapper mapper;

    public ComisionRecintoController(RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase,
                                   ComisionRecintoMapper mapper) {
        this.registrarComisionRecintoUseCase = registrarComisionRecintoUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/{id}/comision")
    public ResponseEntity<RegistrarComisionRecintoResponse> registrarComision(
            @PathVariable("id") Long recintoId,
            @Valid @RequestBody RegistrarComisionRecintoRequest request) {
        log.info("Solicitud de registro de comision para recinto: {}", recintoId);

        ComisionRecintoDto dto = registrarComisionRecintoUseCase.execute(
            recintoId,
            request.getTipoComision(),
            request.getValorComision()
        );

        RegistrarComisionRecintoResponse response = mapper.toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}