package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.Recinto;
import com.ticketevents.liquidation.domain.entities.TipoRecinto;
import com.ticketevents.liquidation.domain.repositories.RecintoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class MockRecintoRepository implements RecintoRepository {

    private static final Logger log = LoggerFactory.getLogger(MockRecintoRepository.class);

    @Override
    public Recinto findById(Long id) {
        log.info("MOCK: Buscando recinto con ID: {}", id);
        
        if (id == null || id <= 0) {
            return null;
        }
        
        return crearRecinto(id);
    }

    private Recinto crearRecinto(Long id) {
        Recinto recinto = new Recinto();
        recinto.setId(id);
        
        // Cada recinto tiene su propia tasa según el acuerdo
        switch (id.intValue()) {
            case 1 -> {
                // Estadio Nacional - acuerdo específico: 12%
                recinto.setNombre("Estadio Nacional de Colombia");
                recinto.setTipoRecinto(TipoRecinto.ESTADIO);
                recinto.setTasaComision(new BigDecimal("0.12"));
                recinto.setEstado("ACTIVO");
            }
            case 2 -> {
                // Teatro Colón - acuerdo específico: 8%
                recinto.setNombre("Teatro Colón");
                recinto.setTipoRecinto(TipoRecinto.TEATRO);
                recinto.setTasaComision(new BigDecimal("0.08"));
                recinto.setEstado("ACTIVO");
            }
            case 3 -> {
                // Estadio Metropolitano - acuerdo específico: 15%
                recinto.setNombre("Estadio Metropolitano");
                recinto.setTipoRecinto(TipoRecinto.ESTADIO);
                recinto.setTasaComision(new BigDecimal("0.15"));
                recinto.setEstado("ACTIVO");
            }
            default -> {
                return null;
            }
        }
        
        return recinto;
    }
}