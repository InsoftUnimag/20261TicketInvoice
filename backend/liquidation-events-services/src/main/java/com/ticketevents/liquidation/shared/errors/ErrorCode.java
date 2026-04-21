package com.ticketevents.liquidation.shared.errors;

public enum ErrorCode {
    EVENT_NOT_FOUND("EVENT_NOT_FOUND", "El evento no se encuentra registrado"),
    EVENT_NOT_CLOSED("EVENT_NOT_CLOSED", "El evento aún no ha sido cerrado"),
    EXTERNAL_SERVICE_UNAVAILABLE("EXTERNAL_SERVICE_UNAVAILABLE", "No fue posible obtener la información de ventas"),
    INVALID_REQUEST("INVALID_REQUEST", "Solicitud inválida");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}