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
                
                // 100 vendidos que asistieron + 10 cortesias que asistieron = 110 validados
                // 30 vendidos que no asistieron
                // 5 cancelados
                // 10 cortesias (ya incluidass en validados)
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(110, 30, 5, 10));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("50000.00"),  // 100 vendidos * 500 (las 10 cortesias son gratis)
                    new BigDecimal("15000.00"),  // 30 vendidos sin asistir * 500
                    new BigDecimal("-2500.00"),  // 5 cancelados * 500
                    BigDecimal.ZERO  // cortesias = 0
                ));
                
                resumen.setTotalRecaudoBruto(new BigDecimal("62500.00"));
            }
            case 2 -> {
                resumen.setNombreEvento("Festival de Teatro Nacional");
                resumen.setEstadoEvento("CERRADO");
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(400, 80, 10, 10));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("200000.00"),
                    new BigDecimal("40000.00"),
                    new BigDecimal("-5000.00"),
                    BigDecimal.ZERO
                ));
                
                resumen.setTotalRecaudoBruto(new BigDecimal("235000.00"));
            }
            case 3 -> {
                resumen.setNombreEvento("Copa América - Semifinal");
                resumen.setEstadoEvento("CERRADO");
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(1800, 150, 30, 20));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("450000.00"),
                    new BigDecimal("37500.00"),
                    new BigDecimal("-7500.00"),
                    BigDecimal.ZERO
                ));
                
                resumen.setTotalRecaudoBruto(new BigDecimal("480000.00"));
            }
            case 4 -> {
                resumen.setNombreEvento("Expo Tecnología 2026");
                resumen.setEstadoEvento("EN_CURSO");
                
                resumen.setTicketsPorCondicion(crearTicketsPorCondicion(300, 400, 20, 30));
                resumen.setRecaudoPorCondicion(crearRecaudoPorCondicion(
                    new BigDecimal("60000.00"),
                    new BigDecimal("80000.00"),
                    new BigDecimal("-4000.00"),
                    BigDecimal.ZERO
                ));
                
                resumen.setTotalRecaudoBruto(new BigDecimal("136000.00"));
            }
            default -> {
                resumen.setNombreEvento("Evento #" + eventoId);
                resumen.setEstadoEvento("PROGRAMADO");
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