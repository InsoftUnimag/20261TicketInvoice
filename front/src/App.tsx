import { useState } from "react";
import { QueryClientProvider } from "@tanstack/react-query";
import { queryClient } from "@/core/api/query-client";
import { ErrorBoundary } from "@/core/errors/error-boundary";
import { AppLayout } from "@/shared/layout/AppLayout";
import { Feature01ConsultarResumenDeVentasPage } from "@/features/eventos/pages/feature-01ConsultarResumenDeVentas-page";
import type { ResumenEventoData } from "@/features/eventos/pages/feature-01ConsultarResumenDeVentas-page";
import { Card } from "@/shared/ui/card";
import { Button } from "@/shared/ui/button";

type Screen = "index" | "eventoAcciones" | "consultarResumenDeVentas";
type EstadoEvento = "No inicia" | "En curso" | "Finalizado";
type TipoEvento = "Concierto" | "Festival";

interface EventoMenu {
  id: string;
  nombre: string;
  fechaInicio: string;
  fechaFin: string;
  tipo: TipoEvento;
  recinto: string;
}

const eventos: EventoMenu[] = [
  {
    id: "EV-24021",
    nombre: "Festival Estereo Capital 2026",
    fechaInicio: "2026-02-21",
    fechaFin: "2026-02-23",
    tipo: "Festival",
    recinto: "Parque Simon Bolivar",
  },
  {
    id: "EV-24100",
    nombre: "Coldplay Music of the Spheres",
    fechaInicio: "2026-08-10",
    fechaFin: "2026-08-10",
    tipo: "Concierto",
    recinto: "Estadio El Campin",
  },
];

const resumenesPorEvento: Record<string, ResumenEventoData> = {
  "EV-24021": {
    snapshotId: "SNAP-24021",
    fechaEvento: "21/02/2026",
    estadoEvento: "Finalizado",
    metricas: {
      vendidos: "5,420",
      validados: "4,980",
      cancelados: "185",
      cortesias: "255",
      totalRecaudado: "$ 328,450,000",
    },
    rows: [
      { condicion: "Vendidos", descripcion: "Tickets emitidos con valor bruto asociado", tickets: "5,420", valor: "$ 337,700,000" },
      { condicion: "Validados", descripcion: "Tickets usados como referencia de asistencia y conciliacion", tickets: "4,980", valor: "$ 328,450,000" },
      { condicion: "Cancelados", descripcion: "Boletas reversadas y descontadas del consolidado", tickets: "185", valor: "- $ 9,250,000", danger: true },
      { condicion: "Cortesias", descripcion: "Tickets sin recaudo monetario", tickets: "255", valor: "$ 0" },
      { condicion: "Total usado para liquidacion", descripcion: "Base consolidada para distribucion del recaudo", tickets: "5,235", valor: "$ 328,450,000", total: true },
    ],
  },
  "EV-24100": {
    snapshotId: "SNAP-24100",
    fechaEvento: "10/08/2026",
    estadoEvento: "No inicia",
    metricas: {
      vendidos: "3,870",
      validados: "0",
      cancelados: "42",
      cortesias: "130",
      totalRecaudado: "$ 691,300,000",
    },
    rows: [
      { condicion: "Vendidos", descripcion: "Tickets emitidos con valor bruto asociado", tickets: "3,870", valor: "$ 699,500,000" },
      { condicion: "Validados", descripcion: "Tickets usados como referencia de asistencia y conciliacion", tickets: "0", valor: "$ 0" },
      { condicion: "Cancelados", descripcion: "Boletas reversadas y descontadas del consolidado", tickets: "42", valor: "- $ 8,200,000", danger: true },
      { condicion: "Cortesias", descripcion: "Tickets sin recaudo monetario", tickets: "130", valor: "$ 0" },
      { condicion: "Total usado para liquidacion", descripcion: "Base consolidada para distribucion del recaudo", tickets: "3,828", valor: "$ 691,300,000", total: true },
    ],
  },
};

const formatDate = (date: string) =>
  new Intl.DateTimeFormat("es-CO", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(new Date(`${date}T00:00:00`));

const getEstadoEvento = (fechaInicio: string, fechaFin: string): EstadoEvento => {
  const hoy = new Date();
  const inicio = new Date(`${fechaInicio}T00:00:00`);
  const fin = new Date(`${fechaFin}T23:59:59`);

  if (hoy < inicio) return "No inicia";
  if (hoy > fin) return "Finalizado";
  return "En curso";
};

const estadoClasses: Record<EstadoEvento, string> = {
  "No inicia": "bg-[#ece9f5] text-[#4f4474]",
  "En curso": "bg-[#deebff] text-[#1d4ed8]",
  "Finalizado": "bg-[#e5f6ea] text-[#166534]",
};

export default function App() {
  const [screen, setScreen] = useState<Screen>("index");
  const [eventoSeleccionado, setEventoSeleccionado] = useState<EventoMenu | null>(null);
  const resumenEventoSeleccionado = eventoSeleccionado ? resumenesPorEvento[eventoSeleccionado.id] : null;

  return (
    <QueryClientProvider client={queryClient}>
      <ErrorBoundary>
        <AppLayout>
          {screen === "index" && (
            <div className="px-8 md:px-12 py-10">
              <h1 className="text-4xl font-semibold text-[#1f1a37] mb-2">Eventos</h1>
              <p className="text-lg text-[#6f6990] mb-8">Selecciona un evento</p>

              <div className="grid gap-4">
                {eventos.map((evento) => {
                  const estado = getEstadoEvento(evento.fechaInicio, evento.fechaFin);
                  return (
                    <Card key={evento.id} className="p-5 border border-[#d7d1e9] bg-white">
                      <div className="flex items-start justify-between gap-4">
                        <div>
                          <h2 className="text-xl font-semibold text-[#1f1a37]">{evento.nombre}</h2>
                          <p className="text-sm text-[#6f6990] mt-1">ID unico: {evento.id}</p>
                        </div>
                        <span className={`px-3 py-1 rounded-full text-sm font-semibold ${estadoClasses[estado]}`}>{estado}</span>
                      </div>
                      <div className="mt-4 grid gap-2 text-sm text-[#4f4474] md:grid-cols-2">
                        <p><strong>Fecha de Inicio:</strong> {formatDate(evento.fechaInicio)}</p>
                        <p><strong>Fecha de Fin:</strong> {formatDate(evento.fechaFin)}</p>
                        <p><strong>Tipo:</strong> {evento.tipo}</p>
                        <p><strong>Recinto:</strong> {evento.recinto}</p>
                      </div>
                      <div className="mt-5">
                        <Button
                          className="bg-[#6351a0] hover:opacity-95"
                          onClick={() => {
                            setEventoSeleccionado(evento);
                            setScreen("eventoAcciones");
                          }}
                        >
                          Realizar acciones
                        </Button>
                      </div>
                    </Card>
                  );
                })}
              </div>
            </div>
          )}

          {screen === "eventoAcciones" && eventoSeleccionado && (
            <div className="px-8 md:px-12 py-10">
              <Button variant="ghost" className="mb-6 text-[#4f4474] hover:bg-[#d8d2e8]" onClick={() => setScreen("index")}>
                Volver a eventos
              </Button>

              <h1 className="text-3xl font-semibold text-[#1f1a37]">{eventoSeleccionado.nombre}</h1>
              <p className="text-[#6f6990] mt-2">
                ID: {eventoSeleccionado.id} | Tipo: {eventoSeleccionado.tipo} | Recinto: {eventoSeleccionado.recinto}
              </p>
              <p className="text-[#6f6990]">
                Inicio: {formatDate(eventoSeleccionado.fechaInicio)} | Fin: {formatDate(eventoSeleccionado.fechaFin)}
              </p>

              <div className="mt-8">
                <p className="text-lg text-[#6f6990] mb-4">Selecciona una accion</p>
                <div className="max-w-3xl grid gap-3">
                  <Button className="justify-start bg-[#6351a0] hover:opacity-95" onClick={() => setScreen("consultarResumenDeVentas")}>
                    Consultar resumen de ventas
                  </Button>
                  <Button variant="outline" className="justify-start">
                    Estado de ingreso (proximamente)
                  </Button>
                  <Button variant="outline" className="justify-start">
                    Ingresos de tickets (proximamente)
                  </Button>
                </div>
              </div>
            </div>
          )}

          {screen === "consultarResumenDeVentas" && eventoSeleccionado && resumenEventoSeleccionado && (
            <Feature01ConsultarResumenDeVentasPage
              onBack={() => setScreen("eventoAcciones")}
              evento={{ id: eventoSeleccionado.id, nombre: eventoSeleccionado.nombre }}
              resumen={resumenEventoSeleccionado}
            />
          )}
        </AppLayout>
      </ErrorBoundary>
    </QueryClientProvider>
  );
}
