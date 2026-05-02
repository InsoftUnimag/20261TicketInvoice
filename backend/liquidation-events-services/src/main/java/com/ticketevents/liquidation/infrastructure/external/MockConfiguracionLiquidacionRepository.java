package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class MockConfiguracionLiquidacionRepository implements ConfiguracionLiquidacionRepository {

    private static final Logger log = LoggerFactory.getLogger(MockConfiguracionLiquidacionRepository.class);
    private static final Map<Long, ConfiguracionLiquidacion> DATA = new ConcurrentHashMap<>();

    @Override
    public Optional<ConfiguracionLiquidacion> guardar(ConfiguracionLiquidacion configuracion) {
        log.info("MOCK: Guardando configuracion de liquidacion para evento: {}", configuracion.getEventoId());
        DATA.put(configuracion.getEventoId(), configuracion);
        return Optional.of(configuracion);
    }

    @Override
    public Optional<ConfiguracionLiquidacion> findByEventoId(Long eventoId) {
        log.info("MOCK: Buscando configuracion para evento: {}", eventoId);
        return Optional.ofNullable(DATA.get(eventoId));
    }

    @Override
    public boolean existeEvento(Long eventoId) {
        log.info("MOCK: Verificando existencia del evento: {}", eventoId);
        return Arrays.asList(1L, 2L, 3L).contains(eventoId);
    }
}
