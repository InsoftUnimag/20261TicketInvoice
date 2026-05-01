package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;

public record ComisionResponse(
        boolean configurada,
        String mensaje,
        String tipoComision,
        BigDecimal valorComision
) {
}
