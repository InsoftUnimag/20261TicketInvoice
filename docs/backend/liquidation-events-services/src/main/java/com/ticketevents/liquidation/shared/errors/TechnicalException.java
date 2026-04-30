package com.ticketevents.liquidation.shared.errors;

public class TechnicalException extends RuntimeException {
    private final ErrorCode errorCode;

    public TechnicalException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
