package com.ticketevents.liquidation.infrastructure.adapter.input.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ConsultarRecintoRequest {
    
    @NotNull(message = "El ID del recinto es requerido")
    @Positive(message = "El ID del recinto debe ser un valor positivo")
    private Long recintoId;

    public ConsultarRecintoRequest() {}

    public ConsultarRecintoRequest(Long recintoId) {
        this.recintoId = recintoId;
    }

    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }
}