package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.ConsultarDistribucionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "09. Consultar Distribución del Recaudo", description = "Consulta la distribución del recaudo liquidada para un evento")
public class ConsultarDistribucionController {

    private static final Logger log = LoggerFactory.getLogger(ConsultarDistribucionController.class);

    private final ConsultarDistribucionRecaudoUseCase consultarDistribucionUseCase;
    private final ConsultarDistribucionMapper consultarMapper;

    public ConsultarDistribucionController(ConsultarDistribucionRecaudoUseCase consultarDistribucionUseCase,
                                              ConsultarDistribucionMapper consultarMapper) {
        this.consultarDistribucionUseCase = consultarDistribucionUseCase;
        this.consultarMapper = consultarMapper;
    }

    @Operation(summary = "Consultar distribución del recaudo", description = """
            Obtiene la distribución del recaudo liquidada para un evento.
            
            Solo disponible para eventos en estado LIQUIDADO.
            Muestra el detalle de montos distribuidos al promotor y comisión de plataforma.
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Distribución encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = ConsultarDistribucionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "eventoId": 1,
                        "nombreEvento": "Concierto Rock 2026",
                        "totalBruto": 62500.00,
                        "totalPagoPromotor": 48200.00,
                        "totalComisionPlataforma": 6500.00,
                        "totalDistribuible": 48200.00,
                        "estado": "LIQUIDADO",
                        "fechaCalculo": "2026-05-07T10:00:00",
                        "fechaLiquidacion": "2026-05-07T12:00:00"
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Distribución no encontrada o evento no existe",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "DISTRIBUTION_NOT_FOUND", "message": "No se encontro la distribucion del recaudo para el evento", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "409", description = "Evento no liquidado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "DISTRIBUTION_NOT_LIQUIDATED", "message": "El evento aun no tiene liquidacion disponible", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}/distribucion-recaudo")
    public ResponseEntity<ConsultarDistribucionResponse> consultarDistribucion(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de consulta de distribucion del recaudo para evento: {}", eventoId);

        DistribucionRecaudoDto dto = consultarDistribucionUseCase.execute(eventoId);

        ConsultarDistribucionResponse response = consultarMapper.toResponse(dto);

        return ResponseEntity.ok(response);
    }
}
