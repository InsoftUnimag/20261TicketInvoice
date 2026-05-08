package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import com.ticketevents.liquidation.domain.entities.TipoComision;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Solicitud para registrar la comisión de un recinto")
public class RegistrarComisionRecintoRequest {

    @Schema(description = "Tipo de comisión", example = "PORCENTAJE", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PORCENTAJE", "VALOR_FIJO"})
    @NotNull(message = "El tipo de comision es requerido")
    private TipoComision tipoComision;

    @Schema(description = "Valor de la comisión", example = "0.12", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El valor de comision es requerido")
    @PositiveOrZero(message = "El valor de comision debe ser cero o positivo")
    private java.math.BigDecimal valorComision;

    public RegistrarComisionRecintoRequest() {}

    public TipoComision getTipoComision() { return tipoComision; }
    public void setTipoComision(TipoComision tipoComision) { this.tipoComision = tipoComision; }
    public java.math.BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(java.math.BigDecimal valorComision) { this.valorComision = valorComision; }
}