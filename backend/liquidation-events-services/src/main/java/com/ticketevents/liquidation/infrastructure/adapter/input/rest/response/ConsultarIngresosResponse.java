package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.math.BigDecimal;
import java.util.Map;

public class ConsultarIngresosResponse {

    private Long eventoId;
    private int totalTicketsVendidos;
    private int totalTicketsValidados;
    private int totalTicketsCancelados;
    private int totalCortesias;
    private int totalNoAsistieron;
    private BigDecimal totalRecaudoBruto;
    private Map<String, Integer> ticketsPorEstado;
    private boolean hasInconsistencies;

    public ConsultarIngresosResponse() {}

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public int getTotalTicketsVendidos() { return totalTicketsVendidos; }
    public void setTotalTicketsVendidos(int totalTicketsVendidos) { this.totalTicketsVendidos = totalTicketsVendidos; }
    public int getTotalTicketsValidados() { return totalTicketsValidados; }
    public void setTotalTicketsValidados(int totalTicketsValidados) { this.totalTicketsValidados = totalTicketsValidados; }
    public int getTotalTicketsCancelados() { return totalTicketsCancelados; }
    public void setTotalTicketsCancelados(int totalTicketsCancelados) { this.totalTicketsCancelados = totalTicketsCancelados; }
    public int getTotalCortesias() { return totalCortesias; }
    public void setTotalCortesias(int totalCortesias) { this.totalCortesias = totalCortesias; }
    public int getTotalNoAsistieron() { return totalNoAsistieron; }
    public void setTotalNoAsistieron(int totalNoAsistieron) { this.totalNoAsistieron = totalNoAsistieron; }
    public BigDecimal getTotalRecaudoBruto() { return totalRecaudoBruto; }
    public void setTotalRecaudoBruto(BigDecimal totalRecaudoBruto) { this.totalRecaudoBruto = totalRecaudoBruto; }
    public Map<String, Integer> getTicketsPorEstado() { return ticketsPorEstado; }
    public void setTicketsPorEstado(Map<String, Integer> ticketsPorEstado) { this.ticketsPorEstado = ticketsPorEstado; }
    public boolean isHasInconsistencies() { return hasInconsistencies; }
    public void setHasInconsistencies(boolean hasInconsistencies) { this.hasInconsistencies = hasInconsistencies; }
}