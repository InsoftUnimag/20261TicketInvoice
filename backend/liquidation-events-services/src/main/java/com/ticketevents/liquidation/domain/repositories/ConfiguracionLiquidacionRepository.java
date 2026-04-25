package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import java.util.Optional;

public interface ConfiguracionLiquidacionRepository {
    Optional<ConfiguracionLiquidacion> guardar(ConfiguracionLiquidacion configuracion);
    Optional<ConfiguracionLiquidacion> findByEventoId(Long eventoId);
    boolean existeEvento(Long eventoId);
}