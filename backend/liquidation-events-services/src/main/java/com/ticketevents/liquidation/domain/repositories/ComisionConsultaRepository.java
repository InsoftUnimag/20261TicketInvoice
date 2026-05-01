package com.ticketevents.liquidation.domain.repositories;

import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import java.util.Optional;

public interface ComisionConsultaRepository {
    boolean existsRecintoById(Long recintoId);
    Optional<ComisionConfig> findComisionByRecintoId(Long recintoId);
}
