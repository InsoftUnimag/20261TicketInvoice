package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.EstadoIngresoDto;
import com.ticketevents.liquidation.application.usecase.ConsultarEstadoIngresoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarEstadoIngresoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.EstadoIngresoMapper;
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
@Tag(name = "02. Estado de Ingreso", description = "Consulta el estado de ingreso de tickets desde el módulo de operación de eventos y control de accesos")
public class EstadoIngresoController {

    private static final Logger log = LoggerFactory.getLogger(EstadoIngresoController.class);

    private final ConsultarEstadoIngresoUseCase consultarEstadoIngresoUseCase;
    private final EstadoIngresoMapper mapper;

    public EstadoIngresoController(ConsultarEstadoIngresoUseCase consultarEstadoIngresoUseCase,
                                   EstadoIngresoMapper mapper) {
        this.consultarEstadoIngresoUseCase = consultarEstadoIngresoUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Consultar estado de ingreso", description = """
            Obtiene el estado de ingreso de todos los tickets vendidos para un evento.
            
            Determina qué tickets fueron utilizados (CHECKED_IN) y cuáles no (NOT_ATTENDED).
            Esta información es necesaria para la matriz de liquidación.
            
            Requisitos:
            - El evento debe existir en el módulo de control de accesos
            - El servicio externo del Módulo 2 debe estar disponible
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de ingreso obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ConsultarEstadoIngresoResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "eventoId": 1,
                        "nombreEvento": "Concierto Rock 2026",
                        "tickets": [
                            {"idTicket": 1, "codigoTicket": "TKT-1", "estadoIngreso": "CHECKED_IN", "tipoAcceso": "INGRESO"},
                            {"idTicket": 2, "codigoTicket": "TKT-2", "estadoIngreso": "NOT_ATTENDED", "tipoAcceso": null}
                        ],
                        "totalTickets": 130,
                        "totalCheckeados": 100,
                        "totalNoAsistieron": 30
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EVENT_NOT_FOUND", "message": "No se encontraron registros de ingreso para el evento", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EXTERNAL_SERVICE_UNAVAILABLE", "message": "No fue posible obtener la información de ventas", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}/estado-ingreso")
    public ResponseEntity<ConsultarEstadoIngresoResponse> consultarEstadoIngreso(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de estado de ingreso para evento: {}", eventoId);

        EstadoIngresoDto output = consultarEstadoIngresoUseCase.execute(eventoId);
        ConsultarEstadoIngresoResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }
}