package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Información de un recinto")
public class ConsultarRecintoResponse {

    @Schema(description = "ID del recinto", example = "1")
    private Long id;

    @Schema(description = "Nombre del recinto", example = "Estadio Nacional")
    private String nombre;

    @Schema(description = "Tipo de recinto", example = "ESTADIO", allowableValues = {"ESTADIO", "TEATRO"})
    private String tipoRecinto;

    @Schema(description = "Tasa de comisión asociada al tipo de recinto", example = "0.15")
    private BigDecimal tasaComision;

    @Schema(description = "Estado del recinto", example = "ACTIVO", allowableValues = {"ACTIVO", "INACTIVO"})
    private String estado;

    public ConsultarRecintoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoRecinto() { return tipoRecinto; }
    public void setTipoRecinto(String tipoRecinto) { this.tipoRecinto = tipoRecinto; }
    public BigDecimal getTasaComision() { return tasaComision; }
    public void setTasaComision(BigDecimal tasaComision) { this.tasaComision = tasaComision; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}