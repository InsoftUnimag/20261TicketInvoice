package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import com.ticketevents.liquidation.domain.entities.TipoComision;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RegistrarComisionRecintoRequest {

    @NotNull(message = "El tipo de comision es requerido")
    private TipoComision tipoComision;
    
    @NotNull(message = "El valor de comision es requerido")
    @PositiveOrZero(message = "El valor de comision debe ser cero o positivo")
    private java.math.BigDecimal valorComision;

    public RegistrarComisionRecintoRequest() {}

    public TipoComision getTipoComision() { return tipoComision; }
    public void setTipoComision(TipoComision tipoComision) { this.tipoComision = tipoComision; }
    public java.math.BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(java.math.BigDecimal valorComision) { this.valorComision = valorComision; }
}