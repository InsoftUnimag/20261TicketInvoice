package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarResumenVentasResponse;
import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import com.ticketevents.liquidation.domain.repositories.EventSnapshotRepository;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConsultarResumenVentasUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConsultarResumenVentasUseCase.class);

    private final EventSnapshotRepository eventSnapshotRepository;

    public ConsultarResumenVentasUseCase(EventSnapshotRepository eventSnapshotRepository) {
        this.eventSnapshotRepository = eventSnapshotRepository;
    }

    public ConsultarResumenVentasResponse execute(Long eventoId) {
        log.info("Iniciando consulta de resumen de ventas para evento: {}", eventoId);

        if (eventoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del evento es requerido");
        }

        ResumenVentasEvento snapshot;
        try {
            snapshot = eventSnapshotRepository.getSnapshot(eventoId);
        } catch (Exception e) {
            log.error("Error al consultar servicio externo para evento {}: {}", eventoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (snapshot == null) {
            throw new BusinessException(ErrorCode.EVENT_NOT_FOUND);
        }

        String estadoEvento = snapshot.getEstadoEvento();
        if (!esEstadoCerrado(estadoEvento)) {
            log.warn("Evento {} no está cerrado. Estado actual: {}", eventoId, estadoEvento);
            throw new BusinessException(ErrorCode.EVENT_NOT_CLOSED, 
                    "El evento aún no ha sido cerrado. Estado actual: " + estadoEvento);
        }

        ConsultarResumenVentasResponse response = mapToResponse(snapshot);
        
        log.info("Resumen de ventas obtenido exitosamente para evento: {}", eventoId);
        return response;
    }

    private boolean esEstadoCerrado(String estado) {
        return estado != null && estado.equalsIgnoreCase("CERRADO");
    }

    private ConsultarResumenVentasResponse mapToResponse(ResumenVentasEvento snapshot) {
        ConsultarResumenVentasResponse response = new ConsultarResumenVentasResponse();
        response.setEventoId(snapshot.getIdEvento());
        response.setNombreEvento(snapshot.getNombreEvento());
        response.setEstadoEvento(snapshot.getEstadoEvento());
        response.setTotalTicketsVendidos(snapshot.getTotalTicketsVendidos());
        response.setTotalTicketsValidados(snapshot.getTotalTicketsValidados());
        response.setTotalTicketsCancelados(snapshot.getTotalTicketsCancelados());
        response.setTotalTicketsCortesia(snapshot.getTotalTicketsCortesia());
        response.setTotalRecaudoBruto(snapshot.getTotalRecaudoBruto());
        
        Map<String, Integer> ticketsPorCondicion = new HashMap<>();
        Map<String, BigDecimal> recaudoPorCondicion = new HashMap<>();
        
        if (snapshot.getTicketsPorCondicion() != null) {
            snapshot.getTicketsPorCondicion().forEach((k, v) -> 
                ticketsPorCondicion.put(k.name(), v));
        }
        
        if (snapshot.getRecaudoPorCondicion() != null) {
            snapshot.getRecaudoPorCondicion().forEach((k, v) -> 
                recaudoPorCondicion.put(k.name(), v));
        }
        
        response.setTicketsPorCondicion(ticketsPorCondicion);
        response.setRecaudoPorCondicion(recaudoPorCondicion);
        
        return response;
    }
}