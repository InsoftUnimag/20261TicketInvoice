package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import java.math.BigDecimal;

public class TicketIngresoDto {
    private Long idTicket;
    private Long idEvento;
    private String estadoFinanciero;
    private BigDecimal valorBruto;

    public TicketIngresoDto() {}

    public TicketIngresoDto(Long idTicket, Long idEvento, String estadoFinanciero, BigDecimal valorBruto) {
        this.idTicket = idTicket;
        this.idEvento = idEvento;
        this.estadoFinanciero = estadoFinanciero;
        this.valorBruto = valorBruto;
    }

    public Long getIdTicket() { return idTicket; }
    public void setIdTicket(Long idTicket) { this.idTicket = idTicket; }
    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getEstadoFinanciero() { return estadoFinanciero; }
    public void setEstadoFinanciero(String estadoFinanciero) { this.estadoFinanciero = estadoFinanciero; }
    public BigDecimal getValorBruto() { return valorBruto; }
    public void setValorBruto(BigDecimal valorBruto) { this.valorBruto = valorBruto; }
}