package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.domain.entities.TipoRecinto;
import com.ticketevents.liquidation.domain.repositories.RecintoRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRecintoRepositoryAdapter implements RecintoRepository {

    private final EntityManager entityManager;

    public JpaRecintoRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Recinto findById(Long id) {
        String sql = """
                SELECT r.id, r.nombre, r.tipo_recinto, r.tasa_comision, r.estado
                FROM recintos r
                WHERE r.id = :id
                """;

        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (row == null) {
            return null;
        }

        Recinto recinto = new Recinto();
        recinto.setId(((Number) row[0]).longValue());
        recinto.setNombre(String.valueOf(row[1]));
        recinto.setTipoRecinto(TipoRecinto.valueOf(String.valueOf(row[2])));
        recinto.setTasaComision((BigDecimal) row[3]);
        recinto.setEstado(String.valueOf(row[4]));
        return recinto;
    }
}
