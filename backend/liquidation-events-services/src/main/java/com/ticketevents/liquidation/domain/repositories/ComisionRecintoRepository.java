package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import java.util.Optional;

public interface ComisionRecintoRepository {
    Optional<ComisionRecinto> guardar(ComisionRecinto comision);
    Optional<ComisionRecinto> findByRecintoId(Long recintoId);
    boolean existeRecinto(Long recintoId);
}