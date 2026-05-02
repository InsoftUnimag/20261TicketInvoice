package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultarDistribucionResponse {

    private Long eventoId;
    private String nombreEvento;
    private BigDecimal totalBruto;
    private BigDecimal totalPagoPromotor;
    private BigDecimal totalComisionPlataforma;
    private BigDecimal totalDistribuible;
    private String estado;
    private LocalDateTime fechaCalculo;
    private LocalDateTime fechaLiquidacion;

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public BigDecimal getTotalBruto() { return totalBruto; }
    public void setTotalBruto(BigDecimal totalBruto) { this.totalBruto = totalBruto; }
    public BigDecimal getTotalPagoPromotor() { return totalPagoPromotor; }
    public void setTotalPagoPromotor(BigDecimal totalPagoPromotor) { this.totalPagoPromotor = totalPagoPromotor; }
    public BigDecimal getTotalComisionPlataforma() { return totalComisionPlataforma; }
    public void setTotalComisionPlataforma(BigDecimal totalComisionPlataforma) { this.totalComisionPlataforma = totalComisionPlataforma; }
    public BigDecimal getTotalDistribuible() { return totalDistribuible; }
    public void setTotalDistribuible(BigDecimal totalDistribuible) { this.totalDistribuible = totalDistribuible; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
    public LocalDateTime getFechaLiquidacion() { return fechaLiquidacion; }
    public void setFechaLiquidacion(LocalDateTime fechaLiquidacion) { this.fechaLiquidacion = fechaLiquidacion; }
}
