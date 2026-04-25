package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.domain.repositories.ComisionRecintoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class MockComisionRecintoRepository implements ComisionRecintoRepository {

    private static final Logger log = LoggerFactory.getLogger(MockComisionRecintoRepository.class);

    @Override
    public Optional<ComisionRecinto> guardar(ComisionRecinto comision) {
        log.info("MOCK: Guardando comision para recinto: {}", comision.getRecintoId());
        return Optional.of(comision);
    }

    @Override
    public Optional<ComisionRecinto> findByRecintoId(Long recintoId) {
        log.info("MOCK: Buscando comision para recinto: {}", recintoId);
        return Optional.empty();
    }

    @Override
    public boolean existeRecinto(Long recintoId) {
        log.info("MOCK: Verificando existencia del recinto: {}", recintoId);
        return java.util.Arrays.asList(1L, 2L, 3L).contains(recintoId);
    }
}