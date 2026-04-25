package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.IngresosEvento;
import java.util.List;

public interface IngresosConsultaRepository {
    List<Object[]> obtenerTicketsAgrupados(Long eventoId);
    boolean existeEvento(Long eventoId);
}