package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import com.ticketevents.liquidation.domain.repositories.ComisionConsultaRepository;
import com.ticketevents.liquidation.infrastructure.adapter.output.external.dto.ComisionDto;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "app.repository.mock-enabled", havingValue = "false", matchIfMissing = true)
public class JpaComisionRepositoryAdapter implements ComisionConsultaRepository {

    private final EntityManager entityManager;

    public JpaComisionRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean existsRecintoById(Long recintoId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM recintos r WHERE r.id = :recintoId)";
        Boolean exists = (Boolean) entityManager.createNativeQuery(sql)
                .setParameter("recintoId", recintoId)
                .getSingleResult();
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public Optional<ComisionConfig> findComisionByRecintoId(Long recintoId) {
        String sql = """
                SELECT c.tipo_comision, c.valor_comision
                FROM recintos r
                LEFT JOIN comisiones_recinto c ON c.recinto_id = r.id
                WHERE r.id = :recintoId
                """;

        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("recintoId", recintoId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (row == null || row[0] == null || row[1] == null) {
            return Optional.empty();
        }

        ComisionDto dto = new ComisionDto(
                com.ticketevents.liquidation.domain.entities.TipoComision.valueOf(String.valueOf(row[0])),
                (java.math.BigDecimal) row[1]
        );
        return Optional.of(new ComisionConfig(true, dto.tipoComision(), dto.valorComision()));
    }
}
