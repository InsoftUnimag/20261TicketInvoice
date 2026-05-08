# Frontend — Módulo 3: Liquidación y Dispersión de Fondos

## Stack

| Capa | Tecnología |
|------|-----------|
| Framework | React 19 + TypeScript |
| Build | Vite 8 |
| HTTP | ky + TanStack Query v5 |
| UI | shadcn/ui + Tailwind CSS v4 |
| Forms | React Hook Form + Zod |
| State | TanStack Query (server) + Zustand (client) |
| Types | openapi-typescript (desde Swagger) |
| Charts | Recharts |

## Arquitectura: Feature-based Clean Architecture

```
src/
├── core/              ← Infraestructura compartida
│   ├── api/           ← ky client, QueryClient config
│   ├── errors/        ← ApiError, ErrorBoundary
│   ├── router/        ← TanStack Router (pendiente)
│   └── config/        ← env vars
├── features/          ← Módulos por dominio
│   ├── eventos/       ← Resumen ventas, estado ingreso, ingresos tickets
│   ├── recintos/      ← Consulta recinto + comisión
│   ├── liquidacion/   ← Config liquidación, comisión, cálculo, distribución
│   └── dashboard/     ← KPIs globales
│       ├── api/       ← TanStack Query hooks
│       ├── models/    ← Tipos (extends openapi-typescript)
│       ├── components/← UI específica del feature
│       └── pages/     ← Páginas (vistas completas)
└── shared/            ← Componentes reutilizables
    ├── ui/            ← shadcn/ui (Button, Card, etc.)
    ├── layout/        ← AppLayout, Sidebar
    └── lib/           ← utils (cn, formatCurrency, etc.)
```

## Integración con Backend Spring Boot

- **Base URL**: `/api/v1` (proxy Vite → `localhost:8080`)
- **Tipos**: Generados con `openapi-typescript` desde `http://localhost:8080/api-docs`
- **HTTP Client**: `ky` en `src/core/api/client.ts` con manejo de errores → `ApiError`
- **Caché**: TanStack Query con `staleTime: 30s`, retry 3 (solo 5xx)

## Endpoints del Backend

| Método | Endpoint | Feature | Caso de Uso |
|--------|----------|---------|-------------|
| GET | /api/v1/eventos/{id}/resumen-ventas | eventos | Resumen ventas cerradas |
| GET | /api/v1/eventos/{id}/estado-ingreso | eventos | Estado check-in tickets |
| GET | /api/v1/eventos/{id}/ingresos | eventos | Ingresos financieros |
| GET | /api/v1/eventos/{id}/configuracion-liquidacion | liquidacion | Ver config liquidación |
| POST | /api/v1/eventos/{id}/configuracion-liquidacion | liquidacion | Crear config liquidación |
| GET | /api/v1/recintos/{id} | recintos | Consultar recinto |
| GET | /api/v1/recintos/{id}/comision | liquidacion | Ver comisión recinto |
| POST | /api/v1/recintos/{id}/comision | liquidacion | Registrar comisión |
| POST | /api/v1/eventos/{id}/calcular-distribucion | liquidacion | Calcular distribución |
| GET | /api/v1/eventos/{id}/distribucion-recaudo | liquidacion | Consultar distribución |

## Flujo del Negocio (orden secuencial)

1. Resumen de ventas → 2. Estado de ingreso → 3. Ingresos tickets → 4. Config tipo liquidación → 5. Registrar comisión recinto → 6. Calcular distribución → 7. Consultar distribución

## Reglas de Dominio Clave

- **Tipo Liquidación**: TARIFA_PLANA (monto fijo) o REPARTO_INGRESOS (porcentaje)
- **Tipo Recinto**: ESTADIO o TEATRO → determina tasa comisión
- **Tipo Comisión**: PORCENTAJE (0.0-1.0) o VALOR_FIJO
- **Estado Financiero Ticket**: Validado / NoAsistio / Cortesia / Cancelado
- **Matriz liquidación**: ticket validado → 90% promotor, 10% plataforma; no asistió → 100%; cortesía → 0%; cancelado → -100%

## Próximos Pasos

1. `npm run dev` para iniciar
2. Descargar OpenAPI spec y generar tipos: `npx openapi-typescript http://localhost:8080/api-docs -o openapi/generated.ts`
3. Implementar features en orden: eventos → recintos → liquidacion → dashboard
4. Reemplazar el App.tsx actual (vista única) con TanStack Router cuando haya múltiples páginas
