import { Card, CardContent, CardHeader, CardTitle } from "@/shared/ui";
import { Calculator, DollarSign, Ticket, Building2 } from "lucide-react";

const stats = [
  { label: "Eventos liquidados", value: "—", icon: Calculator },
  { label: "Total distribuido", value: "—", icon: DollarSign },
  { label: "Tickets procesados", value: "—", icon: Ticket },
  { label: "Recintos activos", value: "—", icon: Building2 },
];

export function DashboardPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <p className="text-muted-foreground text-sm mt-1">
          Módulo de Liquidación y Dispersión de Fondos
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.label}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">{stat.label}</CardTitle>
              <stat.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <p className="text-2xl font-bold">{stat.value}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Flujo de Liquidación</CardTitle>
        </CardHeader>
        <CardContent>
          <ol className="list-decimal list-inside space-y-2 text-sm text-muted-foreground">
            <li>Consultar resumen de ventas del evento</li>
            <li>Consultar estado de ingreso de tickets</li>
            <li>Consultar ingresos por tickets</li>
            <li>Determinar tipo de liquidación (Tarifa Plana / Reparto Ingresos)</li>
            <li>Registrar comisión del recinto</li>
            <li>Calcular distribución del recaudo</li>
            <li>Consultar distribución final</li>
          </ol>
        </CardContent>
      </Card>
    </div>
  );
}
