package com.liquidacion.resumenVentas.application.dto;

public class ConsultarResumenVentasRequest {
    private Long eventoId;

    public ConsultarResumenVentasRequest() {}

    public ConsultarResumenVentasRequest(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
}