package com.ticketevents.liquidation.domain.entities;

import java.math.BigDecimal;
import java.util.Map;

public class ResumenVentasEvento {
    private Long idEvento;
    private String nombreEvento;
    private String estadoEvento;
    private BigDecimal totalRecaudoBruto;
    private Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion;
    private Map<CondicionLiquidacion, Integer> ticketsPorCondicion;

    public ResumenVentasEvento() {}

    public void validar() {
        if (totalRecaudoBruto == null) {
            throw new IllegalArgumentException("El recaudo bruto es requerido");
        }
        if (totalRecaudoBruto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El recaudo bruto no puede ser negativo");
        }
        if (ticketsPorCondicion == null || ticketsPorCondicion.isEmpty()) {
            throw new IllegalArgumentException("Los tickets por condicion son requeridos");
        }
        validarSumaTickets();
        validarRecaudo();
    }

    private void validarSumaTickets() {
        int suma = ticketsPorCondicion.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (suma <= 0) {
            throw new IllegalArgumentException("El total de tickets debe ser mayor a 0");
        }
    }

    private void validarRecaudo() {
        if (recaudoPorCondicion != null) {
            for (Map.Entry<CondicionLiquidacion, BigDecimal> entry : recaudoPorCondicion.entrySet()) {
                CondicionLiquidacion condicion = entry.getKey();
                BigDecimal valor = entry.getValue();
                
                if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
                    if (condicion != CondicionLiquidacion.CANCELADO) {
                        throw new IllegalArgumentException(
                            "Solo CANCELADO puede tener valor negativo. Encontrado en: " + condicion);
                    }
                }
            }
        }
    }

    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getEstadoEvento() { return estadoEvento; }
    public void setEstadoEvento(String estadoEvento) { this.estadoEvento = estadoEvento; }
    public BigDecimal getTotalRecaudoBruto() { return totalRecaudoBruto; }
    public void setTotalRecaudoBruto(BigDecimal totalRecaudoBruto) { this.totalRecaudoBruto = totalRecaudoBruto; }
    public Map<CondicionLiquidacion, BigDecimal> getRecaudoPorCondicion() { return recaudoPorCondicion; }
    public void setRecaudoPorCondicion(Map<CondicionLiquidacion, BigDecimal> recaudoPorCondicion) { this.recaudoPorCondicion = recaudoPorCondicion; }
    public Map<CondicionLiquidacion, Integer> getTicketsPorCondicion() { return ticketsPorCondicion; }
    public void setTicketsPorCondicion(Map<CondicionLiquidacion, Integer> ticketsPorCondicion) { this.ticketsPorCondicion = ticketsPorCondicion; }
}