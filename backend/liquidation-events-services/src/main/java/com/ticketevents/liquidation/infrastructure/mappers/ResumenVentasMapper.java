package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.EventSnapshotDto;
import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResumenVentasMapper {

    public EventSnapshotDto toDto(ResumenVentasEvento snapshot) {
        if (snapshot == null) return null;

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

        EventSnapshotDto dto = new EventSnapshotDto();
        dto.setIdEvento(snapshot.getIdEvento());
        dto.setNombreEvento(snapshot.getNombreEvento());
        dto.setEstadoEvento(snapshot.getEstadoEvento());
        dto.setTotalTicketsVendidos(totalTickets);
        dto.setTotalRecaudoBruto(snapshot.getTotalRecaudoBruto());
        dto.setTicketsPorCondicion(ticketsPorCondicion);
        dto.setRecaudoPorCondicion(recaudoPorCondicion);

        return dto;
    }

    public ConsultarResumenVentasResponse toResponse(EventSnapshotDto dto) {
        if (dto == null) return null;

        ConsultarResumenVentasResponse response = new ConsultarResumenVentasResponse();
        response.setEventoId(dto.getIdEvento());
        response.setNombreEvento(dto.getNombreEvento());
        response.setEstadoEvento(dto.getEstadoEvento());
        response.setTotalRecaudoBruto(dto.getTotalRecaudoBruto());
        response.setTicketsPorCondicion(dto.getTicketsPorCondicion());
        response.setRecaudoPorCondicion(dto.getRecaudoPorCondicion());
        response.setTotalTicketsVendidos(dto.getTotalTicketsVendidos());

        return response;
    }
}
