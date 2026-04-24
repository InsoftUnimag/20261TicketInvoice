package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.Recinto;

public interface RecintoRepository {
    Recinto findById(Long id);
}