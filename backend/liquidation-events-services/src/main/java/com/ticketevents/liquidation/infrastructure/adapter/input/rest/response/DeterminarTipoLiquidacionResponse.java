package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Configuración de liquidación de un evento")
public class DeterminarTipoLiquidacionResponse {

    @Schema(description = "ID de la configuración", example = "1")
    private Long id;

    @Schema(description = "ID del evento", example = "1")
    private Long eventoId;

    @Schema(description = "Tipo de liquidación", example = "TARIFA_PLANA", allowableValues = {"TARIFA_PLANA", "REPARTO_INGRESOS"})
    private String tipoLiquidacion;

    @Schema(description = "Valor fijo de comisión (para tarifa plana)", example = "5000.00")
    private BigDecimal valorComision;

    @Schema(description = "Porcentaje de comisión (para reparto de ingresos)", example = "0.15")
    private BigDecimal porcentaje;

    @Schema(description = "Mensaje informativo", example = "Tipo de liquidacion configurado exitosamente")
    private String mensaje;

    public DeterminarTipoLiquidacionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(String tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}