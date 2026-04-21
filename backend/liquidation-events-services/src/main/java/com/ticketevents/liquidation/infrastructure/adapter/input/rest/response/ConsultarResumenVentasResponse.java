package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;
import java.util.Map;

public class ConsultarResumenVentasResponse {

    private Long eventoId;
    private String nombreEvento;
    private String estadoEvento;
    private int totalTicketsVendidos;
    private BigDecimal totalRecaudoBruto;
    private Map<String, Integer> ticketsPorCondicion;
    private Map<String, BigDecimal> recaudoPorCondicion;

    public ConsultarResumenVentasResponse() {}

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getEstadoEvento() { return estadoEvento; }
    public void setEstadoEvento(String estadoEvento) { this.estadoEvento = estadoEvento; }
public int getTotalTicketsVendidos() { return totalTicketsVendidos; }
    public void setTotalTicketsVendidos(int totalTicketsVendidos) { this.totalTicketsVendidos = totalTicketsVendidos; }
    public BigDecimal getTotalRecaudoBruto() { return totalRecaudoBruto; }
    public void setTotalRecaudoBruto(BigDecimal totalRecaudoBruto) { this.totalRecaudoBruto = totalRecaudoBruto; }
    public Map<String, Integer> getTicketsPorCondicion() { return ticketsPorCondicion; }
    public void setTicketsPorCondicion(Map<String, Integer> ticketsPorCondicion) { this.ticketsPorCondicion = ticketsPorCondicion; }
    public Map<String, BigDecimal> getRecaudoPorCondicion() { return recaudoPorCondicion; }
    public void setRecaudoPorCondicion(Map<String, BigDecimal> recaudoPorCondicion) { this.recaudoPorCondicion = recaudoPorCondicion; }
}