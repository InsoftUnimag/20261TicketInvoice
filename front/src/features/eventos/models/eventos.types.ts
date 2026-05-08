import type { components } from "@/openapi/generated";

// Re-export tipos generados desde OpenAPI
export type ResumenVentasResponse = components["schemas"]["ConsultarResumenVentasResponse"];
export type EstadoIngresoResponse = components["schemas"]["ConsultarEstadoIngresoResponse"];
export type IngresosResponse = components["schemas"]["ConsultarIngresosResponse"];

// Tipos extendidos para UI
export interface EventoContexto {
  eventoId: number;
  nombreEvento: string;
  estadoEvento: "CERRADO" | "EN_CURSO" | "PROGRAMADO";
}

export interface ResumenVentasUI extends ResumenVentasResponse {
  porcentajeAsistencia?: number; // Calculado: ticketsValidados / ticketsVendidos
}
