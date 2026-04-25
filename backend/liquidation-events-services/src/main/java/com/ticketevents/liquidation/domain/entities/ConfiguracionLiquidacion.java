package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;

public class ConfiguracionLiquidacion {
    private Long id;
    private Long eventoId;
    private TipoLiquidacion tipoLiquidacion;
    private BigDecimal valorComision;
    private BigDecimal porcentaje;

    public ConfiguracionLiquidacion() {}

    public ConfiguracionLiquidacion(Long id, Long eventoId, TipoLiquidacion tipoLiquidacion, 
                                    BigDecimal valorComision, BigDecimal porcentaje) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID es requerido");
        }
        if (eventoId == null || eventoId <= 0) {
            throw new IllegalArgumentException("El ID del evento es requerido");
        }
        if (tipoLiquidacion == null) {
            throw new IllegalArgumentException("El tipo de liquidación es requerido");
        }
        this.id = id;
        this.eventoId = eventoId;
        this.tipoLiquidacion = tipoLiquidacion;
        this.valorComision = valorComision;
        this.porcentaje = porcentaje;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public TipoLiquidacion getTipoLiquidacion() { return tipoLiquidacion; }
    public void setTipoLiquidacion(TipoLiquidacion tipoLiquidacion) { this.tipoLiquidacion = tipoLiquidacion; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

    public void validar() {
        if (eventoId == null) {
            throw new IllegalArgumentException("El ID del evento es requerido");
        }
        if (tipoLiquidacion == null) {
            throw new IllegalArgumentException("El tipo de liquidación es requerido");
        }
    }
}