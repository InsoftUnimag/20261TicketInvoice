package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Distribución del recaudo liquidada para un evento")
public class ConsultarDistribucionResponse {

    @Schema(description = "ID del evento", example = "1")
    private Long eventoId;

    @Schema(description = "Nombre del evento", example = "Concierto Rock 2026")
    private String nombreEvento;

    @Schema(description = "Total bruto recaudado", example = "62500.00")
    private BigDecimal totalBruto;

    @Schema(description = "Total pago al promotor", example = "48200.00")
    private BigDecimal totalPagoPromotor;

    @Schema(description = "Total comisión de plataforma", example = "6500.00")
    private BigDecimal totalComisionPlataforma;

    @Schema(description = "Total distribuible", example = "48200.00")
    private BigDecimal totalDistribuible;

    @Schema(description = "Estado de la liquidación", example = "LIQUIDADO", allowableValues = {"PRELIMINAR", "LIQUIDADO", "SIN_RECAUDO"})
    private String estado;

    @Schema(description = "Fecha del cálculo", example = "2026-05-07T10:00:00")
    private LocalDateTime fechaCalculo;

    @Schema(description = "Fecha de liquidación", example = "2026-05-07T12:00:00")
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
