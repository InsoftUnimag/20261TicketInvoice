package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionConsultaRepository;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "app.repository.mock-enabled", havingValue = "true")
public class MockComisionRepository implements ComisionConsultaRepository {

    private static final Map<Long, ComisionConfig> DATA = Map.of(
            1L, new ComisionConfig(true, TipoComision.PORCENTUAL, new BigDecimal("7.50"))
    );

    @Override
    public boolean existsRecintoById(Long recintoId) {
        return recintoId == 1L || recintoId == 2L;
    }

    @Override
    public Optional<ComisionConfig> findComisionByRecintoId(Long recintoId) {
        return Optional.ofNullable(DATA.get(recintoId));
    }
}
