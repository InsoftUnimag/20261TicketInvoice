package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import java.math.BigDecimal;

public class ConfiguracionLiquidacionDto {
    private Long idConfiguracion;
    private Long eventoId;
    private String tipoLiquidacion;
    private BigDecimal valorComision;
    private BigDecimal porcentaje;

    public ConfiguracionLiquidacionDto() {}

    public ConfiguracionLiquidacionDto(Long idConfiguracion, Long eventoId, String tipoLiquidacion,
                                        BigDecimal valorComision, BigDecimal porcentaje) {
        this.idConfiguracion = idConfiguracion;
        this.eventoId = eventoId;
        this.tipoLiquidacion = tipoLiquidacion;
        this.valorComision = valorComision;
        this.porcentaje = porcentaje;
    }

    public Long getIdConfiguracion() { return idConfiguracion; }
    public void setIdConfiguracion(Long idConfiguracion) { this.idConfiguracion = idConfiguracion; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(String tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}