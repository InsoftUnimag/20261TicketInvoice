package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import jakarta.validation.constraints.NotNull;

public class DeterminarTipoLiquidacionRequest {

    @NotNull(message = "El tipo de liquidacion es requerido")
    private TipoLiquidacion tipoLiquidacion;
    
    private java.math.BigDecimal valorComision;
    private java.math.BigDecimal porcentaje;

    public DeterminarTipoLiquidacionRequest() {}

    public TipoLiquidacion getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(TipoLiquidacion tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public java.math.BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(java.math.BigDecimal valorComision) { this.valorComision = valorComision; }
    public java.math.BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(java.math.BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}