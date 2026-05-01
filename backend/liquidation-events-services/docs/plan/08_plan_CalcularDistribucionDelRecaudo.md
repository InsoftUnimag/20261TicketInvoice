# Implementation Plan: Calcular Distribución del Recaudo
**Date:** 30/04/2026  
**Spec:** `08_CalcularDistribucionDelRecaudo.md`

## Summary
Implementar un caso de uso que permita al administrador financiero calcular la distribución del recaudo de un evento finalizado para conocer los valores preliminares antes de determinar el tipo de liquidación final.

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

## Phase 1: Setup (Shared Infrastructure)
**Purpose:** Project initialization
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational (Blocking Prerequisites)
**Purpose:** Core infrastructure that MUST be complete before ANY user story can be implemented

### Existing entities for Spec 08
- [x] T004 Entidad ResumenVentasEvento.java (ya existe)
- [x] T005 Entidad ConfiguracionLiquidacion.java (ya existe)
- [x] T006 Puerto EventSnapshotRepository.java (ya existe)
- [x] T007 Puerto ConfiguracionLiquidacionRepository.java (ya existe)

### New areas for Spec 08
- [ ] T008 Crear entidad `DistribucionRecaudo.java`
- [ ] T009 Definir puerto `DistribucionRecaudoRepository.java` (opcional)

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Calcular Distribución del Recaudo (Priority: P1)
**Goal:** Calcular automáticamente el recaudo total bruto del evento, descontar tickets cancelados y cortesías, y generar valor neto preliminar.

**Independent Test**: Calcular el recaudo bruto teniendo en cuenta:
- Aplica comisión de plataforma
- Determina pago final al promotor
- Registra el resultado del cálculo de ingresos
- Determinar el valor de las comisiones acordadas para el evento
- Estado de la liquidación del evento

### Tests for User Story 1
- [ ] T010 [P] [US1] Unit test para CalcularDistribucionRecaudoUseCase
- [ ] T011 [P] [US1] Integration test para cálculo

### Implementation for User Story 1
- [ ] T012 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T013 [US1] Implementar CalcularDistribucionRecaudoUseCase (retorna DistribucionRecaudoDto)
- [ ] T014 [US1] Implementar CalcularDistribucionController (POST endpoint)
- [ ] T015 [US1] Implementar DistribucionRecaudoMapper (Application DTO → Response DTO)
- [ ] T016 [US1] Integrar manejo de errores
- [ ] T017 [US1] Tests unitarios

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Condición cuando el recinto es teatro (Priority: P2)
**Goal:** Aplicar lógica especial cuando el tipo de recinto es teatro para el cálculo de distribución.

**Independent Test**: Dado que se registró un recinto de tipo teatro, cuando se calcula la distribución, el sistema debe calcular:
- El total bruto recaudado
- Calcular el valor neto preliminar
- Registrar el resultado como "Distribución Preliminar"
- Calcular el valor de las comisiones acordadas
- Registrar el resultado como "Total distribuible"
- Registrar estado de la liquidación

### Tests for User Story 2
- [ ] T018 [P] [US2] Unit test para lógica de teatro
- [ ] T019 [P] [US2] Integration test para cálculo con teatro

### Implementation for User Story 2
- [ ] T020 [US2] Actualizar Use Case para manejar tipo de recinto
- [ ] T021 [US2] Implementar lógica de descuentos (cancelados, cortesías)
- [ ] T022 [US2] Tests unitarios

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: Edge Cases
**Purpose:** Manejo de casos especiales

### Tests for Edge Cases
- [ ] T023 Unit tests: Evento sin ventas (recaudo cero, estado "Sin recaudo")
- [ ] T024 Unit tests: Valores de comisiones no definidos (bloquear y solicitar establecer)

### Implementation for Edge Cases
- [ ] T025 Validar evento finalizado antes de calcular
- [ ] T026 Manejar evento sin ventas
- [ ] T027 Manejar comisiones no definidas

**Checkpoint**: All user stories should now be independently functional

---

## Phase N: Polish & Cross-Cutting Concerns
**Purpose:** Improvements that affect multiple user stories
- [ ] T028 OpenAPI/Swagger documentation
- [ ] T029 Circuit Breaker (Resilience4j)
- [ ] T030 Performance optimization (<1:52 para 500 tickets)
- [ ] T031 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Edge Cases**: Depends on User Stories
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies
- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **Edge Cases**: Depend on User Stories

### Within Each User Story
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority
- Tests after implementation

---

## Notes
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests pass
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
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
