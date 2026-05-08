package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Ingresos generados por la venta de tickets de un evento")
public class ConsultarIngresosResponse {

    @Schema(description = "ID del evento", example = "1")
    private Long eventoId;

    @Schema(description = "Total de tickets vendidos", example = "145")
    private int totalTicketsVendidos;

    @Schema(description = "Total de tickets validados (check-in)", example = "100")
    private int totalTicketsValidados;

    @Schema(description = "Total de tickets cancelados", example = "5")
    private int totalTicketsCancelados;

    @Schema(description = "Total de cortesías", example = "10")
    private int totalCortesias;

    @Schema(description = "Total de tickets no asistieron", example = "30")
    private int totalNoAsistieron;

    @Schema(description = "Total recaudo bruto", example = "62500.00")
    private BigDecimal totalRecaudoBruto;

    @Schema(description = "Tickets agrupados por estado financiero",
        example = "{\"VALIDADO\": 100, \"VENDIDO\": 30, \"CANCELADO\": 5, \"CORTESIA\": 10}")
    private Map<String, Integer> ticketsPorEstado;

    @Schema(description = "Indica si hay tickets sin estado definido", example = "false")
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