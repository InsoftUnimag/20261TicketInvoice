package com.ticketevents.liquidation.infrastructure.adapter.output.external.dto;

import java.util.List;

public class EstadoIngresoDto {
    private Long eventoId;
    private String nombreEvento;
    private List<RegistroIngresoDTO> registros;

    public EstadoIngresoDto() {}

    public EstadoIngresoDto(Long eventoId, String nombreEvento, List<RegistroIngresoDTO> registros) {
        this.eventoId = eventoId;
        this.nombreEvento = nombreEvento;
        this.registros = registros;
    }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public List<RegistroIngresoDTO> getRegistros() { return registros; }
    public void setRegistros(List<RegistroIngresoDTO> registros) { this.registros = registros; }

    public static class RegistroIngresoDTO {
        private Long idTicket;
        private Long idEvento;
        private String fechaHoraIngreso;
        private String estadoIngreso;
        private String tipoAcceso;

        public RegistroIngresoDTO() {}

        public RegistroIngresoDTO(Long idTicket, Long idEvento, String fechaHoraIngreso,
                                  String estadoIngreso, String tipoAcceso) {
            this.idTicket = idTicket;
            this.idEvento = idEvento;
            this.fechaHoraIngreso = fechaHoraIngreso;
            this.estadoIngreso = estadoIngreso;
            this.tipoAcceso = tipoAcceso;
        }

        public Long getIdTicket() { return idTicket; }
        public void setIdTicket(Long idTicket) { this.idTicket = idTicket; }
        public Long getIdEvento() { return idEvento; }
        public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
        public String getFechaHoraIngreso() { return fechaHoraIngreso; }
        public void setFechaHoraIngreso(String fechaHoraIngreso) { this.fechaHoraIngreso = fechaHoraIngreso; }
        public String getEstadoIngreso() { return estadoIngreso; }
        public void setEstadoIngreso(String estadoIngreso) { this.estadoIngreso = estadoIngreso; }
        public String getTipoAcceso() { return tipoAcceso; }
        public void setTipoAcceso(String tipoAcceso) { this.tipoAcceso = tipoAcceso; }
    }
}
