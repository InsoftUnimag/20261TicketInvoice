package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ConsultarEstadoIngresoRequest {
    
    @NotNull(message = "El ID del evento es requerido")
    @Positive(message = "El ID del evento debe ser un valor positivo")
    private Long eventoId;

    public ConsultarEstadoIngresoRequest() {}

    public ConsultarEstadoIngresoRequest(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
}