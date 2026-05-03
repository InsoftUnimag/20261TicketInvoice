package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.EstadoFinanciero;
import com.ticketevents.liquidation.domain.repositories.IngresosConsultaRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JpaIngresosRepositoryAdapter implements IngresosConsultaRepository {

    private final EntityManager entityManager;

    public JpaIngresosRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> obtenerTicketsAgrupados(Long eventoId) {
        String sql = """
                SELECT t.estado_financiero, t.valor_ticket
                FROM tickets t
                WHERE t.evento_id = :eventoId
                """;
        return entityManager.createNativeQuery(sql)
                .setParameter("eventoId", eventoId)
                .getResultList()
                .stream()
                .map(row -> {
                    Object[] r = (Object[]) row;
                    return new Object[]{
                            EstadoFinanciero.valueOf(String.valueOf(r[0])),
                            (BigDecimal) r[1]
                    };
                })
                .toList();
    }

    @Override
    public boolean existeEvento(Long eventoId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM eventos e WHERE e.id = :eventoId)";
        Boolean exists = (Boolean) entityManager.createNativeQuery(sql)
                .setParameter("eventoId", eventoId)
                .getSingleResult();
        return Boolean.TRUE.equals(exists);
    }
}
