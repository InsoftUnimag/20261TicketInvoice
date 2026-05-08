import { Card } from "@/shared/ui/card";
import { Badge } from "@/shared/ui/badge";
import { Skeleton } from "@/shared/ui/skeleton";
import type { ResumenVentasResponse } from "../models/eventos.types";

interface ResumenVentasCardProps {
  data?: ResumenVentasResponse;
  isLoading?: boolean;
  error?: Error | null;
}

export function ResumenVentasCard({ data, isLoading, error }: ResumenVentasCardProps) {
  if (error) {
    return (
      <Card className="p-6 border-red-200 bg-red-50">
        <p className="text-sm text-red-700">Error al cargar resumen de ventas</p>
        <p className="text-xs text-red-600 mt-1">{error.message}</p>
      </Card>
    );
  }

  if (isLoading) {
    return (
      <Card className="p-6">
        <div className="space-y-4">
          <Skeleton className="h-4 w-1/3" />
          <Skeleton className="h-8 w-1/2" />
          <div className="grid grid-cols-2 gap-4">
            <Skeleton className="h-10" />
            <Skeleton className="h-10" />
          </div>
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

  return (
    <Card className="p-6">
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900">{data.nombreEvento}</h3>
          <div className="flex items-center gap-2 mt-2">
            <Badge
              variant={
                data.estadoEvento === "CERRADO"
                  ? "secondary"
                  : data.estadoEvento === "EN_CURSO"
                    ? "default"
                    : "outline"
              }
            >
              {data.estadoEvento}
            </Badge>
            <span className="text-sm text-gray-600">ID: {data.eventoId}</span>
          </div>
        </div>

        {/* KPIs principales */}
        <div className="grid grid-cols-2 gap-4">
          <div className="bg-blue-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Tickets Vendidos</p>
            <p className="text-2xl font-bold text-blue-600 mt-1">{data.totalTicketsVendidos}</p>
          </div>
          <div className="bg-green-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Recaudo Bruto</p>
            <p className="text-2xl font-bold text-green-600 mt-1">{formatCurrency(data.totalRecaudoBruto)}</p>
          </div>
        </div>

        {/* Desglose por condición */}
        {data.ticketsPorCondicion && Object.keys(data.ticketsPorCondicion).length > 0 && (
          <div className="space-y-2">
            <p className="text-sm font-semibold text-gray-700">Desglose de Condiciones</p>
            <div className="space-y-2">
              {Object.entries(data.ticketsPorCondicion).map(([condicion, cantidad]) => (
                <div key={condicion} className="flex justify-between items-center text-sm">
                  <span className="text-gray-600">{condicion}</span>
                  <div className="flex gap-4">
                    <span className="font-semibold">{cantidad} tickets</span>
                    <span className="text-gray-500">
                      {formatCurrency(data.recaudoPorCondicion?.[condicion] ?? 0)}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </Card>
  );
}
