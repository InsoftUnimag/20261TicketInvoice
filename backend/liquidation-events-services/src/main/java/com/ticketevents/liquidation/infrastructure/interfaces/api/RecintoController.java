package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.RecintoDto;
import com.ticketevents.liquidation.application.usecase.ConsultarRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.RecintoMapper;
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
    private final RecintoMapper mapper;

    public RecintoController(ConsultarRecintoUseCase consultarRecintoUseCase,
                            RecintoMapper mapper) {
        this.consultarRecintoUseCase = consultarRecintoUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultarRecintoResponse> consultarRecinto(
            @PathVariable("id") Long id) {
        log.info("Solicitud de recinto: {}", id);

        RecintoDto dto = consultarRecintoUseCase.execute(id);
        ConsultarRecintoResponse response = mapper.toResponse(dto);

        return ResponseEntity.ok(response);
    }
}