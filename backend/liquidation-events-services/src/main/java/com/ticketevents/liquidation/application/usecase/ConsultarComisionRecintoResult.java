package com.ticketevents.liquidation.application.usecase;

import java.math.BigDecimal;

public record ConsultarComisionRecintoResult(
        boolean configurada,
        String mensaje,
        String tipoComision,
        BigDecimal valorComision
) {
}
