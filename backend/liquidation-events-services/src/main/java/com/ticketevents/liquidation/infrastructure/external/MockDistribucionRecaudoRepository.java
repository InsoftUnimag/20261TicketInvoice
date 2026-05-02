package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.DistribucionRecaudo;
import com.ticketevents.liquidation.domain.repositories.DistribucionRecaudoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Primary
public class MockDistribucionRecaudoRepository implements DistribucionRecaudoRepository {

    private static final Logger log = LoggerFactory.getLogger(MockDistribucionRecaudoRepository.class);

    private final Map<Long, DistribucionRecaudo> distribuciones = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public DistribucionRecaudo guardar(DistribucionRecaudo distribucion) {
        log.info("MOCK: Guardando distribucion de recaudo para evento: {}", distribucion.getEventoId());

        if (distribucion.getId() == null) {
            distribucion.setId(idGenerator.getAndIncrement());
        }

        distribuciones.put(distribucion.getEventoId(), distribucion);
        return distribucion;
    }

    @Override
    public Optional<DistribucionRecaudo> findByEventoId(Long eventoId) {
        log.info("MOCK: Buscando distribucion de recaudo para evento: {}", eventoId);
        return Optional.ofNullable(distribuciones.get(eventoId));
    }
}
