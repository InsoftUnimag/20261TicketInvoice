package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.request.ConsultarResumenVentasRequest;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResumenVentasMapper {

    public ConsultarResumenVentasResponse toResponse(ResumenVentasEvento snapshot) {
        if (snapshot == null) return null;

        ConsultarResumenVentasResponse response = new ConsultarResumenVentasResponse();
        response.setEventoId(snapshot.getIdEvento());
        response.setNombreEvento(snapshot.getNombreEvento());
        response.setEstadoEvento(snapshot.getEstadoEvento());
        response.setTotalRecaudoBruto(snapshot.getTotalRecaudoBruto());

        Map<String, Integer> ticketsPorCondicion = new HashMap<>();
        Map<String, BigDecimal> recaudoPorCondicion = new HashMap<>();

        if (snapshot.getTicketsPorCondicion() != null) {
            snapshot.getTicketsPorCondicion().forEach((k, v) ->
                ticketsPorCondicion.put(k.name(), v));
        }

        if (snapshot.getRecaudoPorCondicion() != null) {
            snapshot.getRecaudoPorCondicion().forEach((k, v) ->
                recaudoPorCondicion.put(k.name(), v));
        }

        int totalTickets = ticketsPorCondicion.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        response.setTicketsPorCondicion(ticketsPorCondicion);
        response.setRecaudoPorCondicion(recaudoPorCondicion);
        response.setTotalTicketsVendidos(totalTickets);

        return response;
    }
}