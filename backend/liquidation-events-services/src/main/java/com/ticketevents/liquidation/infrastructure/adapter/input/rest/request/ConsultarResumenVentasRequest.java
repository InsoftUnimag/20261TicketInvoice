package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ConsultarResumenVentasRequest {

    @NotNull(message = "El ID del evento es requerido")
    @Positive(message = "El ID del evento debe ser positivo")
    private Long eventoId;

    public ConsultarResumenVentasRequest() {}

    public ConsultarResumenVentasRequest(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
}