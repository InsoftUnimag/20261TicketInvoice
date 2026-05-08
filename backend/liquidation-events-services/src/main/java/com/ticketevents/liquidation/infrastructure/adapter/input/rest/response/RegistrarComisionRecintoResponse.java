package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta del registro de comisión de un recinto")
public class RegistrarComisionRecintoResponse {

    @Schema(description = "ID de la comisión", example = "1")
    private Long id;

    @Schema(description = "ID del recinto", example = "1")
    private Long recintoId;

    @Schema(description = "Tipo de comisión", example = "PORCENTAJE", allowableValues = {"PORCENTAJE", "VALOR_FIJO"})
    private String tipoComision;

    @Schema(description = "Valor de la comisión", example = "0.12")
    private BigDecimal valorComision;

    @Schema(description = "Fecha de registro", example = "2026-05-07T10:00:00")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Mensaje informativo", example = "Comision registrada exitosamente")
    private String mensaje;

    public RegistrarComisionRecintoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }
    public String getTipoComision() { return tipoComision; }
    public void setTipoComision(String tipoComision) { this.tipoComision = tipoComision; }
    public BigDecimal getValorComision() { return valorComision; }
    public void setValorComision(BigDecimal valorComision) { this.valorComision = valorComision; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}