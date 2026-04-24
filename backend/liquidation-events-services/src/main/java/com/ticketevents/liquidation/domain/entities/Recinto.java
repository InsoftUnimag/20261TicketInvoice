package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Recinto {
    private Long id;
    private String nombre;
    private TipoRecinto tipoRecinto;
    private BigDecimal tasaComision;
    private String estado;

    public Recinto() {}

    public Recinto(Long id, String nombre, TipoRecinto tipoRecinto, BigDecimal tasaComision) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del recinto debe ser positivo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del recinto es requerido");
        }
        if (tipoRecinto == null) {
            throw new IllegalArgumentException("El tipo de recinto es requerido");
        }
        if (tasaComision == null) {
            throw new IllegalArgumentException("La tasa de comisión es requerida");
        }
        if (tasaComision.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La tasa de comisión no puede ser negativa");
        }
        if (tasaComision.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("La tasa de comisión no puede ser mayor al 100%");
        }
        this.id = id;
        this.nombre = nombre;
        this.tipoRecinto = tipoRecinto;
        this.tasaComision = tasaComision;
        this.estado = "ACTIVO";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoRecinto getTipoRecinto() { return tipoRecinto; }
    public void setTipoRecinto(TipoRecinto tipoRecinto) { this.tipoRecinto = tipoRecinto; }
    public BigDecimal getTasaComision() { return tasaComision; }
    public void setTasaComision(BigDecimal tasaComision) { this.tasaComision = tasaComision; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}