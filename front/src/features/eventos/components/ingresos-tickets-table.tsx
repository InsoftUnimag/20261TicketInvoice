import { Card } from "@/shared/ui/card";
import { Skeleton } from "@/shared/ui/skeleton";
import { Badge } from "@/shared/ui/badge";
import type { IngresosResponse } from "../models/eventos.types";

interface IngresosTicketsTableProps {
  data?: IngresosResponse;
  isLoading?: boolean;
  error?: Error | null;
}

export function IngresosTicketsTable({ data, isLoading, error }: IngresosTicketsTableProps) {
  if (error) {
    return (
      <Card className="p-6 border-red-200 bg-red-50">
        <p className="text-sm text-red-700">Error al cargar ingresos</p>
        <p className="text-xs text-red-600 mt-1">{error.message}</p>
      </Card>
    );
  }

  if (isLoading) {
    return (
      <Card className="p-6">
        <div className="space-y-3">
          <Skeleton className="h-4 w-1/4" />
          <Skeleton className="h-10" />
          <Skeleton className="h-10" />
          <Skeleton className="h-10" />
          <Skeleton className="h-10" />
        </div>
      </Card>
    );
  }

  if (!data) return null;

  const formatCurrency = (value?: number) => {
    if (!value) return "$0";
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
      maximumFractionDigits: 0,
    }).format(value);
  };

  const estadoData = [
    {
      estado: "VALIDADO (Check-in)",
      cantidad: data.totalTicketsValidados,
      badge: "success",
    },
    {
      estado: "VENDIDO (No Asistió)",
      cantidad: data.totalNoAsistieron,
      badge: "default",
    },
    {
      estado: "CANCELADO",
      cantidad: data.totalTicketsCancelados,
      badge: "destructive",
    },
    {
      estado: "CORTESIA",
      cantidad: data.totalCortesias,
      badge: "outline",
    },
  ];

  return (
    <Card className="p-6">
      <div className="space-y-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">Ingresos de Tickets - Evento {data.eventoId}</h3>
          {data.hasInconsistencies && (
            <div className="mt-2 p-2 bg-yellow-50 border border-yellow-200 rounded text-sm text-yellow-700">
              ⚠️ Se detectaron tickets sin estado definido
            </div>
          )}
        </div>

        {/* Tabla de estados */}
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Estado Financiero</th>
                <th className="text-right py-3 px-4 font-semibold text-gray-700">Cantidad</th>
                <th className="text-right py-3 px-4 font-semibold text-gray-700">Porcentaje</th>
              </tr>
            </thead>
            <tbody>
              {estadoData.map((row) => {
                const total = data.totalTicketsVendidos ?? 0;
                const porcentaje = total > 0 ? ((row.cantidad ?? 0) / total) * 100 : 0;

                return (
                  <tr key={row.estado} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="py-3 px-4">
                      <div className="flex items-center gap-2">
                        <Badge variant={row.badge as any}>{row.estado}</Badge>
                      </div>
                    </td>
                    <td className="py-3 px-4 text-right font-semibold text-gray-900">
                      {row.cantidad ?? 0}
                    </td>
                    <td className="py-3 px-4 text-right text-gray-600">{porcentaje.toFixed(1)}%</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>

        {/* Resumen financiero */}
        <div className="grid grid-cols-2 gap-4 mt-4">
          <div className="bg-blue-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Total Tickets Vendidos</p>
            <p className="text-2xl font-bold text-blue-600 mt-1">{data.totalTicketsVendidos}</p>
          </div>
          <div className="bg-green-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Recaudo Bruto</p>
            <p className="text-2xl font-bold text-green-600 mt-1">{formatCurrency(data.totalRecaudoBruto)}</p>
          </div>
        </div>
      </div>
    </Card>
  );
}
