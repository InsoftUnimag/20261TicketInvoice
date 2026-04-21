package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import java.util.List;

public interface AccessControlRepository {
    List<RegistroIngreso> getIngresosByEvento(Long eventoId);
}