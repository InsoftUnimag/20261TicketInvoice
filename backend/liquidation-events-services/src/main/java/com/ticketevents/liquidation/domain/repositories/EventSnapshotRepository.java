package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;

public interface EventSnapshotRepository {
    ResumenVentasEvento getSnapshot(Long eventoId);
}