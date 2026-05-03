package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.ConfiguracionLiquidacion;
import com.ticketevents.liquidation.domain.entities.TipoLiquidacion;
import com.ticketevents.liquidation.domain.repositories.ConfiguracionLiquidacionRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaConfiguracionLiquidacionRepositoryAdapter implements ConfiguracionLiquidacionRepository {

    private final EntityManager entityManager;

    public JpaConfiguracionLiquidacionRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Optional<ConfiguracionLiquidacion> guardar(ConfiguracionLiquidacion configuracion) {
        String sql = """
                INSERT INTO configuraciones_liquidacion (evento_id, tipo_liquidacion, valor_comision, porcentaje)
                VALUES (:eventoId, :tipo, :valor, :porcentaje)
                ON CONFLICT (evento_id) DO UPDATE
                SET tipo_liquidacion = EXCLUDED.tipo_liquidacion,
                    valor_comision = EXCLUDED.valor_comision,
                    porcentaje = EXCLUDED.porcentaje
                RETURNING id
                """;
        Number id = (Number) entityManager.createNativeQuery(sql)
                .setParameter("eventoId", configuracion.getEventoId())
                .setParameter("tipo", configuracion.getTipoLiquidacion().name())
                .setParameter("valor", configuracion.getValorComision())
                .setParameter("porcentaje", configuracion.getPorcentaje())
                .getSingleResult();
        configuracion.setId(id.longValue());
        return Optional.of(configuracion);
    }

    @Override
    public Optional<ConfiguracionLiquidacion> findByEventoId(Long eventoId) {
        String sql = """
                SELECT c.id, c.evento_id, c.tipo_liquidacion, c.valor_comision, c.porcentaje
                FROM configuraciones_liquidacion c
                WHERE c.evento_id = :eventoId
                """;
        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("eventoId", eventoId)
                .getResultStream()
                .findFirst()
                .orElse(null);
        if (row == null) {
            return Optional.empty();
        }
        ConfiguracionLiquidacion c = new ConfiguracionLiquidacion();
        c.setId(((Number) row[0]).longValue());
        c.setEventoId(((Number) row[1]).longValue());
        c.setTipoLiquidacion(TipoLiquidacion.valueOf(String.valueOf(row[2])));
        c.setValorComision((BigDecimal) row[3]);
        c.setPorcentaje((BigDecimal) row[4]);
        return Optional.of(c);
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
