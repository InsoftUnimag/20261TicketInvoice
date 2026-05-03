package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionRecintoDto;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import com.ticketevents.liquidation.application.usecase.RegistrarComisionRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.RegistrarComisionRecintoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ComisionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recintos")
public class ComisionRecintoController {

    private static final Logger log = LoggerFactory.getLogger(ComisionRecintoController.class);

    private final RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase;
    private final ConsultarComisionRecintoUseCase consultarComisionRecintoUseCase;
    private final ComisionRecintoMapper mapper;

    public ComisionRecintoController(RegistrarComisionRecintoUseCase registrarComisionRecintoUseCase,
                                     ConsultarComisionRecintoUseCase consultarComisionRecintoUseCase,
                                     ComisionRecintoMapper mapper) {
        this.registrarComisionRecintoUseCase = registrarComisionRecintoUseCase;
        this.consultarComisionRecintoUseCase = consultarComisionRecintoUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}/comision")
    public ResponseEntity<ComisionResponse> consultarComision(@PathVariable("id") Long recintoId) {
        log.info("Solicitud de consulta de comision para recinto: {}", recintoId);

        ConsultarComisionRecintoResult result = consultarComisionRecintoUseCase.ejecutar(recintoId);
        ComisionResponse response = new ComisionResponse(
                result.configurada(),
                result.mensaje(),
                result.tipoComision(),
                result.valorComision()
        );

        return ResponseEntity.ok(response);
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

    @GetMapping("/{id}/comision/configurar")
    public ResponseEntity<RegistrarComisionRecintoResponse> registrarComisionDesdeNavegador(
            @PathVariable("id") Long recintoId,
            @RequestParam("tipoComision") TipoComision tipoComision,
            @RequestParam("valorComision") BigDecimal valorComision) {
        log.info("Solicitud GET de registro de comision para recinto: {}", recintoId);

        ComisionRecintoDto dto = registrarComisionRecintoUseCase.execute(
                recintoId,
                tipoComision,
                valorComision
        );

        RegistrarComisionRecintoResponse response = mapper.toResponse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
