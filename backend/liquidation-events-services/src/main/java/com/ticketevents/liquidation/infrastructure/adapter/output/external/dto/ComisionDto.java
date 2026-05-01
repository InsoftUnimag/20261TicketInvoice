package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import com.ticketevents.liquidation.domain.entities.TipoComision;
import java.math.BigDecimal;

public record ComisionDto(
        TipoComision tipoComision,
        BigDecimal valorComision
) {
}
