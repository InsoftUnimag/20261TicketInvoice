package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private String codigo;
    private Long eventoId;
    private CondicionLiquidacion condicion;
    private BigDecimal valorBruto;
    private LocalDateTime fechaVenta;

    public Ticket() {}

    public Ticket(Long id, String codigo, Long eventoId, CondicionLiquidacion condicion, 
                 BigDecimal valorBruto, LocalDateTime fechaVenta) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del ticket debe ser positivo");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo del ticket es requerido");
        }
        if (eventoId == null || eventoId <= 0) {
            throw new IllegalArgumentException("El ID del evento debe ser positivo");
        }
        if (valorBruto == null) {
            throw new IllegalArgumentException("El valor bruto es requerido");
        }
        if (valorBruto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor bruto no puede ser negativo");
        }
        this.id = id;
        this.codigo = codigo;
        this.eventoId = eventoId;
        this.condicion = condicion;
        this.valorBruto = valorBruto;
        this.fechaVenta = fechaVenta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public CondicionLiquidacion getCondicion() { return condicion; }
    public void setCondicion(CondicionLiquidacion condicion) { this.condicion = condicion; }
    public BigDecimal getValorBruto() { return valorBruto; }
    public void setValorBruto(BigDecimal valorBruto) { this.valorBruto = valorBruto; }
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }
}