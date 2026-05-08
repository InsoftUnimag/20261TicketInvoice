package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Información de la comisión de un recinto")
public record ComisionResponse(
        @Schema(description = "Indica si la comisión está configurada", example = "true")
        boolean configurada,

        @Schema(description = "Mensaje informativo", example = "Comision configurada")
        String mensaje,

        @Schema(description = "Tipo de comisión", example = "PORCENTAJE", allowableValues = {"PORCENTAJE", "VALOR_FIJO"})
        String tipoComision,

        @Schema(description = "Valor de la comisión", example = "0.12")
        BigDecimal valorComision
) {
}
