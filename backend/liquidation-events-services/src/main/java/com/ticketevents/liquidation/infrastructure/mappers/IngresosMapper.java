package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.IngresosEvento;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarIngresosResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngresosMapper {

    public ConsultarIngresosResponse toResponse(IngresosEvento ingresos) {
        if (ingresos == null) return null;

        ConsultarIngresosResponse response = new ConsultarIngresosResponse();
        response.setEventoId(ingresos.getEventoId());
        response.setTotalTicketsVendidos(ingresos.getTotalTicketsVendidos());
        response.setTotalTicketsValidados(ingresos.getTotalTicketsValidados());
        response.setTotalTicketsCancelados(ingresos.getTotalTicketsCancelados());
        response.setTotalCortesias(ingresos.getTotalCortesias());
        response.setTotalNoAsistieron(ingresos.getTotalNoAsistieron());
        response.setTotalRecaudoBruto(ingresos.getTotalRecaudoBruto());
        response.setHasInconsistencies(ingresos.isHasInconsistencies());

        Map<String, Integer> ticketsPorEstado = new HashMap<>();
        if (ingresos.getTicketsPorEstado() != null) {
            ingresos.getTicketsPorEstado().forEach((k, v) ->
                ticketsPorEstado.put(k.name(), v));
        }
        response.setTicketsPorEstado(ticketsPorEstado);

        return response;
    }
}