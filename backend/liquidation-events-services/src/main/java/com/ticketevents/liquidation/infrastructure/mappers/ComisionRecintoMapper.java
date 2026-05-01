package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionRecintoDto;
import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import org.springframework.stereotype.Component;

@Component
public class ComisionRecintoMapper {

    public ComisionRecintoDto toDto(ComisionRecinto comision) {
        if (comision == null) return null;

        ComisionRecintoDto dto = new ComisionRecintoDto();
        dto.setIdComision(comision.getId());
        dto.setRecintoId(comision.getRecintoId());
        dto.setTipoComision(comision.getTipoComision() != null ? comision.getTipoComision().name() : null);
        dto.setValorComision(comision.getValorComision());
        dto.setFechaRegistro(comision.getFechaRegistro());

        return dto;
    }

    public RegistrarComisionRecintoResponse toResponse(ComisionRecintoDto dto) {
        if (dto == null) return null;

        RegistrarComisionRecintoResponse response = new RegistrarComisionRecintoResponse();
        response.setId(dto.getIdComision());
        response.setRecintoId(dto.getRecintoId());
        response.setTipoComision(dto.getTipoComision());
        response.setValorComision(dto.getValorComision());
        response.setFechaRegistro(dto.getFechaRegistro());
        response.setMensaje("Comision registrada exitosamente");

        return response;
    }
}
