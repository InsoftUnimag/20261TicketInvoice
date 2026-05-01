# Implementation Plan: Calcular Distribución del Recaudo
**Date:** 30/04/2026  
**Spec:** `08_CalcularDistribucionDelRecaudo.md`

## Summary
Implementar un caso de uso que permita al administrador financiero calcular la distribución del recaudo de un evento finalizado para conocer los valores preliminares antes de determinar el tipo de liquidación final. Se debe aplicar comisión de plataforma, determinar pago al promotor, y registrar el resultado como "Distribución Preliminar".

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <1 minuto 52 segundos para eventos con hasta 500 tickets
- **Constraints:** Evento finalizado, datos completos, descuentos por cancelación y cortesías

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 08_CalcularDistribucionDelRecaudo.md
└── plan/
    └── 08_plan_CalcularDistribucionDelRecaudo.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── DistribucionRecaudo.java  (nuevo)
│   │   ├── ResumenVentasEvento.java (ya existe)
│   │   ├── ConfiguracionLiquidacion.java (ya existe)
│   │   └── ComisionRecinto.java      (ya existe)
│   └── repositories/
│       ├── DistribucionRecaudoRepository.java (nuevo, opcional)
│       ├── EventSnapshotRepository.java      (ya existe)
│       └── ConfiguracionLiquidacionRepository.java (ya existe)

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── CalcularDistribucionRecaudoUseCase.java (nuevo)
│       ├── ConsultarResumenVentasUseCase.java (ya existe, reusable)
│       └── DeterminarTipoLiquidacionUseCase.java (ya existe, relacionado)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── CalcularDistribucionRequest.java (nuevo)
│       │       └── response/
│       │           ├── CalcularDistribucionResponse.java (nuevo)
│       │           └── DistribucionPreliminarResponse.java (nuevo)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── DistribucionRecaudoDto.java (nuevo)
│                   ├── EventSnapshotDto.java     (ya existe)
│                   └── ConfiguracionLiquidacionDto.java (ya existe)
│   ├── interfaces/
│   │   └── api/
│   │       ├── CalcularDistribucionController.java (nuevo)
│   │       └── ConfiguracionLiquidacionController.java (ya existe, relacionado)
│   └── mappers/
│       ├── DistribucionRecaudoMapper.java (nuevo)
│       └── ResumenVentasMapper.java      (ya existe, reusable)

└── shared/                             ← CAPA 4: COMPARTIDO
    └── errors/
        ├── ErrorCode.java
        ├── BusinessException.java
        └── TechnicalException.java
```

**Structure Decision**: Arquitectura Hexagonal con distribución de paquetes:
- Domain: Entidades y puertos (interfaces)
- Application: Casos de uso (UseCases) que retornan Application DTOs
- Infrastructure: Adaptadores de entrada (REST), salida (DTOs), y mappers
- Shared: Errores comunes

## Phase 1: Setup
**Purpose:** Project initialization
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)
- [x] T006 Entidad ResumenVentasEvento.java (ya existe)
- [x] T007 Entidad ConfiguracionLiquidacion.java (ya existe)
- [x] T008 Puerto EventSnapshotRepository.java (ya existe)
- [x] T009 Puerto ConfiguracionLiquidacionRepository.java (ya existe)

### Nuevas áreas para Spec 08
- [ ] T010 Crear entidad `DistribucionRecaudo.java`
- [ ] T011 Definir puerto `DistribucionRecaudoRepository.java` (opcional)

## Phase 3: User Story 1 - Calcular Distribución del Recaudo
**Goal:** Calcular automáticamente el recaudo total bruto del evento, descontar tickets cancelados y cortesías, y generar valor neto preliminar.

**Independent Test:** Calcular el recaudo bruto teniendo en cuenta:
- Aplica comisión de plataforma
- Determina pago final al promotor
- Registra el resultado del cálculo de ingresos
- Determinar el valor de las comisiones acordadas para el evento
- Estado de la liquidación del evento

### Tests for User Story 1
- [ ] T012 Unit tests para CalcularDistribucionRecaudoUseCase
- [ ] T013 Integration tests para cálculo

### Implementation for User Story 1
- [ ] T014 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T015 [US1] Implementar CalcularDistribucionRecaudoUseCase (retorna DistribucionRecaudoDto)
- [ ] T016 [US1] Implementar CalcularDistribucionController (POST endpoint)
- [ ] T017 [US1] Implementar DistribucionRecaudoMapper (Application DTO → Response DTO)
- [ ] T018 [US1] Integrar manejo de errores
- [ ] T019 [US1] Tests unitarios

## Phase 4: User Story 2 - Condición cuando el recinto es teatro
**Goal:** Aplicar lógica especial cuando el tipo de recinto es teatro para el cálculo de distribución.

**Independent Test:** Dado que se registró un recinto de tipo teatro, cuando se calcula la distribución, el sistema debe calcular:
- El total bruto recaudado
- Calcular el valor neto preliminar
- Registrar el resultado como "Distribución Preliminar"
- Calcular el valor de las comisiones acordadas
- Registrar el resultado como "Total distribuible"
- Registrar estado de la liquidación

### Tests for User Story 2
- [ ] T020 Unit tests para lógica de teatro
- [ ] T021 Integration tests para cálculo con teatro

### Implementation for User Story 2
- [ ] T022 [US2] Actualizar Use Case para manejar tipo de recinto
- [ ] T023 [US2] Implementar lógica de descuentos (cancelados, cortesías)
- [ ] T024 [US2] Tests unitarios

## Phase 5: Edge Cases
**Purpose:** Manejo de casos especiales

### Tests for Edge Cases
- [ ] T025 Unit tests: Evento sin ventas (recaudo cero, estado "Sin recaudo")
- [ ] T026 Unit tests: Valores de comisión no definidos (bloquear y solicitar establecer)

### Implementation for Edge Cases
- [ ] T027 [EC] Validar evento finalizado antes de calcular
- [ ] T028 [EC] Manejar evento sin ventas
- [ ] T029 [EC] Manejar comisiones no definidas

## Phase 6: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T030 OpenAPI/Swagger documentation
- [ ] T031 Circuit Breaker (Resilience4j)
- [ ] T032 Performance optimization (<1:52 para 500 tickets)
- [ ] T033 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo + entidad DistribucionRecaudo
- **User Stories:** Depende de Foundational
- **Edge Cases:** Depende de User Stories
- **Polish:** Depende de todo lo anterior

### Within Each User Story
1. Entidades antes de repositorios
2. Interfaces de repositorio antes de implementaciones
3. Lógica de Use Case antes de Controller
4. Mappers después de Use Cases
5. Tests después de implementación

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- Los Use Cases retornan Application DTOs (de `infrastructure/adapter/output/external/dto/`)
- Los Controllers usan Mappers para convertir: Application DTO → Response DTO
- Request DTOs están en `infrastructure/adapter/input/rest/request/`
- Response DTOs están en `infrastructure/adapter/input/rest/response/`
- EventSnapshotDto ya existe en `infrastructure/adapter/output/external/dto/`
- Se debe registrar el estado de liquidación como "Preliminar"

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → CalcularDistribucionController → CalcularDistribucionRecaudoUseCase → Domain → Infrastructure
                                                                                    ↓
                                                           DistribucionRecaudoDto (output/external/dto)
                                                                                               ↓
                                                           Mapper: DistribucionRecaudoDto → CalcularDistribucionResponse
                                                                                               ↓
Cliente ← CalcularDistribucionResponse ← CalcularDistribucionController
```

**Ejemplo:**
1. Cliente envía POST `/api/v1/eventos/{id}/calcular-distribucion`
2. `CalcularDistribucionController` recibe el request
3. Llama a `CalcularDistribucionRecaudoUseCase.execute(eventoId)`
4. Use Case:
   - Obtiene resumen de ventas (reutiliza ConsultarResumenVentasUseCase)
   - Aplica comisión de plataforma
   - Descuenta tickets cancelados y cortesías
   - Calcula valores netos preliminares
   - Determina pago al promotor
   - Registra como "Distribución Preliminar"
5. Use Case retorna `DistribucionRecaudoDto`
6. Controller usa `DistribucionRecaudoMapper.toResponse(dto)` → `CalcularDistribucionResponse`
7. Cliente recibe la distribución calculada

---

## Cálculos Detallados

### Fórmula:
```
Total Bruto = Suma de ingresos validados
Descuentos = Cancelados + Cortesías (no generan ingreso)
Neto Preliminar = Total Bruto - Descuentos - Comisión Plataforma
Total Distribuible = Neto Preliminar - Comisiones acordadas
Estado = "Preliminar" (hasta ejecutar liquidación final)
```

### Registros Financieros:
- **Distribución Preliminar**: Valor neto antes de comisiones
- **Total Distribuible**: Valor final a distribuir
- **Estado de Liquidación**: "Preliminar", "Liquidado", "Sin recaudo"
