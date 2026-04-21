package com.ticketevents.liquidation.domain.entities;

import java.time.LocalDateTime;

public class Evento {
    private Long id;
    private String nombre;
    private LocalDateTime fecha;
    private EstadoEvento estado;

    public Evento() {}

    public Evento(Long id, String nombre, LocalDateTime fecha, EstadoEvento estado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del evento debe ser positivo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es requerido");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado del evento es requerido");
        }
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public EstadoEvento getEstado() { return estado; }
    public void setEstado(EstadoEvento estado) { this.estado = estado; }

    public boolean isCerrado() {
        return estado == EstadoEvento.CERRADO;
    }
}