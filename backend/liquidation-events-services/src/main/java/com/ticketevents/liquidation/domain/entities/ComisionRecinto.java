package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComisionRecinto {
    private Long id;
    private Long recintoId;
    private TipoComision tipoComision;
    private BigDecimal valorComision;
    private LocalDateTime fechaRegistro;

    public ComisionRecinto() {}

    public ComisionRecinto(Long id, Long recintoId, TipoComision tipoComision, BigDecimal valorComision) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID es requerido");
        }
        if (recintoId == null || recintoId <= 0) {
            throw new IllegalArgumentException("El ID del recinto es requerido");
        }
        if (tipoComision == null) {
            throw new IllegalArgumentException("El tipo de comision es requerido");
        }
        if (valorComision == null) {
            throw new IllegalArgumentException("El valor de comision es requerido");
        }
        if (valorComision.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor de comision no puede ser negativo");
        }
        this.id = id;
        this.recintoId = recintoId;
        this.tipoComision = tipoComision;
        this.valorComision = valorComision;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }
    public TipoComision getTipoComision() { return tipoComision; }
    public void setTipoComision(TipoComision tipoComision) { this.tipoComision = tipoComision; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public void validar() {
        if (recintoId == null) {
            throw new IllegalArgumentException("El ID del recinto es requerido");
        }
        if (tipoComision == null) {
            throw new IllegalArgumentException("El tipo de comision es requerido");
        }
        if (valorComision == null || valorComision.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor de comision debe ser un numero valido positivo");
        }
    }
}