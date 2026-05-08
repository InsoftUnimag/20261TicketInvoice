package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.CalcularDistribucionRecaudoUseCase;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.CalcularDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import com.ticketevents.liquidation.infrastructure.mappers.DistribucionRecaudoMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "08. Calcular Distribución del Recaudo", description = "Calcula la distribución preliminar del recaudo de un evento finalizado")
public class CalcularDistribucionController {

    private static final Logger log = LoggerFactory.getLogger(CalcularDistribucionController.class);

    private final CalcularDistribucionRecaudoUseCase calcularDistribucionUseCase;
    private final DistribucionRecaudoMapper distribucionMapper;

    public CalcularDistribucionController(CalcularDistribucionRecaudoUseCase calcularDistribucionUseCase,
                                           DistribucionRecaudoMapper distribucionMapper) {
        this.calcularDistribucionUseCase = calcularDistribucionUseCase;
        this.distribucionMapper = distribucionMapper;
    }

    @Operation(summary = "Calcular distribución del recaudo (POST)", description = """
            Calcula la distribución preliminar del recaudo de un evento finalizado.
            
            El cálculo toma en cuenta:
            1. Resumen de ventas del evento (Módulo 1)
            2. Tipo de recinto y su tasa de comisión
            3. Configuración de liquidación (Tarifa Plana o Reparto de Ingresos)
            4. Descuentos por tickets cancelados y cortesías
            
            Resultados:
            - Total bruto recaudado
            - Valor neto preliminar
            - Total distribuible
            - Estado PRELIMINAR o SIN_RECAUDO
            """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Distribución calculada exitosamente",
            content = @Content(schema = @Schema(implementation = CalcularDistribucionResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "eventoId": 1,
                        "nombreEvento": "Concierto Rock 2026",
                        "totalBruto": 62500.00,
                        "totalNetoPreliminar": 56000.00,
                        "totalDistribuible": 48200.00,
                        "comisionPlataforma": 6500.00,
                        "descuentoCancelados": 2500.00,
                        "descuentoCortesia": 0.00,
                        "estado": "PRELIMINAR",
                        "fechaCalculo": "2026-05-07T10:00:00",
                        "mensaje": "Distribucion del recaudo calculada exitosamente"
                    }"""))),
        @ApiResponse(responseCode = "404", description = "Evento o recinto no encontrado"),
        @ApiResponse(responseCode = "409", description = "Evento no cerrado"),
        @ApiResponse(responseCode = "502", description = "Servicio externo no disponible")
    })
    @PostMapping("/{id}/calcular-distribucion")
    public ResponseEntity<CalcularDistribucionResponse> calcularDistribucion(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud de calculo de distribucion del recaudo para evento: {}", eventoId);

        DistribucionRecaudoDto dto = calcularDistribucionUseCase.execute(eventoId);

        CalcularDistribucionResponse response = distribucionMapper.toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Calcular distribución del recaudo (GET)", description = "Método alternativo para navegador. Misma funcionalidad que POST.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Distribución calculada exitosamente",
            content = @Content(schema = @Schema(implementation = CalcularDistribucionResponse.class)))
    })
    @GetMapping("/{id}/calcular-distribucion")
    public ResponseEntity<CalcularDistribucionResponse> calcularDistribucionDesdeNavegador(
            @Parameter(description = "ID del evento", example = "1", required = true)
            @PathVariable("id") Long eventoId) {
        log.info("Solicitud GET de calculo de distribucion del recaudo para evento: {}", eventoId);

        DistribucionRecaudoDto dto = calcularDistribucionUseCase.execute(eventoId);
        CalcularDistribucionResponse response = distribucionMapper.toResponse(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
