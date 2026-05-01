package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ConfiguracionLiquidacionDto;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionLiquidacionMapper {

    public ConfiguracionLiquidacionDto toDto(ConfiguracionLiquidacion configuracion) {
        if (configuracion == null) return null;

        ConfiguracionLiquidacionDto dto = new ConfiguracionLiquidacionDto();
        dto.setIdConfiguracion(configuracion.getId());
        dto.setEventoId(configuracion.getEventoId());
        dto.setTipoLiquidacion(configuracion.getTipoLiquidacion() != null ? configuracion.getTipoLiquidacion().name() : null);
        dto.setValorComision(configuracion.getValorComision());
        dto.setPorcentaje(configuracion.getPorcentaje());

        return dto;
    }

    public DeterminarTipoLiquidacionResponse toResponse(ConfiguracionLiquidacionDto dto) {
        if (dto == null) return null;

        DeterminarTipoLiquidacionResponse response = new DeterminarTipoLiquidacionResponse();
        response.setEventoId(dto.getEventoId());
        response.setTipoLiquidacion(dto.getTipoLiquidacion());
        response.setValorComision(dto.getValorComision());
        response.setPorcentaje(dto.getPorcentaje());
        response.setMensaje("Tipo de liquidacion configurado exitosamente");

        return response;
    }
}
