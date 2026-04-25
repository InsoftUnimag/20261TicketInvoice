package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionLiquidacionMapper {

    public DeterminarTipoLiquidacionResponse toResponse(ConfiguracionLiquidacion configuracion) {
        if (configuracion == null) return null;

        DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
        response.setId(configuracion.getId());
        response.setEventoId(configuracion.getEventoId());
        response.setTipoLiquidacion(configuracion.getTipoLiquidacion().name());
        response.setValorComision(configuracion.getValorComision());
        response.setPorcentaje(configuracion.getPorcentaje());
        response.setMensaje("Tipo de liquidacion configurado exitosamente");

        return response;
    }
}