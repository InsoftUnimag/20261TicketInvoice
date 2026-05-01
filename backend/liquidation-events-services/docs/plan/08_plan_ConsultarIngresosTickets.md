# Implementation Plan: Consultar Ingresos Tickets
**Date:** 30/04/2026
**Spec:** `08_ConsultarIngresosTickets.md`

## Summary
Implementar un caso de uso y endpoint que permita consultar los ingresos de tickets para un evento específico. Se debe retornar la lista de tickets con su estado financiero y valor bruto, utilizando Arquitectura Hexagonal con Application DTOs.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Validación de existencia de evento, datos de tickets disponibles

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 08_ConsultarIngresosTickets.md
└── plan/
    └── 08_plan_ConsultarIngresosTickets.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/
│   ├── entities/
│   │   ├── TicketIngreso.java          (entidad, nuevo)
│   │   └── IngresoTicket.java          (entidad, nuevo)
│   └── repositories/
│       └── IngresosTicketsRepository.java (Puerto/Port, nuevo)

├── application/
│   └── usecase/
│       └── ConsultarIngresosTicketsUseCase.java (nuevo)

├── infrastructure/
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarIngresosTicketsRequest.java (nuevo)
│       │       └── response/
│       │           ├── ConsultarIngresosTicketsResponse.java (nuevo)
│       │           └── TicketIngresoResponse.java (nuevo)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── TicketIngresoDto.java (ya existe)
│                   └── IngresosTicketsDto.java (nuevo)
│   ├── external/
│   │   └── IngresosTicketsApiClient.java (nuevo)
│   ├── interfaces/
│   │   └── api/
│   │       └── IngresosTicketsController.java (nuevo)
│   └── mappers/
│       ├── IngresosTicketsMapper.java (nuevo)
│       └── TicketIngresoMapper.java (nuevo)

└── shared/
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

### Nuevas areas para Spec 08
- [ ] T006 Crear entidad `TicketIngreso.java`
- [ ] T007 Crear entidad `IngresoTicket.java`
- [ ] T008 Definir puerto `IngresosTicketsRepository.java`

## Phase 3: User Story 1 - Consultar Ingresos Tickets
**Goal:** Permitir consultar los ingresos de tickets para un evento, retornando lista con estado financiero y valor bruto.

**Independent Test:** Invocar `GET /api/v1/eventos/{id}/ingresos-tickets`. Validar respuesta `200` con lista de tickets. Verificar `404` para evento inexistente.

### Tests for User Story 1
- [ ] T009 Unit tests para ConsultarIngresosTicketsUseCase
- [ ] T010 Integration tests para endpoint

### Implementation for User Story 1
- [ ] T011 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T012 [US1] Implementar IngresosTicketsApiClient (o usar existente)
- [ ] T013 [US1] Implementar ConsultarIngresosTicketsUseCase (retorna IngresosTicketsDto)
- [ ] T014 [US1] Implementar IngresosTicketsController (GET endpoint)
- [ ] T015 [US1] Implementar IngresosTicketsMapper (Application DTO → Response DTO)
- [ ] T016 [US1] Integrar manejo de errores
- [ ] T017 [US1] Tests unitarios

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T018 OpenAPI/Swagger documentation
- [ ] T019 Circuit Breaker (Resilience4j)
- [ ] T020 Tests de concurrencia
- [ ] T021 Validaciones más estrictas
- [ ] T022 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo + areas nuevas
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

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
- TicketIngresoDto ya existe en `infrastructure/adapter/output/external/dto/`

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → IngresosTicketsController → ConsultarIngresosTicketsUseCase → Domain → Infrastructure
                                                                  ↓
                                    IngresosTicketsDto (output/external/dto)
                                                                ↓
                                    Mapper: IngresosTicketsDto → ConsultarIngresosTicketsResponse
                                                                ↓
Cliente ← ConsultarIngresosTicketsResponse ← IngresosTicketsController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/eventos/1/ingresos-tickets`
2. `IngresosTicketsController` recibe el request
3. Llama a `ConsultarIngresosTicketsUseCase.execute(eventoId)`
4. Use Case retorna `IngresosTicketsDto` (o lista de `TicketIngresoDto`)
5. Controller usa `IngresosTicketsMapper.toResponse(dto)` → `ConsultarIngresosTicketsResponse`
6. Cliente recibe lista de tickets con estado financiero y valor bruto
