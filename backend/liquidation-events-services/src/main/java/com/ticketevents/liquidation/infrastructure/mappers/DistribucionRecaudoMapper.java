package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.CalcularDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import org.springframework.stereotype.Component;

@Component
public class DistribucionRecaudoMapper {

    public DistribucionRecaudoDto toDto(DistribucionRecaudo distribucion) {
        if (distribucion == null) return null;

        DistribucionRecaudoDto dto = new DistribucionRecaudoDto();
        dto.setId(distribucion.getId());
        dto.setEventoId(distribucion.getEventoId());
        dto.setNombreEvento(distribucion.getNombreEvento());
        dto.setTotalBruto(distribucion.getTotalBruto());
        dto.setTotalNetoPreliminar(distribucion.getTotalNetoPreliminar());
        dto.setTotalDistribuible(distribucion.getTotalDistribuible());
        dto.setComisionPlataforma(distribucion.getComisionPlataforma());
        dto.setDescuentoCancelados(distribucion.getDescuentoCancelados());
        dto.setDescuentoCortesia(distribucion.getDescuentoCortesia());
        dto.setEstado(distribucion.getEstado());
        dto.setFechaCalculo(distribucion.getFechaCalculo());
        dto.setTotalPagoPromotor(distribucion.getTotalPagoPromotor());
        dto.setFechaLiquidacion(distribucion.getFechaLiquidacion());

        return dto;
    }

    public CalcularDistribucionResponse toResponse(DistribucionRecaudoDto dto) {
        if (dto == null) return null;

        CalcularDistribucionResponse response = new CalcularDistribucionResponse();
        response.setEventoId(dto.getEventoId());
        response.setNombreEvento(dto.getNombreEvento());
        response.setTotalBruto(dto.getTotalBruto());
        response.setTotalNetoPreliminar(dto.getTotalNetoPreliminar());
        response.setTotalDistribuible(dto.getTotalDistribuible());
        response.setComisionPlataforma(dto.getComisionPlataforma());
        response.setDescuentoCancelados(dto.getDescuentoCancelados());
        response.setDescuentoCortesia(dto.getDescuentoCortesia());
        response.setEstado(dto.getEstado());
        response.setFechaCalculo(dto.getFechaCalculo());
        response.setMensaje("Distribucion del recaudo calculada exitosamente");

        return response;
    }

    public DistribucionRecaudo toEntity(DistribucionRecaudoDto dto) {
        if (dto == null) return null;

        DistribucionRecaudo entity = new DistribucionRecaudo();
        entity.setId(dto.getId());
        entity.setEventoId(dto.getEventoId());
        entity.setNombreEvento(dto.getNombreEvento());
        entity.setTotalBruto(dto.getTotalBruto());
        entity.setTotalNetoPreliminar(dto.getTotalNetoPreliminar());
        entity.setTotalDistribuible(dto.getTotalDistribuible());
        entity.setComisionPlataforma(dto.getComisionPlataforma());
        entity.setDescuentoCancelados(dto.getDescuentoCancelados());
        entity.setDescuentoCortesia(dto.getDescuentoCortesia());
        entity.setEstado(dto.getEstado());
        entity.setFechaCalculo(dto.getFechaCalculo());

        return entity;
    }
}
