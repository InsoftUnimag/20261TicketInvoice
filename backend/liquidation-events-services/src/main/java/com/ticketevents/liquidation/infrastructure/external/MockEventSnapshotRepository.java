package com.ticketevents.liquidation.infrastructure.external;

import com.ticketevents.liquidation.domain.entities.CondicionLiquidacion;
import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import com.ticketevents.liquidation.domain.repositories.EventSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class MockEventSnapshotRepository implements EventSnapshotRepository {

    private static final Logger log = LoggerFactory.getLogger(MockEventSnapshotRepository.class);

    @Override
    public ResumenVentasEvento getSnapshot(Long eventoId) {
        log.info("MOCK: Devolviendo snapshot simulado para evento: {}", eventoId);
        return crearResumenPorEvento(eventoId);
    }

    private ResumenVentasEvento crearResumenPorEvento(Long eventoId) {
        ResumenVentasEvento resumen = new ResumenVentasEvento();
        resumen.setIdEvento(eventoId);

        switch (eventoId.intValue()) {
            case 1 -> {
                resumen.setNombreEvento("Concierto Rock 2026");
                resumen.setEstadoEvento("CERRADO");
                resumen.setTotalTicketsVendidos(150);
                resumen.setTotalTicketsValidados(120);
                resumen.setTotalTicketsCancelados(10);
                resumen.setTotalTicketsCortesia(20);
                resumen.setTotalRecaudoBruto(new BigDecimal("75000.00"));
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(120, 10, 10, 20));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("60000.00"),
                    new BigDecimal("5000.00"),
                    new BigDecimal("-5000.00"),
                    BigDecimal.ZERO
                ));
            }
            case 2 -> {
                resumen.setNombreEvento("Festival de Teatro Nacional");
                resumen.setEstadoEvento("CERRADO");
                resumen.setTotalTicketsVendidos(500);
                resumen.setTotalTicketsValidados(480);
                resumen.setTotalTicketsCancelados(15);
                resumen.setTotalTicketsCortesia(5);
                resumen.setTotalRecaudoBruto(new BigDecimal("250000.00"));
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(480, 15, 15, 5));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("240000.00"),
                    new BigDecimal("7500.00"),
                    new BigDecimal("-7500.00"),
                    BigDecimal.ZERO
                ));
            }
            case 3 -> {
                resumen.setNombreEvento("Copa América - Semifinal");
                resumen.setEstadoEvento("CERRADO");
                resumen.setTotalTicketsVendidos(2000);
                resumen.setTotalTicketsValidados(1950);
                resumen.setTotalTicketsCancelados(30);
                resumen.setTotalTicketsCortesia(20);
                resumen.setTotalRecaudoBruto(new BigDecimal("500000.00"));
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(1950, 30, 30, 20));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("487500.00"),
                    new BigDecimal("7500.00"),
                    new BigDecimal("-7500.00"),
                    BigDecimal.ZERO
                ));
            }
            case 4 -> {
                resumen.setNombreEvento("Expo Tecnología 2026");
                resumen.setEstadoEvento("EN_CURSO");
                resumen.setTotalTicketsVendidos(800);
                resumen.setTotalTicketsValidados(400);
                resumen.setTotalTicketsCancelados(5);
                resumen.setTotalTicketsCortesia(50);
                resumen.setTotalRecaudoBruto(new BigDecimal("160000.00"));
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(400, 345, 5, 50));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("80000.00"),
                    new BigDecimal("69000.00"),
                    new BigDecimal("-1000.00"),
                    BigDecimal.ZERO
                ));
            }
            default -> {
                resumen.setNombreEvento("Evento #" + eventoId);
                resumen.setEstadoEvento("PROGRAMADO");
                resumen.setTotalTicketsVendidos(0);
                resumen.setTotalTicketsValidados(0);
                resumen.setTotalTicketsCancelados(0);
                resumen.setTotalTicketsCortesia(0);
                resumen.setTotalRecaudoBruto(BigDecimal.ZERO);
                resumen.setTicketsPorCondicion(new HashMap<>());
                resumen.setRecaudoPorCondicion(new HashMap<>());
            }
        }

        return resumen;
    }

    private Map<CondicionLiquidacion, Integer> crearTicketsPorCondicion(int validados, int vendidos, int cancelados, int cortesia) {
        Map<CondicionLiquidacion, Integer> map = new HashMap<>();
        map.put(CondicionLiquidacion.VALIDADO, validados);
        map.put(CondicionLiquidacion.VENDIDO, vendidos);
        map.put(CondicionLiquidacion.CANCELADO, cancelados);
        map.put(CondicionLiquidacion.CORTESIA, cortesia);
        return map;
    }

    private Map<CondicionLiquidacion, BigDecimal> crearRecaudoPorCondicion(
            BigDecimal validados, BigDecimal vendidos, BigDecimal cancelados, BigDecimal cortesia) {
        Map<CondicionLiquidacion, BigDecimal> map = new HashMap<>();
        map.put(CondicionLiquidacion.VALIDADO, validados);
        map.put(CondicionLiquidacion.VENDIDO, vendidos);
        map.put(CondicionLiquidacion.CANCELADO, cancelados);
        map.put(CondicionLiquidacion.CORTESIA, cortesia);
        return map;
    }
}
