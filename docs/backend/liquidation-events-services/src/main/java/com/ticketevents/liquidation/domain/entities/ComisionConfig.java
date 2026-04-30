package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;

public record ComisionConfig(
        boolean configurada,
        TipoComision tipoComision,
        BigDecimal valorComision
) {
}
