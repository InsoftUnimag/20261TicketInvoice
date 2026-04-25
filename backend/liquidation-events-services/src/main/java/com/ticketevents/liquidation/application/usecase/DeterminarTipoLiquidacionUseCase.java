package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.DeterminarTipoLiquidacionResponse;
import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import com.ticketevents.liquidation.infrastructure.mappers.ConfiguracionLiquidacionMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DeterminarTipoLiquidacionUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeterminarTipoLiquidacionUseCase.class);

    private final ConfiguracionLiquidacionRepository repository;
    private final ConfiguracionLiquidacionMapper mapper;

    public DeterminarTipoLiquidacionUseCase(ConfiguracionLiquidacionRepository repository, 
                                            ConfiguracionLiquidacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DeterminarTipoLiquidacionResponse execute(Long eventoId, TipoLiquidacion tipoLiquidacion, 
                                                      BigDecimal valor, BigDecimal porcentaje) {
        log.info("Iniciando configuracion de tipo de liquidacion para evento: {}", eventoId);

        if (eventoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del evento es requerido");
        }

        if (tipoLiquidacion == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El tipo de liquidacion es requerido");
        }

        boolean existeEvento;
        try {
            existeEvento = repository.existeEvento(eventoId);
        } catch (Exception e) {
            log.error("Error al verificar existencia del evento {}: {}", eventoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (!existeEvento) {
            log.warn("Evento {} no encontrado", eventoId);
            throw new BusinessException(ErrorCode.EVENT_NOT_FOUND, 
                    "Error, no se puede asignar un tipo a un evento no existente");
        }

        ConfiguracionLiquidacion configuracion = new ConfiguracionLiquidacion(
            1L,
            eventoId,
            tipoLiquidacion,
            valor,
            porcentaje
        );

        try {
            configuracion.validar();
        } catch (IllegalArgumentException e) {
            log.error("Validacion de configuracion fallida: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_REQUEST, e.getMessage());
        }

        ConfiguracionLiquidacion guardada;
        try {
            Optional<ConfiguracionLiquidacion> resultado = repository.guardar(configuracion);
            guardada = resultado.orElseThrow(() -> {
                log.error("Error al guardar configuracion para evento {}", eventoId);
                return new TechnicalException(ErrorCode.INVALID_REQUEST, 
                        "Error al guardar la configuracion de liquidacion");
            });
        } catch (Exception e) {
            log.error("Error al guardar configuracion para evento {}: {}", eventoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        DeterminarTipoLiquidacionResponse response = mapper.toResponse(guardada);

        log.info("Tipo de liquidacion configurado exitosamente para evento: {} - Tipo: {}", 
                eventoId, tipoLiquidacion);
        return response;
    }
}