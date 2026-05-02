# Implementation Plan: Consultar Distribución del Recaudo

**Date**: 30/04/2026  
**Spec**: `docs/specs/09_ConsultarDistribucionDelRecaudo.md`

## Summary

Implementar un caso de uso y endpoint GET que permita al administrador financiero consultar la distribución del recaudo de un evento en estado "Liquidado", mostrando el detalle de montos distribuidos al promotor, comisión de plataforma, y estado de la liquidación.

## Technical Context

**Language/Version**: Java 21 LTS  
**Primary Dependencies**: Spring Boot 4.0.5, Spring Web, JUnit 5, Mockito, Jackson  
**Storage**: PostgreSQL (configurado)  
**Testing**: JUnit 5, Mockito  
**Target Platform**: Backend Service (Linux/Containerized)  
**Project Type**: Single Backend Service (Hexagonal Architecture)  
**Performance Goals**: <100ms p95 en consulta  
**Constraints**: Evento debe estar en estado "Liquidado", datos de distribución previamente calculados  
**Scale/Scope**: Eventos finalizados con distribución registrada

## Project Structure

### Documentation (this feature)

```text
docs/
├── specs/
│   └── 09_ConsultarDistribucionDelRecaudo.md
└── plan/
    └── 09_plan_ConsultarDistribucionDelRecaudo.md
```

### Source Code (repository root)

```text
src/main/java/com/ticketevents/liquidation/
├── domain/
│   ├── entities/
│   │   └── DistribucionRecaudo.java (extendida con campos de liquidacion)
│   └── repositories/
│       └── DistribucionRecaudoRepository.java (ya existe de Spec 08)
├── application/
│   └── usecase/
│       └── ConsultarDistribucionRecaudoUseCase.java
├── infrastructure/
│   ├── adapter/
│   │   ├── input/
│   │   │   └── rest/
│   │   │       └── response/
│   │   │           └── ConsultarDistribucionResponse.java
│   │   └── output/
│   │       └── external/
│   │           └── dto/
│   │               └── DistribucionRecaudoDto.java (ya existe de Spec 08)
│   ├── interfaces/
│   │   └── api/
│   │       └── ConsultarDistribucionController.java
│   └── mappers/
│       └── ConsultarDistribucionMapper.java
└── shared/
    └── errors/
        ├── ErrorCode.java
        ├── BusinessException.java
        └── TechnicalException.java

src/test/java/com/ticketevents/liquidation/
└── application/
    └── usecase/
        └── ConsultarDistribucionRecaudoUseCaseTest.java
```

**Structure Decision**: Arquitectura Hexagonal con distribución de paquetes por capas:
- `domain/`: Entidades y puertos
- `application/`: Casos de uso
- `infrastructure/`: Adaptadores de entrada (REST), salida (DTOs), mappers
- `shared/`: Errores compartidos
- Tests en `src/test/` siguiendo misma estructura

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create project structure per implementation plan
- [x] T002 Initialize Java 21 project with Spring Boot 4.0.5 dependencies
- [x] T003 Configure linting and formatting tools

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T004 Entidad DistribucionRecaudo.java (ya existe de Spec 08)
- [x] T005 Puerto DistribucionRecaudoRepository.java (ya existe de Spec 08)
- [x] T006 DistribucionRecaudoDto.java (ya existe de Spec 08)
- [x] T007 MockDistribucionRecaudoRepository.java (ya existe de Spec 08)

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Consultar Distribución del Recaudo (Priority: P1)

**Goal**: Permitir al administrador financiero consultar la distribución del recaudo de un evento en estado "Liquidado", mostrando total pago al promotor, comisión de plataforma, y estado de liquidación.

**Independent Test**: Dado un evento en estado "Liquidado" con distribución registrada, cuando se consulta la distribución, entonces se devuelve el detalle de montos distribuidos.

### Tests for User Story 1

- [ ] T008 [P] [US1] Unit test para ConsultarDistribucionRecaudoUseCase en src/test/java/com/ticketevents/liquidation/application/usecase/ConsultarDistribucionRecaudoUseCaseTest.java

### Implementation for User Story 1

- [ ] T009 [P] [US1] Agregar error code DISTRIBUTION_NOT_FOUND en ErrorCode.java
- [ ] T010 [P] [US1] Actualizar DistribucionRecaudo con campos de liquidacion (totalPagoPromotor, totalComisionPlataforma, fechaLiquidacion)
- [ ] T011 [P] [US1] Crear ConsultarDistribucionResponse en src/main/java/com/ticketevents/liquidation/infrastructure/adapter/input/rest/response/ConsultarDistribucionResponse.java
- [ ] T012 [US1] Implementar ConsultarDistribucionMapper en src/main/java/com/ticketevents/liquidation/infrastructure/mappers/ConsultarDistribucionMapper.java
- [ ] T013 [US1] Implementar ConsultarDistribucionRecaudoUseCase en src/main/java/com/ticketevents/liquidation/application/usecase/ConsultarDistribucionRecaudoUseCase.java
- [ ] T014 [US1] Implementar ConsultarDistribucionController (GET endpoint) en src/main/java/com/ticketevents/liquidation/infrastructure/interfaces/api/ConsultarDistribucionController.java
- [ ] T015 [US1] Add validation and error handling
- [ ] T016 [US1] Add logging for operations

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: Edge Cases

**Purpose**: Manejo de casos especiales definidos en la especificación

- [ ] T017 Unit tests: Evento no existe (error 404)
- [ ] T018 Unit tests: Distribucion no existe para evento
- [ ] T019 Unit tests: Evento no liquidado (bloquear consulta)
- [ ] T020 Validar existencia de distribucion antes de consultar
- [ ] T021 Validar estado "Liquidado" o "Preliminar"

**Checkpoint**: All edge cases should now be handled

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T022 Documentation updates in docs/
- [ ] T023 Code cleanup and refactoring
- [ ] T024 Performance optimization (<100ms p95)
- [ ] T025 Additional unit tests

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Edge Cases (Phase 4)**: Depends on User Story 1 implementation
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories

### Within Each User Story

- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority
- Tests after implementation

## Notes

- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests pass
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
- Los Use Cases retornan Application DTOs (de `infrastructure/adapter/output/external/dto/`)
- Los Controllers usan Mappers para convertir: Application DTO → Response DTO
- Response DTOs están en `infrastructure/adapter/input/rest/response/`
- Se reutiliza DistribucionRecaudoRepository de Spec 08
- Se reutiliza DistribucionRecaudoDto de Spec 08
- El endpoint es GET (consulta), no POST (calculo)
