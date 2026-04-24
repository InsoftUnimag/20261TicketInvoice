package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;

public class ConsultarRecintoResponse {
    private Long id;
    private String nombre;
    private String tipoRecinto;
    private BigDecimal tasaComision;
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