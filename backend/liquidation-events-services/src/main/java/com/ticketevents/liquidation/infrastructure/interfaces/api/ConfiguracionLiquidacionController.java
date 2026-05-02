package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ConfiguracionLiquidacionDto;
import com.ticketevents.liquidation.application.usecase.DeterminarTipoLiquidacionUseCase;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.DeterminarTipoLiquidacionRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ConfiguracionLiquidacionMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/eventos")
public class ConfiguracionLiquidacionController {

    private static final Logger log = LoggerFactory.getLogger(ConfiguracionLiquidacionController.class);

    private final DeterminarTipoLiquidacionUseCase determinarTipoLiquidacionUseCase;
    private final ConfiguracionLiquidacionRepository repository;
    private final ConfiguracionLiquidacionMapper mapper;

    public ConfiguracionLiquidacionController(DeterminarTipoLiquidacionUseCase determinarTipoLiquidacionUseCase,
                                             ConfiguracionLiquidacionRepository repository,
                                             ConfiguracionLiquidacionMapper mapper) {
        this.determinarTipoLiquidacionUseCase = determinarTipoLiquidacionUseCase;
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping("/{id}/configuracion-liquidacion")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> consultarConfiguracionLiquidacion(
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de consulta de configuracion de liquidacion para evento: {}", eventoId);

        Optional<ConfiguracionLiquidacion> config = repository.findByEventoId(eventoId);
        if (config.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DeterminarTipoLiquidacionResponse response = mapper.toResponse(mapper.toDto(config.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/configuracion-liquidacion")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> configurarLiquidacion(
            @PathVariable("id") Long eventoId,
            @Valid @RequestBody DeterminarTipoLiquidacionRequest request) {
        log.info("Solicitud de configuracion de liquidacion para evento: {}", eventoId);

        ConfiguracionLiquidacionDto dto = determinarTipoLiquidacionUseCase.execute(
            eventoId,
            request.getTipoLiquidacion(),
            request.getValorComision(),
            request.getPorcentaje()
        );

        DeterminarTipoLiquidacionResponse response = mapper.toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
