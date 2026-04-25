package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistrarComisionRecintoResponse {

    private Long id;
    private Long recintoId;
    private String tipoComision;
    private BigDecimal valorComision;
    private LocalDateTime fechaRegistro;
    private String mensaje;

    public RegistrarComisionRecintoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }
    public String getTipoComision() { return tipoComision; }
    public void setTipoComision(String tipoComision) { this.tipoComision = tipoComision; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}