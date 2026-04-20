package com.liquidacion.resumenVentas.domain.repositories;

import com.liquidacion.resumenVentas.domain.entities.ResumenVentasEvento;

public interface EventSnapshotRepository {
    ResumenVentasEvento getSnapshot(Long eventoId);
}