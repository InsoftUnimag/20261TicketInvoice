package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.EstadoFinanciero;
import com.ticketevents.liquidation.domain.repositories.IngresosConsultaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Primary
public class MockIngresosRepository implements IngresosConsultaRepository {

    private static final Logger log = LoggerFactory.getLogger(MockIngresosRepository.class);

    @Override
    public List<Object[]> obtenerTicketsAgrupados(Long eventoId) {
        log.info("MOCK: Obteniendo tickets agrupados para evento: {}", eventoId);
        return crearTicketsPorEvento(eventoId);
    }

    @Override
    public boolean existeEvento(Long eventoId) {
        log.info("MOCK: Verificando existencia del evento: {}", eventoId);
        return Arrays.asList(1L, 2L, 3L).contains(eventoId);
    }

    private List<Object[]> crearTicketsPorEvento(Long eventoId) {
        List<Object[]> tickets = new ArrayList<>();

        switch (eventoId.intValue()) {
            case 1 -> {
                for (int i = 0; i < 100; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
                }
                for (int i = 0; i < 10; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CORTESIA, BigDecimal.ZERO});
                }
                for (int i = 0; i < 30; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.NO_ASISTIO, new BigDecimal("500.00")});
                }
                for (int i = 0; i < 5; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CANCELADO, new BigDecimal("500.00")});
                }
            }
            case 2 -> {
                for (int i = 0; i < 400; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("500.00")});
                }
                for (int i = 0; i < 80; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.NO_ASISTIO, new BigDecimal("500.00")});
                }
                for (int i = 0; i < 10; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CANCELADO, new BigDecimal("500.00")});
                }
                for (int i = 0; i < 10; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CORTESIA, BigDecimal.ZERO});
                }
            }
            case 3 -> {
                for (int i = 0; i < 1800; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.VALIDADO, new BigDecimal("250.00")});
                }
                for (int i = 0; i < 20; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CORTESIA, BigDecimal.ZERO});
                }
                for (int i = 0; i < 150; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.NO_ASISTIO, new BigDecimal("250.00")});
                }
                for (int i = 0; i < 30; i++) {
                    tickets.add(new Object[]{EstadoFinanciero.CANCELADO, new BigDecimal("250.00")});
                }
            }
            default -> {
            }
        }

        return tickets;
    }
}