package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.RecintoDto;
import com.ticketevents.liquidation.application.usecase.ConsultarRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.RecintoMapper;
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
@RequestMapping("/api/v1/recintos")
@Tag(name = "03. Recintos", description = "Consulta información de recintos registrados en el sistema")
public class RecintoController {

    private static final Logger log = LoggerFactory.getLogger(RecintoController.class);

    private final ConsultarRecintoUseCase consultarRecintoUseCase;
    private final RecintoMapper mapper;

    public RecintoController(ConsultarRecintoUseCase consultarRecintoUseCase,
                            RecintoMapper mapper) {
        this.consultarRecintoUseCase = consultarRecintoUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Consultar recinto por ID", description = """
            Obtiene la información de un recinto registrado en el sistema.
            
            El recinto contiene:
            - Tipo de recinto (ESTADIO / TEATRO)
            - Tasa de comisión asociada
            - Estado actual (ACTIVO / INACTIVO)
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recinto encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = ConsultarRecintoResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Estadio Nacional",
                        "tipoRecinto": "ESTADIO",
                        "tasaComision": 0.15,
                        "estado": "ACTIVO"
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Recinto no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "RECINTO_NOT_FOUND", "message": "El recinto no se encuentra registrado", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EXTERNAL_SERVICE_UNAVAILABLE", "message": "No fue posible obtener la información de ventas", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsultarRecintoResponse> consultarRecinto(
            @Parameter(description = "ID del recinto", example = "1", required = true)
            @PathVariable("id") Long id) {
        log.info("Solicitud de recinto: {}", id);

        RecintoDto dto = consultarRecintoUseCase.execute(id);
        ConsultarRecintoResponse response = mapper.toResponse(dto);

        return ResponseEntity.ok(response);
    }
}