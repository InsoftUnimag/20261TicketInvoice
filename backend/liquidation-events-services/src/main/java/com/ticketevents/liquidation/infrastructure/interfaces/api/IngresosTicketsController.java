package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarIngresosTicketsUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.IngresosTicketsDto;
import com.ticketevents.liquidation.infrastructure.mappers.IngresosMapper;
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
@Tag(name = "04. Ingresos de Tickets", description = "Consulta los ingresos generados por la venta de tickets de un evento")
public class IngresosTicketsController {

    private static final Logger log = LoggerFactory.getLogger(IngresosTicketsController.class);

    private final ConsultarIngresosTicketsUseCase consultarIngresosTicketsUseCase;
    private final IngresosMapper mapper;

    public IngresosTicketsController(ConsultarIngresosTicketsUseCase consultarIngresosTicketsUseCase, IngresosMapper mapper) {
        this.consultarIngresosTicketsUseCase = consultarIngresosTicketsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Consultar ingresos de tickets", description = """
            Obtiene el resumen de ingresos generados por la venta de tickets de un evento.
            
            La respuesta incluye:
            - Total de tickets vendidos, validados, cancelados y cortesías
            - Recaudo bruto generado
            - Desglose de tickets por estado financiero
            - Indicador de inconsistencias si hay tickets sin estado definido
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ingresos obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = ConsultarIngresosResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "eventoId": 1,
                        "totalTicketsVendidos": 145,
                        "totalTicketsValidados": 100,
                        "totalTicketsCancelados": 5,
                        "totalCortesias": 10,
                        "totalNoAsistieron": 30,
                        "totalRecaudoBruto": 62500.00,
                        "ticketsPorEstado": {
                            "VALIDADO": 100,
                            "VENDIDO": 30,
                            "CANCELADO": 5,
                            "CORTESIA": 10
                        },
                        "hasInconsistencies": false
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EVENT_NOT_FOUND", "message": "El evento no se encuentra registrado", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EXTERNAL_SERVICE_UNAVAILABLE", "message": "No fue posible obtener la información de ventas", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}/ingresos")
    public ResponseEntity<ConsultarIngresosResponse> consultarIngresos(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de ingresos de tickets para evento: {}", eventoId);
        IngresosTicketsDto dto = consultarIngresosTicketsUseCase.execute(eventoId);
        ConsultarIngresosResponse response = mapper.toResponse(dto);
        return ResponseEntity.ok(response);
    }
}