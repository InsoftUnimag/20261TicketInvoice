package com.ticketevents.liquidation.infrastructure.adapter.input.rest.response;

import java.util.List;
import java.util.Map;

public class ConsultarEstadoIngresoResponse {
    private Long eventoId;
    private String nombreEvento;
    private List<TicketEstadoIngreso> tickets;
    private int totalTickets;
    private int totalCheckeados;
    private int totalNoAsistieron;

    public ConsultarEstadoIngresoResponse() {}

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public List<TicketEstadoIngreso> getTickets() { return tickets; }
    public void setTickets(List<TicketEstadoIngreso> tickets) { this.tickets = tickets; }
    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }
    public int getTotalCheckeados() { return totalCheckeados; }
    public void setTotalCheckeados(int totalCheckeados) { this.totalCheckeados = totalCheckeados; }
    public int getTotalNoAsistieron() { return totalNoAsistieron; }
    public void setTotalNoAsistieron(int totalNoAsistieron) { this.totalNoAsistieron = totalNoAsistieron; }

    public static class TicketEstadoIngreso {
        private Long idTicket;
        private String codigoTicket;
        private String estadoIngreso;
        private String tipoAcceso;

        public TicketEstadoIngreso() {}

        public Long getIdTicket() { return idTicket; }
        public void setIdTicket(Long idTicket) { this.idTicket = idTicket; }
        public String getCodigoTicket() { return codigoTicket; }
        public void setCodigoTicket(String codigoTicket) { this.codigoTicket = codigoTicket; }
        public String getEstadoIngreso() { return estadoIngreso; }
        public void setEstadoIngreso(String estadoIngreso) { this.estadoIngreso = estadoIngreso; }
        public String getTipoAcceso() { return tipoAcceso; }
        public void setTipoAcceso(String tipoAcceso) { this.tipoAcceso = tipoAcceso; }
    }
}