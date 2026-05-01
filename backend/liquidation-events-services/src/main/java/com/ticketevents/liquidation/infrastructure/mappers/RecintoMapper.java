package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.RecintoDto;
import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import org.springframework.stereotype.Component;

@Component
public class RecintoMapper {

    public RecintoDto toDto(Recinto recinto) {
        if (recinto == null) return null;

        RecintoDto dto = new RecintoDto();
        dto.setIdRecinto(recinto.getId());
        dto.setNombreRecinto(recinto.getNombre());
        dto.setTipoRecinto(recinto.getTipoRecinto() != null ? recinto.getTipoRecinto().name() : null);
        dto.setTasaComision(recinto.getTasaComision());
        dto.setEstado(recinto.getEstado());

        return dto;
    }

    public ConsultarRecintoResponse toResponse(RecintoDto dto) {
        if (dto == null) return null;

        ConsultarRecintoResponse response = new ConsultarRecintoResponse();
        response.setId(dto.getIdRecinto());
        response.setNombre(dto.getNombreRecinto());
        response.setTipoRecinto(dto.getTipoRecinto());
        response.setTasaComision(dto.getTasaComision());
        response.setEstado(dto.getEstado());

        return response;
    }
}
