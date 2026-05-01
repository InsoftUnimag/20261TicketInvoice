package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.IngresosEvento;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.IngresosTicketsDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngresosMapper {

    public IngresosTicketsDto toDto(IngresosEvento ingresos) {
        if (ingresos == null) return null;

        IngresosTicketsDto dto = new IngresosTicketsDto();
        dto.setEventoId(ingresos.getEventoId());
        dto.setTotalTicketsVendidos(ingresos.getTotalTicketsVendidos());
        dto.setTotalTicketsValidados(ingresos.getTotalTicketsValidados());
        dto.setTotalTicketsCancelados(ingresos.getTotalTicketsCancelados());
        dto.setTotalCortesias(ingresos.getTotalCortesias());
        dto.setTotalNoAsistieron(ingresos.getTotalNoAsistieron());
        dto.setTotalRecaudoBruto(ingresos.getTotalRecaudoBruto());
        dto.setHasInconsistencias(ingresos.isHasInconsistencies());

        Map<String, Integer> ticketsPorEstado = new HashMap<>();
        if (ingresos.getTicketsPorEstado() != null) {
            ingresos.getTicketsPorEstado().forEach((k, v) ->
                ticketsPorEstado.put(k.name(), v));
        }
        dto.setTicketsPorEstado(ticketsPorEstado);

        return dto;
    }

    public ConsultarIngresosResponse toResponse(IngresosTicketsDto dto) {
        if (dto == null) return null;

        ConsultarIngresosResponse response = new ConsultarIngresosResponse();
        response.setEventoId(dto.getEventoId());
        response.setTotalTicketsVendidos(dto.getTotalTicketsVendidos());
        response.setTotalTicketsValidados(dto.getTotalTicketsValidados());
        response.setTotalTicketsCancelados(dto.getTotalTicketsCancelados());
        response.setTotalCortesias(dto.getTotalCortesias());
        response.setTotalNoAsistieron(dto.getTotalNoAsistieron());
        response.setTotalRecaudoBruto(dto.getTotalRecaudoBruto());
        response.setHasInconsistencies(dto.isHasInconsistencias());

        Map<String, Integer> ticketsPorEstado = new HashMap<>();
        if (dto.getTicketsPorEstado() != null) {
            dto.getTicketsPorEstado().forEach((k, v) ->
                ticketsPorEstado.put(k, v));
        }
        response.setTicketsPorEstado(ticketsPorEstado);

        return response;
    }
}
