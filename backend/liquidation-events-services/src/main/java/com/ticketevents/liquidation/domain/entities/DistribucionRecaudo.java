package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DistribucionRecaudo {

    private Long id;
    private Long eventoId;
    private String nombreEvento;
    private BigDecimal totalBruto;
    private BigDecimal totalNetoPreliminar;
    private BigDecimal totalDistribuible;
    private BigDecimal comisionPlataforma;
    private BigDecimal descuentoCancelados;
    private BigDecimal descuentoCortesia;
    private String estado;
    private LocalDateTime fechaCalculo;

    public DistribucionRecaudo() {}

    public DistribucionRecaudo(Long eventoId, String nombreEvento, BigDecimal totalBruto) {
        if (eventoId == null || eventoId <= 0) {
            throw new IllegalArgumentException("El ID del evento es requerido y debe ser positivo");
        }
        if (nombreEvento == null || nombreEvento.isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es requerido");
        }
        if (totalBruto == null || totalBruto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total bruto no puede ser negativo");
        }
        this.eventoId = eventoId;
        this.nombreEvento = nombreEvento;
        this.totalBruto = totalBruto;
        this.fechaCalculo = LocalDateTime.now();
        this.estado = "PRELIMINAR";
    }

    public void calcularNetoPreliminar(BigDecimal comisionPlataforma, BigDecimal descuentos) {
        if (comisionPlataforma == null) {
            comisionPlataforma = BigDecimal.ZERO;
        }
        if (descuentos == null) {
            descuentos = BigDecimal.ZERO;
        }
        this.comisionPlataforma = comisionPlataforma;
        this.totalNetoPreliminar = this.totalBruto.subtract(comisionPlataforma).subtract(descuentos);
    }

    public void calcularTotalDistribuible(BigDecimal comisionesAcordadas) {
        if (comisionesAcordadas == null) {
            comisionesAcordadas = BigDecimal.ZERO;
        }
        if (this.totalNetoPreliminar == null) {
            throw new IllegalStateException("Debe calcular primero el neto preliminar");
        }
        this.totalDistribuible = this.totalNetoPreliminar.subtract(comisionesAcordadas);
    }

    public void marcarSinRecaudo() {
        this.estado = "SIN_RECAUDO";
        this.totalBruto = BigDecimal.ZERO;
        this.totalNetoPreliminar = BigDecimal.ZERO;
        this.totalDistribuible = BigDecimal.ZERO;
    }

    public void validar() {
        if (eventoId == null) {
            throw new IllegalArgumentException("El ID del evento es requerido");
        }
        if (totalBruto == null) {
            throw new IllegalArgumentException("El total bruto es requerido");
        }
        if (fechaCalculo == null) {
            throw new IllegalArgumentException("La fecha de calculo es requerida");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public BigDecimal getTotalBruto() { return totalBruto; }
    public void setTotalBruto(BigDecimal totalBruto) { this.totalBruto = totalBruto; }
    public BigDecimal getTotalNetoPreliminar() { return totalNetoPreliminar; }
    public void setTotalNetoPreliminar(BigDecimal totalNetoPreliminar) { this.totalNetoPreliminar = totalNetoPreliminar; }
    public BigDecimal getTotalDistribuible() { return totalDistribuible; }
    public void setTotalDistribuible(BigDecimal totalDistribuible) { this.totalDistribuible = totalDistribuible; }
    public BigDecimal getComisionPlataforma() { return comisionPlataforma; }
    public void setComisionPlataforma(BigDecimal comisionPlataforma) { this.comisionPlataforma = comisionPlataforma; }
    public BigDecimal getDescuentoCancelados() { return descuentoCancelados; }
    public void setDescuentoCancelados(BigDecimal descuentoCancelados) { this.descuentoCancelados = descuentoCancelados; }
    public BigDecimal getDescuentoCortesia() { return descuentoCortesia; }
    public void setDescuentoCortesia(BigDecimal descuentoCortesia) { this.descuentoCortesia = descuentoCortesia; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
}
