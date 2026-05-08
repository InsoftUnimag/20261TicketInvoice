package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resultado del cálculo de distribución del recaudo de un evento")
public class CalcularDistribucionResponse {

    @Schema(description = "ID del evento", example = "1")
    private Long eventoId;

    @Schema(description = "Nombre del evento", example = "Concierto Rock 2026")
    private String nombreEvento;

    @Schema(description = "Total bruto recaudado", example = "62500.00")
    private BigDecimal totalBruto;

    @Schema(description = "Valor neto preliminar (bruto - cancelados)", example = "56000.00")
    private BigDecimal totalNetoPreliminar;

    @Schema(description = "Total distribuible después de comisiones", example = "48200.00")
    private BigDecimal totalDistribuible;

    @Schema(description = "Comisión de la plataforma", example = "6500.00")
    private BigDecimal comisionPlataforma;

    @Schema(description = "Descuento por tickets cancelados", example = "2500.00")
    private BigDecimal descuentoCancelados;

    @Schema(description = "Descuento por cortesías", example = "0.00")
    private BigDecimal descuentoCortesia;

    @Schema(description = "Estado de la liquidación", example = "PRELIMINAR", allowableValues = {"PRELIMINAR", "SIN_RECAUDO"})
    private String estado;

    @Schema(description = "Fecha del cálculo", example = "2026-05-07T10:00:00")
    private LocalDateTime fechaCalculo;

    @Schema(description = "Mensaje informativo", example = "Distribucion del recaudo calculada exitosamente")
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
