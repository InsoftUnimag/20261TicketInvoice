package com.liquidacion.resumenVentas.domain.entities;

import java.math.BigDecimal;
import java.util.Map;

public class ResumenVentasEvento {
    private Long idEvento;
    private String nombreEvento;
    private String estadoEvento;
    private int totalTicketsVendidos;
    private int totalTicketsValidados;
    private int totalTicketsCancelados;
    private int totalTicketsCortesia;
    private BigDecimal totalRecaudoBruto;
    private Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion;
    private Map<CondicionLiquidacion, Integer> ticketsPorCondicion;

    public ResumenVentasEvento() {}

    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getEstadoEvento() { return estadoEvento; }
    public void setEstadoEvento(String estadoEvento) { this.estadoEvento = estadoEvento; }
    public int getTotalTicketsVendidos() { return totalTicketsVendidos; }
    public void setTotalTicketsVendidos(int totalTicketsVendidos) { this.totalTicketsVendidos = totalTicketsVendidos; }
    public int getTotalTicketsValidados() { return totalTicketsValidados; }
    public void setTotalTicketsValidados(int totalTicketsValidados) { this.totalTicketsValidados = totalTicketsValidados; }
    public int getTotalTicketsCancelados() { return totalTicketsCancelados; }
    public void setTotalTicketsCancelados(int totalTicketsCancelados) { this.totalTicketsCancelados = totalTicketsCancelados; }
    public int getTotalTicketsCortesia() { return totalTicketsCortesia; }
    public void setTotalTicketsCortesia(int totalTicketsCortesia) { this.totalTicketsCortesia = totalTicketsCortesia; }
    public BigDecimal getTotalRecaudoBruto() { return totalRecaudoBruto; }
    public void setTotalRecaudoBruto(BigDecimal totalRecaudoBruto) { this.totalRecaudoBruto = totalRecaudoBruto; }
    public Map<CondicionLiquidacion, BigDecimal> getRecaudoPorCondicion() { return recaudoPorCondicion; }
    public void setRecaudoPorCondicion(Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion) { this.recaudoPorCondicion = recaudoPorCondicion; }
    public Map<CondicionLiquidacion, Integer> getTicketsPorCondicion() { return ticketsPorCondicion; }
    public void setTicketsPorCondicion(Map<CondicionLiquidacion, Integer> ticketsPorCondicion) { this.ticketsPorCondicion = ticketsPorCondicion; }
}