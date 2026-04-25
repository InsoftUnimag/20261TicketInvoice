# Implementation Plan: Consultar ingresos de tickets por evento
**Date:** 24/04/2026  
**Spec:** `Consultar_ingresos_tickets.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura que permita consultar el resumen financiero de ingresos por tickets asociados a un evento. Se calculará el recaudo bruto, se desglosarán los tickets por estado financiero (`Validado`, `NoAsistio`, `Cortesia`, `Cancelado`) y se aplicarán reglas de inmutabilidad. Se utilizará Arquitectura Hexagonal garantizando trazabilidad financiera.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <200ms p95 para agregaciones financieras
- **Constraints:** Datos inmutables post-venta, validación estricta de `estadoFinanciero`
- **Scale/Scope:** Consultas on-demand por evento

## Project Structure

### Documentation (this feature)

```
docs/
├── specs/
│   └── 04_ConsultarIngresosTickets.md
└── plan/
    └── 04_plan_ConsultarIngresosTickets.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── Ticket.java               (nuevo)
│   │   ├── EstadoFinanciero.java      (enum, nuevo)
│   │   └── IngresosEvento.java      (Value Object, nuevo)
│   └── repositories/
│       └── IngresosConsultaRepository.java (Puerto/Port, nuevo)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── ConsultarIngresosTicketsUseCase.java (nuevo)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarIngresosRequest.java
│       │       └── response/
│       │           ├── ConsultarIngresosResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── TicketIngresoDto.java
│   ├── external/
│   │   ├── IngresosApiClient.java
│   │   └── MockIngresosRepository.java (mock para pruebas)
│   ├── interfaces/
│   │   └── api/
│   │       ├── IngresosTicketsController.java
│   │       └── GlobalExceptionHandler.java
│   └── config/
│       └── RestClientConfig.java

└── shared/                             ← CAPA 4: COMPARTIDO

    └── errors/
        ├── ErrorCode.java
        ├── BusinessException.java
        └── TechnicalException.java
```

**Structure Decision**: Arquitectura Hexagonal con distribución de paquetes:
- Domain: Entidades y puertos (interfaces)
- Application: Casos de uso (UseCases)
- Infrastructure: Adaptadores de entrada (REST), salida (API externa), y configuración
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

### Nuevas tareas para Spec 04
- [ ] T006 Crear entidad `Ticket.java` en domain/entities/
- [ ] T007 Crear enum `EstadoFinanciero.java` en domain/entities/
- [ ] T008 Crear Value Object `IngresosEvento.java` en domain/entities/
- [ ] T009 Definir puerto `IngresosConsultaRepository.java` en domain/repositories/

## Phase 3: User Story 1 - Consultar ingresos tickets
**Goal:** Exponer endpoint de solo lectura para obtener el desglose financiero de tickets por evento, calculando recaudo bruto y validando integridad de estados.

**Independent Test:** Invocar `GET /api/v1/eventos/{id}/ingresos` con ID válido. Validar JSON con conteos por `EstadoFinanciero` y `totalRecaudoBruto`. Verificar `404` para evento inexistente, `200` con contadores en `0` para evento sin ventas.

### Tests for User Story 1
- [ ] T010 Unit tests para ConsultarIngresosTicketsUseCase

### Implementation for User Story 1
- [ ] T011 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T012 [US1] Crear TicketIngresoDto en infrastructure/adapter/output/external/dto/
- [ ] T013 [US1] Implementar IngresosApiClient en infrastructure/external/
- [ ] T014 [US1] Implementar MockIngresosRepository
- [ ] T015 [US1] Implementar ConsultarIngresosTicketsUseCase
- [ ] T016 [US1] Implementar IngresosTicketsController
- [ ] T017 [US1] Integrar manejo de errores
- [ ] T018 [US1] Tests unitarios

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T019 OpenAPI/Swagger
- [ ] T020 Circuit Breaker (Resilience4j)
- [ ] T021 Tests de concurrencia
- [ ] T022 Validaciones más estrictas
- [ ] T023 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo + tareas nuevas
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

### Within Each User Story
1. Entidades & Enums antes de repositorios
2. Interfaces de repositorio antes de implementaciones
3. Lógica de Use Case antes de Controller
4. Manejo de errores después de lógica de negocio
5. Tests después de implementación

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- FR-006: El UseCase opera solo en modo lectura. No se exponen métodos POST/PUT/DELETE
- Edge Case "Sin estado": Si `estadoFinanciero` es null, marcar `hasInconsistencies = true` en respuesta
- Edge Case "Sin ventas": Retorna 200 OK con contadores en 0 y totalRecaudoBruto = 0
- Mock disponible en MockIngresosRepository para pruebas sin servicio externo