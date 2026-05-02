package com.ticketevents.liquidation.infrastructure.mappers;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarDistribucionResponse;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.DistribucionRecaudoDto;
import org.springframework.stereotype.Component;

@Component
public class ConsultarDistribucionMapper {

    public ConsultarDistribucionResponse toResponse(DistribucionRecaudoDto dto) {
        if (dto == null) return null;

        ConsultarDistribucionResponse response = new ConsultarDistribucionResponse();
        response.setEventoId(dto.getEventoId());
        response.setNombreEvento(dto.getNombreEvento());
        response.setTotalBruto(dto.getTotalBruto());
        response.setTotalPagoPromotor(dto.getTotalPagoPromotor());
        response.setTotalComisionPlataforma(dto.getComisionPlataforma());
        response.setTotalDistribuible(dto.getTotalDistribuible());
        response.setEstado(dto.getEstado());
        response.setFechaCalculo(dto.getFechaCalculo());
        response.setFechaLiquidacion(dto.getFechaLiquidacion());

        return response;
    }
}
