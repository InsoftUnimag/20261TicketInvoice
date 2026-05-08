package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Resumen consolidado de ventas de un evento")
public class ConsultarResumenVentasResponse {

    @Schema(description = "ID del evento", example = "1")
    private Long eventoId;

    @Schema(description = "Nombre del evento", example = "Concierto Rock 2026")
    private String nombreEvento;

    @Schema(description = "Estado del evento", example = "CERRADO", allowableValues = {"CERRADO", "EN_CURSO", "PROGRAMADO"})
    private String estadoEvento;

    @Schema(description = "Total de tickets vendidos", example = "145")
    private int totalTicketsVendidos;

    @Schema(description = "Total recaudo bruto", example = "62500.00")
    private BigDecimal totalRecaudoBruto;

    @Schema(description = "Cantidad de tickets agrupados por condición de liquidación",
        example = "{\"VALIDADO\": 100, \"VENDIDO\": 30, \"CANCELADO\": 5, \"CORTESIA\": 10}")
    private Map<String, Integer> ticketsPorCondicion;

    @Schema(description = "Recaudo agrupado por condición de liquidación",
        example = "{\"VALIDADO\": 50000.00, \"VENDIDO\": 15000.00, \"CANCELADO\": -2500.00, \"CORTESIA\": 0.00}")
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