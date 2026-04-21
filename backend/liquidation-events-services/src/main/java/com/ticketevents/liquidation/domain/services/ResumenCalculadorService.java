package com.ticketevents.liquidation.domain.services;

import com.ticketevents.liquidation.domain.entities.CondicionLiquidacion;
import com.ticketevents.liquidation.domain.entities.EstadoIngreso;
import com.ticketevents.liquidation.domain.entities.RegistroIngreso;
import com.ticketevents.liquidation.domain.entities.ResumenVentasEvento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResumenCalculadorService {

    private static final BigDecimal VALOR_TICKET = new BigDecimal("500.00");
    private static final BigDecimal VALOR_CORTESIA = BigDecimal.ZERO;

    public ResumenVentasEvento calcularResumen(Long eventoId, String nombreEvento, 
                                               List<RegistroIngreso> registros) {
        ResumenVentasEvento resumen = new ResumenVentasEvento();
        resumen.setIdEvento(eventoId);
        resumen.setNombreEvento(nombreEvento);
        resumen.setEstadoEvento("CERRADO");
        
        Map<CondicionLiquidacion, Integer> ticketsPorCondicion = new HashMap<>();
        Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion = new HashMap<>();
        
        int validados = 0;
        int vendidos = 0;
        int cortesia = 0;
        int cancelados = 0;
        
        for (RegistroIngreso reg : registros) {
            if (reg.getEstadoIngreso() == EstadoIngreso.CHECKED_IN) {
                if (reg.getTipoAcceso() != null && reg.getTipoAcceso().equals("CORTESIA")) {
                    cortesia++;
                } else {
                    validados++;
                }
            } else {
                vendidos++;
            }
        }
        
        ticketsPorCondicion.put(CondicionLiquidacion.VALIDADO, validados);
        ticketsPorCondicion.put(CondicionLiquidacion.VENDIDO, vendidos);
        ticketsPorCondicion.put(CondicionLiquidacion.CORTESIA, cortesia);
        ticketsPorCondicion.put(CondicionLiquidacion.CANCELADO, cancelados);
        
        recaudoPorCondicion.put(CondicionLiquidacion.VALIDADO, 
            VALOR_TICKET.multiply(new BigDecimal(validados)));
        recaudoPorCondicion.put(CondicionLiquidacion.VENDIDO, 
            VALOR_TICKET.multiply(new BigDecimal(vendidos)));
        recaudoPorCondicion.put(CondicionLiquidacion.CORTESIA, 
            VALOR_CORTESIA.multiply(new BigDecimal(cortesia)));
        recaudoPorCondicion.put(CondicionLiquidacion.CANCELADO, BigDecimal.ZERO);
        
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal valor : recaudoPorCondicion.values()) {
            total = total.add(valor);
        }
        
        resumen.setTicketsPorCondicion(ticketsPorCondicion);
        resumen.setRecaudoPorCondicion(recaudoPorCondicion);
        resumen.setTotalRecaudoBruto(total);
        
        return resumen;
    }
}