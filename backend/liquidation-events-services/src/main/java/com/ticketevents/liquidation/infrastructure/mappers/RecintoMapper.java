package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import org.springframework.stereotype.Component;

@Component
public class RecintoMapper {

    public ConsultarRecintoResponse toResponse(Recinto recinto) {
        if (recinto == null) return null;

        ConsultarRecintoResponse response = new ConsultarRecintoResponse();
        response.setId(recinto.getId());
        response.setNombre(recinto.getNombre());
        response.setTipoRecinto(recinto.getTipoRecinto().name());
        response.setTasaComision(recinto.getTasaComision());
        response.setEstado(recinto.getEstado());

        return response;
    }
}