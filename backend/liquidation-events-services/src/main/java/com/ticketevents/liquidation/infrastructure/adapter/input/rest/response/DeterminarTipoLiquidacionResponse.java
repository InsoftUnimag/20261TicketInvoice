package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;

public class DeterminarTipoLiquidacionResponse {

    private Long id;
    private Long eventoId;
    private String tipoLiquidacion;
    private BigDecimal valorComision;
    private BigDecimal porcentaje;
    private String mensaje;

    public DeterminarTipoLiquidacionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(String tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}