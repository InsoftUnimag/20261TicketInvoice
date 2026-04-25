package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComisionRecintoDto {
    private Long idComision;
    private Long recintoId;
    private String tipoComision;
    private BigDecimal valorComision;
    private LocalDateTime fechaRegistro;

    public ComisionRecintoDto() {}

    public ComisionRecintoDto(Long idComision, Long recintoId, String tipoComision,
                               BigDecimal valorComision, LocalDateTime fechaRegistro) {
        this.idComision = idComision;
        this.recintoId = recintoId;
        this.tipoComision = tipoComision;
        this.valorComision = valorComision;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdComision() { return idComision; }
    public void setIdComision(Long idComision) { this.idComision = idComision; }
    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }
    public String getTipoComision() { return tipoComision; }
    public void setTipoComision(String tipoComision) { this.tipoComision = tipoComision; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}