package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionRecintoRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaComisionRecintoRepositoryAdapter implements ComisionRecintoRepository {

    private final EntityManager entityManager;

    public JpaComisionRecintoRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Optional<ComisionRecinto> guardar(ComisionRecinto comision) {
        String sql = """
                INSERT INTO comisiones_recinto (recinto_id, tipo_comision, valor_comision)
                VALUES (:recintoId, :tipo, :valor)
                ON CONFLICT (recinto_id) DO UPDATE
                SET tipo_comision = EXCLUDED.tipo_comision,
                    valor_comision = EXCLUDED.valor_comision
                RETURNING id, fecha_registro
                """;
        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("recintoId", comision.getRecintoId())
                .setParameter("tipo", comision.getTipoComision().name())
                .setParameter("valor", comision.getValorComision())
                .getSingleResult();
        comision.setId(((Number) row[0]).longValue());
        comision.setFechaRegistro(toLocalDateTime(row[1]));
        return Optional.of(comision);
    }

    @Override
    public Optional<ComisionRecinto> findByRecintoId(Long recintoId) {
        String sql = """
                SELECT c.id, c.recinto_id, c.tipo_comision, c.valor_comision, c.fecha_registro
                FROM comisiones_recinto c
                WHERE c.recinto_id = :recintoId
                """;
        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("recintoId", recintoId)
                .getResultStream()
                .findFirst()
                .orElse(null);
        if (row == null) {
            return Optional.empty();
        }
        ComisionRecinto c = new ComisionRecinto();
        c.setId(((Number) row[0]).longValue());
        c.setRecintoId(((Number) row[1]).longValue());
        c.setTipoComision(TipoComision.valueOf(String.valueOf(row[2])));
        c.setValorComision((BigDecimal) row[3]);
        c.setFechaRegistro(toLocalDateTime(row[4]));
        return Optional.of(c);
    }

    @Override
    public boolean existeRecinto(Long recintoId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM recintos r WHERE r.id = :recintoId)";
        Boolean exists = (Boolean) entityManager.createNativeQuery(sql)
                .setParameter("recintoId", recintoId)
                .getSingleResult();
        return Boolean.TRUE.equals(exists);
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        return LocalDateTime.parse(String.valueOf(value));
    }
}
