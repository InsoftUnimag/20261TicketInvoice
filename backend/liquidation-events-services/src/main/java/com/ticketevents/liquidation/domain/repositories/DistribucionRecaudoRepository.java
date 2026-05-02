package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;

import java.util.Optional;

public interface DistribucionRecaudoRepository {

    DistribucionRecaudo guardar(DistribucionRecaudo distribucion);

    Optional<DistribucionRecaudo> findByEventoId(Long eventoId);
}
