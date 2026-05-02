package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.ConsultarDistribucionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class ConsultarDistribucionController {

    private static final Logger log = LoggerFactory.getLogger(ConsultarDistribucionController.class);

    private final ConsultarDistribucionRecaudoUseCase consultarDistribucionUseCase;
    private final ConsultarDistribucionMapper consultarMapper;

    public ConsultarDistribucionController(ConsultarDistribucionRecaudoUseCase consultarDistribucionUseCase,
                                             ConsultarDistribucionMapper consultarMapper) {
        this.consultarDistribucionUseCase = consultarDistribucionUseCase;
        this.consultarMapper = consultarMapper;
    }

    @GetMapping("/{id}/distribucion-recaudo")
    public ResponseEntity<ConsultarDistribucionResponse> consultarDistribucion(@PathVariable("id") Long eventoId) {
        log.info("Solicitud de consulta de distribucion del recaudo para evento: {}", eventoId);

        DistribucionRecaudoDto dto = consultarDistribucionUseCase.execute(eventoId);

        ConsultarDistribucionResponse response = consultarMapper.toResponse(dto);

        return ResponseEntity.ok(response);
    }
}
