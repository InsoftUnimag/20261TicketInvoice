package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionRecintoDto;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import com.ticketevents.liquidation.application.usecase.RegistrarComisionRecintoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.RegistrarComisionRecintoRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ComisionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "06. Comisión de Recinto", description = "Registra y consulta la comisión asociada a un recinto para el cálculo de liquidaciones")
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

    @Operation(summary = "Consultar comisión del recinto", description = """
            Obtiene la comisión configurada para un recinto.
            
            Retorna si está configurada, el tipo (PORCENTAJE o VALOR_FIJO) y el valor asociado.
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comisión consultada exitosamente",
            content = @Content(schema = @Schema(implementation = ComisionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "configurada": true,
                        "mensaje": "Comision configurada",
                        "tipoComision": "PORCENTAJE",
                        "valorComision": 0.12
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Recinto no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "RECINTO_NOT_FOUND", "message": "El recinto no se encuentra registrado", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @GetMapping("/{id}/comision")
    public ResponseEntity<ComisionResponse> consultarComision(
            @Parameter(description = "ID del recinto", example = "1", required = true)
            @PathVariable("id") Long recintoId) {
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

    @Operation(summary = "Registrar comisión del recinto (POST)", description = """
            Registra o actualiza la comisión asociada a un recinto.
            
            Tipos de comisión:
            - **PORCENTAJE**: Valor decimal (ej: 0.12 = 12%)
            - **VALOR_FIJO**: Monto fijo en la moneda local
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comisión registrada exitosamente",
            content = @Content(schema = @Schema(implementation = RegistrarComisionRecintoResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "recintoId": 1,
                        "tipoComision": "PORCENTAJE",
                        "valorComision": 0.12,
                        "fechaRegistro": "2026-05-07T10:00:00",
                        "mensaje": "Comision registrada exitosamente"
                    }"""))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (validación de campos)",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "INVALID_REQUEST", "message": "El valor de comision debe ser cero o positivo", "timestamp": "2026-05-07T10:00:00"}"""))),
        @ApiResponse(responseCode = "404", description = "Recinto no encontrado"),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @PostMapping("/{id}/comision")
    public ResponseEntity<RegistrarComisionRecintoResponse> registrarComision(
            @Parameter(description = "ID del recinto", example = "1", required = true)
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

    @Operation(summary = "Registrar comisión del recinto (GET)", description = "Método alternativo para navegador. Requiere recinto existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comisión registrada exitosamente",
            content = @Content(schema = @Schema(implementation = RegistrarComisionRecintoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Recinto no encontrado"),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @GetMapping("/{id}/comision/configurar")
    public ResponseEntity<RegistrarComisionRecintoResponse> registrarComisionDesdeNavegador(
            @Parameter(description = "ID del recinto", example = "1", required = true)
            @PathVariable("id") Long recintoId,
            @Parameter(description = "Tipo de comisión (PORCENTAJE o VALOR_FIJO)", example = "PORCENTAJE", required = true)
            @RequestParam("tipoComision") TipoComision tipoComision,
            @Parameter(description = "Valor de la comisión", example = "0.12", required = true)
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
