package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import java.math.BigDecimal;

public class RecintoDto {
    private Long idRecinto;
    private String nombreRecinto;
    private String tipoRecinto;
    private BigDecimal tasaComision;
    private String ubicacion;
    private String estado;

    public RecintoDto() {}

    public RecintoDto(Long idRecinto, String nombreRecinto, String tipoRecinto, 
                      BigDecimal tasaComision, String ubicacion, String estado) {
        this.idRecinto = idRecinto;
        this.nombreRecinto = nombreRecinto;
        this.tipoRecinto = tipoRecinto;
        this.tasaComision = tasaComision;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public Long getIdRecinto() { return idRecinto; }
    public void setIdRecinto(Long idRecinto) { this.idRecinto = idRecinto; }
    public String getNombreRecinto() { return nombreRecinto; }
    public void setNombreRecinto(String nombreRecinto) { this.nombreRecinto = nombreRecinto; }
    public String getTipoRecinto() { return tipoRecinto; }
    public void setTipoRecinto(String tipoRecinto) { this.tipoRecinto = tipoRecinto; }
    public BigDecimal getTasaComision() { return tasaComision; }
    public void setTasaComision(BigDecimal tasaComision) { this.tasaComision = tasaComision; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}