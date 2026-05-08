package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.EventSnapshotDto;
import com.ticketevents.liquidation.application.usecase.ConsultarResumenVentasUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ResumenVentasMapper;
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
@Tag(name = "01. Resumen de Ventas", description = "Consulta el resumen de ventas de un evento desde el módulo de gestión de recintos e inventario de aforo")
public class ResumenVentasController {

    private static final Logger log = LoggerFactory.getLogger(ResumenVentasController.class);

    private final ConsultarResumenVentasUseCase consultarResumenVentasUseCase;
    private final ResumenVentasMapper mapper;

    public ResumenVentasController(ConsultarResumenVentasUseCase consultarResumenVentasUseCase,
                                   ResumenVentasMapper mapper) {
        this.consultarResumenVentasUseCase = consultarResumenVentasUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Consultar resumen de ventas", description = """
            Obtiene el resumen consolidado de tickets vendidos para un evento.
            
            Requisitos:
            - El evento debe existir en el sistema de gestión de recintos
            - El evento debe estar en estado CERRADO
            - El servicio externo del Módulo 1 debe estar disponible
            
            La respuesta incluye:
            - Total de tickets vendidos
            - Total recaudo bruto
            - Desglose de tickets por condición de liquidación (VALIDADO, VENDIDO, CANCELADO, CORTESIA)
            - Desglose de recaudo por condición de liquidación
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resumen de ventas obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ConsultarResumenVentasResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "eventoId": 1,
                        "nombreEvento": "Concierto Rock 2026",
                        "estadoEvento": "CERRADO",
                        "totalTicketsVendidos": 145,
                        "totalRecaudoBruto": 62500.00,
                        "ticketsPorCondicion": {
                            "VALIDADO": 100,
                            "VENDIDO": 30,
                            "CANCELADO": 5,
                            "CORTESIA": 10
                        },
                        "recaudoPorCondicion": {
                            "VALIDADO": 50000.00,
                            "VENDIDO": 15000.00,
                            "CANCELADO": -2500.00,
                            "CORTESIA": 0.00
                        }
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EVENT_NOT_FOUND", "message": "El evento no se encuentra registrado", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "409", description = "Evento no está cerrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EVENT_NOT_CLOSED", "message": "El evento aún no ha sido cerrado. Estado actual: EN_CURSO", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EXTERNAL_SERVICE_UNAVAILABLE", "message": "No fue posible obtener la información de ventas", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}/resumen-ventas")
    public ResponseEntity<ConsultarResumenVentasResponse> consultarResumenVentas(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de resumen de ventas para evento: {}", eventoId);
        EventSnapshotDto dto = consultarResumenVentasUseCase.execute(eventoId);
        ConsultarResumenVentasResponse response = mapper.toResponse(dto);
        return ResponseEntity.ok(response);
    }
}