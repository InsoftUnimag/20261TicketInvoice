import { useState } from "react";
import { useResumenVentas, useEstadoIngreso, useIngresosTickets } from "../api/eventos.api";
import { ResumenVentasCard } from "../components/resumen-ventas-card";
import { EstadoIngresoTable } from "../components/estado-ingreso-table";
import { IngresosTicketsTable } from "../components/ingresos-tickets-table";
import { Button } from "@/shared/ui/button";
import { ChevronRight } from "lucide-react";

export function EventoDashboardPage() {
  const [eventoId, setEventoId] = useState<number>(1);
  const [inputValue, setInputValue] = useState("1");

  const { data: resumenData, isLoading: resumenLoading, error: resumenError } = useResumenVentas(eventoId);
  const { data: estadoData, isLoading: estadoLoading, error: estadoError } = useEstadoIngreso(eventoId);
  const { data: ingresosData, isLoading: ingresosLoading, error: ingresosError } = useIngresosTickets(eventoId);

  const handleLoadEvento = () => {
    const id = parseInt(inputValue, 10);
    if (!isNaN(id) && id > 0) {
      setEventoId(id);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      handleLoadEvento();
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-purple-50 to-blue-50 border-b border-purple-100 px-8 py-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-bold text-gray-900">Liquidación de Eventos</h1>
              <p className="text-gray-600 mt-2">Paso 1: Resumen de ventas • Estado de ingresos • Ingresos de tickets</p>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-7xl mx-auto px-8 py-8 space-y-8">
        {/* Search Section */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <label className="block text-sm font-semibold text-gray-900 mb-4">Cargar datos del evento</label>
          <div className="flex gap-3">
            <div className="flex-1 relative">
              <input
                type="number"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Ingresa el ID del evento"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent text-gray-900 placeholder-gray-500"
              />
            </div>
            <Button 
              onClick={handleLoadEvento}
              className="px-8 bg-purple-600 hover:bg-purple-700 text-white font-medium rounded-lg"
            >
              Cargar
            </Button>
          </div>
          <p className="text-xs text-gray-500 mt-3">
            ID del evento actual: <span className="font-semibold text-gray-700">{eventoId}</span>
          </p>
        </div>

        {/* Main Content Grid */}
        <div className="space-y-8">
          {/* Step 1: Resumen de Ventas */}
          <section>
            <div className="flex items-center gap-3 mb-4">
              <div className="w-8 h-8 rounded-full bg-purple-600 text-white flex items-center justify-center text-sm font-bold">
                1
              </div>
              <h2 className="text-2xl font-bold text-gray-900">Resumen de Ventas</h2>
            </div>
            <ResumenVentasCard data={resumenData} isLoading={resumenLoading} error={resumenError} />
          </section>

          {/* Step 2: Estado de Ingreso */}
          <section>
            <div className="flex items-center gap-3 mb-4">
              <div className="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center text-sm font-bold">
                2
              </div>
              <h2 className="text-2xl font-bold text-gray-900">Estado de Ingreso (Check-in)</h2>
            </div>
            <EstadoIngresoTable data={estadoData} isLoading={estadoLoading} error={estadoError} />
          </section>

          {/* Step 3: Ingresos de Tickets */}
          <section>
            <div className="flex items-center gap-3 mb-4">
              <div className="w-8 h-8 rounded-full bg-green-600 text-white flex items-center justify-center text-sm font-bold">
                3
              </div>
              <h2 className="text-2xl font-bold text-gray-900">Ingresos de Tickets</h2>
            </div>
            <IngresosTicketsTable data={ingresosData} isLoading={ingresosLoading} error={ingresosError} />
          </section>

          {/* Next Steps */}
          <div className="bg-blue-50 border border-blue-200 rounded-xl p-6 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold text-gray-900">Próximo paso: Configurar liquidación</h3>
              <p className="text-sm text-gray-600 mt-1">Continúa con la configuración del tipo de liquidación del evento</p>
            </div>
            <Button className="bg-blue-600 hover:bg-blue-700 text-white font-medium flex items-center gap-2">
              Continuar
              <ChevronRight className="w-4 h-4" />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
