package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.domain.repositories.RecintoRepository;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ConsultarRecintoResponse;
import com.ticketevents.liquidation.infrastructure.mappers.RecintoMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConsultarRecintoUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConsultarRecintoUseCase.class);

    private final RecintoRepository recintoRepository;
    private final RecintoMapper mapper;

    public ConsultarRecintoUseCase(RecintoRepository recintoRepository, RecintoMapper mapper) {
        this.recintoRepository = recintoRepository;
        this.mapper = mapper;
    }

    public ConsultarRecintoResponse execute(Long recintoId) {
        log.info("Iniciando consulta de recinto: {}", recintoId);

        if (recintoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del recinto es requerido");
        }

        Recinto recinto;
        try {
            recinto = recintoRepository.findById(recintoId);
        } catch (Exception e) {
            log.error("Error al consultar servicio externo para recinto {}: {}", recintoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (recinto == null) {
            throw new BusinessException(ErrorCode.RECINTO_NOT_FOUND);
        }

        try {
            validarRecinto(recinto);
        } catch (IllegalArgumentException e) {
            log.error("Validación de recinto fallida: {}", e.getMessage());
            throw new TechnicalException(ErrorCode.INVALID_REQUEST, e.getMessage());
        }

        ConsultarRecintoResponse response = mapper.toResponse(recinto);
        
        log.info("Recinto consultado exitosamente: {} - {} ({})", 
                recintoId, recinto.getNombre(), recinto.getTipoRecinto());
        return response;
    }

    private void validarRecinto(Recinto recinto) {
        if (recinto.getTasaComision() == null) {
            throw new IllegalArgumentException("La tasa de comisión es requerida");
        }
        if (recinto.getTasaComision().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La tasa de comisión no puede ser negativa");
        }
        if (recinto.getTasaComision().compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("La tasa de comisión no puede ser mayor al 100%");
        }
    }
}