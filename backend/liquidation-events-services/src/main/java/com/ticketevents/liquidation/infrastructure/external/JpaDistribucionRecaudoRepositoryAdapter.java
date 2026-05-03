package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.domain.repositories.DistribucionRecaudoRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaDistribucionRecaudoRepositoryAdapter implements DistribucionRecaudoRepository {

    private final EntityManager entityManager;

    public JpaDistribucionRecaudoRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public DistribucionRecaudo guardar(DistribucionRecaudo d) {
        String sql = """
                INSERT INTO distribucion_recaudo (
                    evento_id, nombre_evento, total_bruto, descuento_cancelados, descuento_cortesia,
                    total_neto_preliminar, total_distribuible
                ) VALUES (
                    :eventoId, :nombreEvento, :totalBruto, :descuentoCancelados, :descuentoCortesia,
                    :totalNetoPreliminar, :totalDistribuible
                )
                ON CONFLICT (evento_id) DO UPDATE
                SET nombre_evento = EXCLUDED.nombre_evento,
                    total_bruto = EXCLUDED.total_bruto,
                    descuento_cancelados = EXCLUDED.descuento_cancelados,
                    descuento_cortesia = EXCLUDED.descuento_cortesia,
                    total_neto_preliminar = EXCLUDED.total_neto_preliminar,
                    total_distribuible = EXCLUDED.total_distribuible
                RETURNING id
                """;
        Number id = (Number) entityManager.createNativeQuery(sql)
                .setParameter("eventoId", d.getEventoId())
                .setParameter("nombreEvento", d.getNombreEvento())
                .setParameter("totalBruto", d.getTotalBruto())
                .setParameter("descuentoCancelados", d.getDescuentoCancelados())
                .setParameter("descuentoCortesia", d.getDescuentoCortesia())
                .setParameter("totalNetoPreliminar", d.getTotalNetoPreliminar())
                .setParameter("totalDistribuible", d.getTotalDistribuible())
                .getSingleResult();
        d.setId(id.longValue());
        return d;
    }

    @Override
    public Optional<DistribucionRecaudo> findByEventoId(Long eventoId) {
        String sql = """
                SELECT id, evento_id, nombre_evento, total_bruto, descuento_cancelados, descuento_cortesia,
                       total_neto_preliminar, total_distribuible
                FROM distribucion_recaudo
                WHERE evento_id = :eventoId
                """;
        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("eventoId", eventoId)
                .getResultStream()
                .findFirst()
                .orElse(null);
        if (row == null) {
            return Optional.empty();
        }

        DistribucionRecaudo d = new DistribucionRecaudo(
                ((Number) row[1]).longValue(),
                String.valueOf(row[2]),
                (BigDecimal) row[3]
        );
        d.setId(((Number) row[0]).longValue());
        d.setDescuentoCancelados((BigDecimal) row[4]);
        d.setDescuentoCortesia((BigDecimal) row[5]);
        d.setTotalNetoPreliminar((BigDecimal) row[6]);
        d.setTotalDistribuible((BigDecimal) row[7]);
        return Optional.of(d);
    }
}
