package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import com.ticketevents.liquidation.shared.errors.ErrorCode;
import java.time.Instant;

public record ErrorResponse(
        ErrorCode code,
        String message,
        Instant timestamp
) {
}
