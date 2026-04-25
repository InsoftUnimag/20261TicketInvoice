package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import org.springframework.stereotype.Component;

@Component
public class ComisionRecintoMapper {

    public RegistrarComisionRecintoResponse toResponse(ComisionRecinto comision) {
        if (comision == null) return null;

        RegistrarComisionRecintoResponse response = new RegistrarComisionRecintoResponse();
        response.setId(comision.getId());
        response.setRecintoId(comision.getRecintoId());
        response.setTipoComision(comision.getTipoComision().name());
        response.setValorComision(comision.getValorComision());
        response.setFechaRegistro(comision.getFechaRegistro());
        response.setMensaje("Comision registrada exitosamente");

        return response;
    }
}