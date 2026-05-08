package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ConfiguracionLiquidacionDto;
import com.ticketevents.liquidation.application.usecase.DeterminarTipoLiquidacionUseCase;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.DeterminarTipoLiquidacionRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import com.ticketevents.liquidation.infrastructure.mappers.ConfiguracionLiquidacionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "05. Configuración de Liquidación", description = "Configura y consulta el tipo de liquidación (Tarifa Plana o Reparto de Ingresos) para un evento")
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

    @Operation(summary = "Consultar configuración de liquidación", description = """
            Obtiene la configuración de liquidación registrada para un evento.
            
            Si el evento no tiene configuración registrada, retorna un mensaje indicativo.
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuración encontrada o mensaje informativo",
            content = @Content(schema = @Schema(implementation = DeterminarTipoLiquidacionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "eventoId": 1,
                        "tipoLiquidacion": "TARIFA_PLANA",
                        "valorComision": 5000.00,
                        "porcentaje": 0.00,
                        "mensaje": "Tipo de liquidacion configurado exitosamente"
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado",
            content = @Content(examples = @ExampleObject(value = """
                {"code": "EVENT_NOT_FOUND", "message": "El evento no se encuentra registrado", "timestamp": "2026-05-07T10:00:00"}""")))
    })
    @GetMapping("/{id}/configuracion-liquidacion")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> consultarConfiguracionLiquidacion(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de consulta de configuracion de liquidacion para evento: {}", eventoId);

        Optional<ConfiguracionLiquidacion> config = repository.findByEventoId(eventoId);
        if (config.isEmpty()) {
            DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
            response.setEventoId(eventoId);
            response.setMensaje("El evento no tiene configuracion de liquidacion registrada");
            return ResponseEntity.ok(response);
        }

        DeterminarTipoLiquidacionResponse response = mapper.toResponse(mapper.toDto(config.get()));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Configurar liquidación (GET)", description = """
            Configura el tipo de liquidación para un evento usando parámetros query.
            
            Método alternativo para navegador. Requiere evento existente y recinto con tipo asignado.
            
            Modelos de negocio:
            - **TARIFA_PLANA**: Monto fijo. Enviar `valorComision`.
            - **REPARTO_INGRESOS**: Porcentaje. Enviar `porcentaje`.
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Configuración creada exitosamente",
            content = @Content(schema = @Schema(implementation = DeterminarTipoLiquidacionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "eventoId": 1,
                        "tipoLiquidacion": "REPARTO_INGRESOS",
                        "valorComision": null,
                        "porcentaje": 0.15,
                        "mensaje": "Tipo de liquidacion configurado exitosamente"
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @GetMapping("/{id}/configuracion-liquidacion/configurar")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> configurarLiquidacionDesdeNavegador(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId,
            @Parameter(description = "Tipo de liquidación (TARIFA_PLANA o REPARTO_INGRESOS)", example = "REPARTO_INGRESOS", required = true)
            @RequestParam("tipoLiquidacion") TipoLiquidacion tipoLiquidacion,
            @Parameter(description = "Valor fijo de comisión (para TARIFA_PLANA)", example = "5000.00")
            @RequestParam(value = "valorComision", required = false) BigDecimal valorComision,
            @Parameter(description = "Porcentaje de comisión (para REPARTO_INGRESOS)", example = "0.15")
            @RequestParam(value = "porcentaje", required = false) BigDecimal porcentaje) {
        log.info("Solicitud GET de configuracion de liquidacion para evento: {}", eventoId);

        ConfiguracionLiquidacionDto dto = determinarTipoLiquidacionUseCase.execute(
                eventoId,
                tipoLiquidacion,
                valorComision,
                porcentaje
        );

        DeterminarTipoLiquidacionResponse response = mapper.toResponse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Configurar liquidación (POST)", description = """
            Configura el tipo de liquidación para un evento usando JSON body.
            
            Modelos de negocio:
            - **TARIFA_PLANA**: Enviar `valorComision` con el monto fijo
            - **REPARTO_INGRESOS**: Enviar `porcentaje` con la tasa
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Configuración creada exitosamente",
            content = @Content(schema = @Schema(implementation = DeterminarTipoLiquidacionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "eventoId": 1,
                        "tipoLiquidacion": "TARIFA_PLANA",
                        "valorComision": 5000.00,
                        "porcentaje": null,
                        "mensaje": "Tipo de liquidacion configurado exitosamente"
                    }"""))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (validación de campos)"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @PostMapping("/{id}/configuracion-liquidacion")
    public ResponseEntity<DeterminarTipoLiquidacionResponse> configurarLiquidacion(
            @Parameter(description = "ID del evento", example = "1", required = true)
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
