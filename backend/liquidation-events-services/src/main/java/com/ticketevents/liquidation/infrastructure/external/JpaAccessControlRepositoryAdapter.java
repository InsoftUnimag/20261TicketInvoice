package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.EstadoIngreso;
import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import com.ticketevents.liquidation.domain.repositories.AccessControlRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JpaAccessControlRepositoryAdapter implements AccessControlRepository {

    private final EntityManager entityManager;

    public JpaAccessControlRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<RegistroIngreso> getIngresosByEvento(Long eventoId) {
        String sql = """
                SELECT t.id, t.evento_id, t.fecha_hora_ingreso, t.estado_ingreso, t.tipo_acceso
                FROM tickets t
                WHERE t.evento_id = :eventoId
                """;

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("eventoId", eventoId)
                .getResultList();

        return rows.stream().map(row -> new RegistroIngreso(
                ((Number) row[0]).longValue(),
                ((Number) row[1]).longValue(),
                toLocalDateTime(row[2]),
                EstadoIngreso.valueOf(String.valueOf(row[3])),
                row[4] != null ? String.valueOf(row[4]) : null
        )).toList();
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
