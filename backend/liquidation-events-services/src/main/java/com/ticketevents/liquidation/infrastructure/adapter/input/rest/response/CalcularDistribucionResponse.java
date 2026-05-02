package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CalcularDistribucionResponse {

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
    private String mensaje;

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
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
