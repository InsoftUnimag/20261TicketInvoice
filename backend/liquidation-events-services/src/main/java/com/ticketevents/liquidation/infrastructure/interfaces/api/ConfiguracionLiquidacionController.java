package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.DeterminarTipoLiquidacionUseCase;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.DeterminarTipoLiquidacionRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/eventos")
public class ConfiguracionLiquidacionController {

    private static final Logger log = LoggerFactory.getLogger(ConfiguracionLiquidacionController.class);

    private final DeterminarTipoLiquidacionUseCase determinarTipoLiquidacionUseCase;

    public ConfiguracionLiquidacionController(DeterminarTipoLiquidacionUseCase determinarTipoLiquidacionUseCase) {
        this.determinarTipoLiquidacionUseCase = determinarTipoLiquidacionUseCase;
    }

    @PostMapping("/{id}/configuracion-liquidacion")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> configurarLiquidacion(
            @PathVariable("id") Long eventoId,
            @Valid @RequestBody DeterminarTipoLiquidacionRequest request) {
        log.info("Solicitud de configuracion de liquidacion para evento: {}", eventoId);
        
        DeterminarTipoLiquidacionResponse response = determinarTipoLiquidacionUseCase.execute(
            eventoId,
            request.getTipoLiquidacion(),
            request.getValorComision(),
            request.getPorcentaje()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}