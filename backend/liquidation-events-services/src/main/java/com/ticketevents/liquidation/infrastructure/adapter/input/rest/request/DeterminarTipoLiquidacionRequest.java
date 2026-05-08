package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para configurar el tipo de liquidación de un evento")
public class DeterminarTipoLiquidacionRequest {

    @Schema(description = "Tipo de liquidación", example = "TARIFA_PLANA", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"TARIFA_PLANA", "REPARTO_INGRESOS"})
    @NotNull(message = "El tipo de liquidacion es requerido")
    private TipoLiquidacion tipoLiquidacion;

    @Schema(description = "Valor fijo de comisión (para TARIFA_PLANA)", example = "5000.00")
    private java.math.BigDecimal valorComision;

    @Schema(description = "Porcentaje de comisión (para REPARTO_INGRESOS)", example = "0.15")
    private java.math.BigDecimal porcentaje;

    public DeterminarTipoLiquidacionRequest() {}

    public TipoLiquidacion getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(TipoLiquidacion tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public java.math.BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(java.math.BigDecimal valorComision) { this.valorComision = valorComision; }
    public java.math.BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(java.math.BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}