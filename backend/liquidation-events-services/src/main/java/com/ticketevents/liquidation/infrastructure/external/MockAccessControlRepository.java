package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.EstadoIngreso;
import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import com.ticketevents.liquidation.domain.repositories.AccessControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class MockAccessControlRepository implements AccessControlRepository {

    private static final Logger log = LoggerFactory.getLogger(MockAccessControlRepository.class);

    @Override
    public List<RegistroIngreso> getIngresosByEvento(Long eventoId) {
        log.info("MOCK: Devolviendo registros de ingreso simulado para evento: {}", eventoId);
        
        if (eventoId == null || eventoId <= 0) {
            return new ArrayList<>();
        }

        return crearRegistrosPorEvento(eventoId);
    }

    private List<RegistroIngreso> crearRegistrosPorEvento(Long eventoId) {
        List<RegistroIngreso> registros = new ArrayList<>();
        
        switch (eventoId.intValue()) {
            case 1 -> {
                int idTicket = 1;
                
                // VALIDADO (100): Vendidos y asistentes
                for (int i = 0; i < 100; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        LocalDateTime.now().minusHours(2),
                        EstadoIngreso.CHECKED_IN,
                        "INGRESO"
                    ));
                }
                
                // VENDIDO (30): Vendieron pero no asistieron
                for (int i = 0; i < 30; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        null,
                        EstadoIngreso.NOT_ATTENDED,
                        null
                    ));
                }
                
                // CORTESIA (10): Cortesias que asistieron
                for (int i = 0; i < 10; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        LocalDateTime.now().minusHours(1),
                        EstadoIngreso.CHECKED_IN,
                        "CORTESIA"
                    ));
                }
                
                // CANCELADO (5): Cancelados no asistieron
                for (int i = 0; i < 5; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        null,
                        EstadoIngreso.NOT_ATTENDED,
                        null
                    ));
                }
            }
            case 2 -> {
                int idTicket = 1;
                
                // VALIDADO (400)
                for (int i = 0; i < 400; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        LocalDateTime.now().minusHours(5),
                        EstadoIngreso.CHECKED_IN,
                        "INGRESO"
                    ));
                }
                
                // VENDIDO (80)
                for (int i = 0; i < 80; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        null,
                        EstadoIngreso.NOT_ATTENDED,
                        null
                    ));
                }
                
                // CORTESIA (10)
                for (int i = 0; i < 10; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        LocalDateTime.now().minusHours(4),
                        EstadoIngreso.CHECKED_IN,
                        "CORTESIA"
                    ));
                }
                
                // CANCELADO (10)
                for (int i = 0; i < 10; i++) {
                    registros.add(new RegistroIngreso(
                        (long) idTicket++,
                        eventoId,
                        null,
                        EstadoIngreso.NOT_ATTENDED,
                        null
                    ));
                }
            }
            default -> {}
        }
        
        return registros;
    }
}