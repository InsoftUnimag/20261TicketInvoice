package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.CalcularDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.CalcularDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class CalcularDistribucionController {

    private static final Logger log = LoggerFactory.getLogger(CalcularDistribucionController.class);

    private final CalcularDistribucionRecaudoUseCase calcularDistribucionUseCase;
    private final DistribucionRecaudoMapper distribucionMapper;

    public CalcularDistribucionController(CalcularDistribucionRecaudoUseCase calcularDistribucionUseCase,
                                           DistribucionRecaudoMapper distribucionMapper) {
        this.calcularDistribucionUseCase = calcularDistribucionUseCase;
        this.distribucionMapper = distribucionMapper;
    }

    @PostMapping("/{id}/calcular-distribucion")
    public ResponseEntity<CalcularDistribucionResponse> calcularDistribucion(@PathVariable("id") Long eventoId) {
        log.info("Solicitud de calculo de distribucion del recaudo para evento: {}", eventoId);

        DistribucionRecaudoDto dto = calcularDistribucionUseCase.execute(eventoId);

        CalcularDistribucionResponse response = distribucionMapper.toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
