package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.EstadoIngreso;
import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarEstadoIngresoResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstadoIngresoMapper {

    public ConsultarEstadoIngresoResponse toResponse(Long eventoId, String nombreEvento, List<RegistroIngreso> registros) {
        if (registros == null || registros.isEmpty()) return null;

        ConsultarEstadoIngresoResponse response = new ConsultarEstadoIngresoResponse();
        response.setEventoId(eventoId);
        response.setNombreEvento(nombreEvento);

        List<ConsultarEstadoIngresoResponse.TicketEstadoIngreso> tickets = new ArrayList<>();
        int checkeados = 0;
        int noAsistieron = 0;

        for (RegistroIngreso reg : registros) {
            ConsultarEstadoIngresoResponse.TicketEstadoIngreso ticket = new ConsultarEstadoIngresoResponse.TicketEstadoIngreso();
            ticket.setIdTicket(reg.getIdTicket());
            ticket.setCodigoTicket("TKT-" + reg.getIdTicket());
            ticket.setEstadoIngreso(reg.getEstadoIngreso().name());
            ticket.setTipoAcceso(reg.getTipoAcceso());
            tickets.add(ticket);

            if (reg.getEstadoIngreso() == EstadoIngreso.CHECKED_IN) {
                checkeados++;
            } else {
                noAsistieron++;
            }
        }

        response.setTickets(tickets);
        response.setTotalTickets(tickets.size());
        response.setTotalCheckeados(checkeados);
        response.setTotalNoAsistieron(noAsistieron);

        return response;
    }
}