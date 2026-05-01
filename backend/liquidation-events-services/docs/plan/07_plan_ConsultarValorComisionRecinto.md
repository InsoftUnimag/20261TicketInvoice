# Implementation Plan: Consultar Valor Comisión Recinto
**Date:** 30/04/2026  
**Spec:** `07_ConsultarValorComisionRecinto.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador de recinto consultar el valor de la comisión asociada al recinto, para verificar que se registraron las condiciones económicas previamente acordadas.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Validación de existencia de recinto, comisión configurada previamente

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 07_ConsultarValorComisionRecinto.md
└── plan/
    └── 07_plan_ConsultarValorComisionRecinto.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── ComisionRecinto.java      (ya existe)
│   │   └── Recinto.java              (ya existe)
│   └── repositories/
│       ├── ComisionRecintoRepository.java (ya existe)
│       └── RecintoRepository.java        (ya existe)

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── ConsultarValorComisionRecintoUseCase.java (nuevo)
│       └── RegistrarComisionRecintoUseCase.java (ya existe, relacionado)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarValorComisionRecintoRequest.java (nuevo, opcional)
│       │       └── response/
│       │           ├── ConsultarValorComisionRecintoResponse.java (nuevo)
│       │           └── ComisionRecintoResponse.java (ya existe, reusable)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── ComisionRecintoDto.java (ya existe)
│                   └── RecintoDto.java        (ya existe)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ValorComisionRecintoController.java (nuevo)
│   │       └── ComisionRecintoController.java  (ya existe, relacionado)
│   └── mappers/
│       ├── ValorComisionRecintoMapper.java (nuevo)
│       └── ComisionRecintoMapper.java      (ya existe, reusable)

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

### Existing entities for Spec 07
- [x] T004 Entidad ComisionRecinto.java (ya existe)
- [x] T005 Entidad Recinto.java (ya existe)
- [x] T006 Puerto ComisionRecintoRepository.java (ya existe)
- [x] T007 Puerto RecintoRepository.java (ya existe)

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Consultar valor comisión recinto (Priority: P1)
**Goal:** Permitir al administrador de recinto consultar el valor de la comisión asociada al recinto.

**Independent Test**: Consultar un recinto que tenga una comisión configurada previamente y validar que el sistema muestre correctamente el porcentaje o valor de comisión asociado.

### Tests for User Story 1
- [ ] T008 [P] [US1] Unit test para ConsultarValorComisionRecintoUseCase
- [ ] T009 [P] [US1] Integration test para endpoint

### Implementation for User Story 1
- [ ] T010 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T011 [US1] Implementar ConsultarValorComisionRecintoUseCase (retorna ComisionRecintoDto)
- [ ] T012 [US1] Implementar ValorComisionRecintoController (GET endpoint)
- [ ] T013 [US1] Implementar ValorComisionRecintoMapper (Application DTO → Response DTO)
- [ ] T014 [US1] Integrar manejo de errores
- [ ] T015 [US1] Tests unitarios

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Recinto sin comisión registrada (Priority: P2)
**Goal:** Manejar el caso cuando se consulta la comisión de un recinto que no tiene comisión registrada.

**Independent Test**: Dado que existe un recinto registrado en el sistema. Cuando el administrador consulta la comisión del recinto y no existe una comisión configurada, entonces el sistema informa que el recinto no tiene una comisión registrada.

### Tests for User Story 2
- [ ] T016 [P] [US2] Unit test para caso sin comisión
- [ ] T017 [P] [US2] Integration test para error controlado

### Implementation for User Story 2
- [ ] T018 [US2] Actualizar Use Case para manejar caso sin comisión
- [ ] T019 [US2] Actualizar Response para incluir mensaje de "sin comisión"
- [ ] T020 [US2] Tests unitarios

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Recinto no existe (Priority: P3)
**Goal:** Manejar el caso cuando se intenta consultar la comisión de un recinto que no existe.

**Independent Test**: Dado que el recinto no existe, el sistema debe mostrar un mensaje de error indicando que el recinto no está registrado.

### Tests for User Story 3
- [ ] T021 [P] [US3] Unit test para recinto inexistente
- [ ] T022 [P] [US3] Integration test para error 404

### Implementation for User Story 3
- [ ] T023 [US3] Validar existencia de recinto antes de consultar
- [ ] T024 [US3] Retornar error controlado (404)
- [ ] T025 [US3] Tests unitarios

**Checkpoint**: All user stories should now be independently functional

---

## Phase N: Polish & Cross-Cutting Concerns
**Purpose:** Improvements that affect multiple user stories
- [ ] T026 OpenAPI/Swagger documentation
- [ ] T027 Circuit Breaker (Resilience4j)
- [ ] T028 Tests de concurrencia
- [ ] T029 Validaciones más estrictas
- [ ] T030 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies
- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable

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
- ComisionRecintoDto ya existe en `infrastructure/adapter/output/external/dto/`

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → ValorComisionRecintoController → ConsultarValorComisionRecintoUseCase → Domain → Infrastructure
                                                                             ↓
                                                    ComisionRecintoDto (output/external/dto)
                                                                                        ↓
                                                    Mapper: ComisionRecintoDto → ConsultarValorComisionRecintoResponse
                                                                                        ↓
Cliente ← ConsultarValorComisionRecintoResponse ← ValorComisionRecintoController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/recintos/{id}/comision`
2. `ValorComisionRecintoController` recibe el request
3. Llama a `ConsultarValorComisionRecintoUseCase.execute(recintoId)`
4. Use Case retorna `ComisionRecintoDto` (o null si no tiene)
5. Controller usa `ValorComisionRecintoMapper.toResponse(dto)` → `ConsultarValorComisionRecintoResponse`
6. Cliente recibe el valor de comisión o mensaje de "sin comisión"

---

## Diferencia con RegistrarComisionRecintoUseCase (ya implementado)

| Característica | RegistrarComisionRecintoUseCase | ConsultarValorComisionRecintoUseCase |
|----------------|--------------------------------------|-------------------------------------------|
| Endpoint | `POST /api/v1/recintos/{id}/comision` | `GET /api/v1/recintos/{id}/comision` |
| Propósito | Registrar una nueva comisión | Consultar comisión existente |
| Retorno | `ComisionRecintoDto` | `ComisionRecintoDto` (o null) |
| Use Case existente | Sí (ya implementado) | No (nuevo) |
| Controller existente | Sí (ComisionRecintoController) | No (ValorComisionRecintoController) |
| Mapper existente | Sí (ComisionRecintoMapper) | Sí (reusable) |
