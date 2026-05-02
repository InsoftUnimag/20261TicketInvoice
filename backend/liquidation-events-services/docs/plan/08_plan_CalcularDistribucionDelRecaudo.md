# Implementation Plan: Calcular Distribución del Recaudo

**Date**: 30/04/2026  
**Spec**: `docs/specs/08_CalcularDistribucionDelRecaudo.md`

## Summary

Implementar un caso de uso que calcule automáticamente la distribución del recaudo de un evento finalizado, aplicando comisiones de plataforma, descontando tickets cancelados y cortesías, y generando valores preliminares (neto preliminar y total distribuible) antes de determinar el tipo de liquidación final.

## Technical Context

**Language/Version**: Java 21 LTS  
**Primary Dependencies**: Spring Boot 4.0.5, Spring Web, Spring Data JPA, JUnit 5, Mockito, Jackson  
**Storage**: PostgreSQL (configurado)  
**Testing**: JUnit 5, Mockito  
**Target Platform**: Backend Service (Linux/Containerized)  
**Project Type**: Single Backend Service (Hexagonal Architecture)  
**Performance Goals**: <1 minuto 52 segundos para eventos con hasta 500 tickets  
**Constraints**: Evento debe estar cerrado, configuración de comisiones debe existir  
**Scale/Scope**: Eventos masivos con hasta 500 tickets vendidos

## Project Structure

### Documentation (this feature)

```text
docs/
├── specs/
│   └── 08_CalcularDistribucionDelRecaudo.md
└── plan/
    └── 08_plan_CalcularDistribucionDelRecaudo.md
```

### Source Code (repository root)

```text
src/main/java/com/ticketevents/liquidation/
├── domain/
│   ├── entities/
│   │   └── DistribucionRecaudo.java
│   └── repositories/
│       └── DistribucionRecaudoRepository.java
├── application/
│   └── usecase/
│       └── CalcularDistribucionRecaudoUseCase.java
├── infrastructure/
│   ├── adapter/
│   │   ├── input/
│   │   │   └── rest/
│   │   │       ├── request/
│   │   │       │   └── CalcularDistribucionRequest.java
│   │   │       └── response/
│   │   │           └── CalcularDistribucionResponse.java
│   │   └── output/
│   │       └── external/
│   │           └── dto/
│   │               └── DistribucionRecaudoDto.java
│   ├── interfaces/
│   │   └── api/
│   │       └── CalcularDistribucionController.java
│   └── mappers/
│       └── DistribucionRecaudoMapper.java
└── shared/
    └── errors/
        ├── ErrorCode.java
        ├── BusinessException.java
        └── TechnicalException.java

src/test/java/com/ticketevents/liquidation/
└── application/
    └── usecase/
        └── CalcularDistribucionRecaudoUseCaseTest.java
```

**Structure Decision**: Arquitectura Hexagonal con distribución de paquetes por capas:
- `domain/`: Entidades de negocio y puertos (interfaces de repositorios)
- `application/`: Casos de uso que orquestan la lógica
- `infrastructure/`: Adaptadores de entrada (REST controllers), salida (DTOs externos), y mappers
- `shared/`: Errores y utilidades compartidos
- Tests en `src/test/` siguiendo la misma estructura de paquetes

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create project structure per implementation plan
- [x] T002 Initialize Java 21 project with Spring Boot 4.0.5 dependencies
- [x] T003 Configure linting and formatting tools

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T004 Entidad ResumenVentasEvento.java (ya existe)
- [x] T005 Entidad ConfiguracionLiquidacion.java (ya existe)
- [x] T006 Entidad ComisionRecinto.java (ya existe)
- [x] T007 Entidad Recinto.java (ya existe)
- [x] T008 Puerto EventSnapshotRepository.java (ya existe)
- [x] T009 Puerto ConfiguracionLiquidacionRepository.java (ya existe)
- [x] T010 Puerto RecintoRepository.java (ya existe)
- [x] T011 ResumenVentasMapper.java (ya existe)
- [x] T012 ConsultarResumenVentasUseCase.java (ya existe)

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Calcular Distribución del Recaudo (Priority: P1)

**Goal**: Calcular automáticamente el recaudo total bruto del evento, descontar tickets cancelados y cortesías, aplicar comisiones, y generar valores netos preliminares y total distribuible.

**Independent Test**: Calcular el recaudo bruto teniendo en cuenta: aplica comisión de plataforma, determina pago final al promotor, registra el resultado del cálculo de ingresos, determina el valor de las comisiones acordadas, y registra el estado de la liquidación del evento.

### Tests for User Story 1

- [ ] T013 [P] [US1] Unit test para CalcularDistribucionRecaudoUseCase en src/test/java/com/ticketevents/liquidation/application/usecase/CalcularDistribucionRecaudoUseCaseTest.java

### Implementation for User Story 1

- [ ] T014 [P] [US1] Crear entidad DistribucionRecaudo en src/main/java/com/ticketevents/liquidation/domain/entities/DistribucionRecaudo.java
- [ ] T015 [P] [US1] Crear puerto DistribucionRecaudoRepository en src/main/java/com/ticketevents/liquidation/domain/repositories/DistribucionRecaudoRepository.java
- [ ] T016 [P] [US1] Crear DistribucionRecaudoDto en src/main/java/com/ticketevents/liquidation/infrastructure/adapter/output/external/dto/DistribucionRecaudoDto.java
- [ ] T017 [US1] Implementar CalcularDistribucionRecaudoUseCase en src/main/java/com/ticketevents/liquidation/application/usecase/CalcularDistribucionRecaudoUseCase.java
- [ ] T018 [US1] Crear CalcularDistribucionRequest en src/main/java/com/ticketevents/liquidation/infrastructure/adapter/input/rest/request/CalcularDistribucionRequest.java
- [ ] T019 [US1] Crear CalcularDistribucionResponse en src/main/java/com/ticketevents/liquidation/infrastructure/adapter/input/rest/response/CalcularDistribucionResponse.java
- [ ] T020 [US1] Implementar DistribucionRecaudoMapper en src/main/java/com/ticketevents/liquidation/infrastructure/mappers/DistribucionRecaudoMapper.java
- [ ] T021 [US1] Implementar CalcularDistribucionController en src/main/java/com/ticketevents/liquidation/infrastructure/interfaces/api/CalcularDistribucionController.java
- [ ] T022 [US1] Add validation and error handling
- [ ] T023 [US1] Add logging for operations

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: Edge Cases

**Purpose**: Manejo de casos especiales definidos en la especificación

- [ ] T024 Unit tests: Evento sin ventas (recaudo cero, estado "Sin recaudo")
- [ ] T025 Unit tests: Configuración de liquidación no definida (bloquear y solicitar establecer)
- [ ] T026 Validar evento cerrado antes de calcular
- [ ] T027 Manejar evento sin ventas
- [ ] T028 Manejar comisiones no definidas

**Checkpoint**: All edge cases should now be handled

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T029 Documentation updates in docs/
- [ ] T030 Code cleanup and refactoring
- [ ] T031 Performance optimization (<1:52 para 500 tickets)
- [ ] T032 Additional unit tests

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
- Request DTOs están en `infrastructure/adapter/input/rest/request/`
- Response DTOs están en `infrastructure/adapter/input/rest/response/`
- Se reutiliza ConsultarResumenVentasUseCase para obtener datos de ventas
- Se reutiliza RecintoRepository para obtener tipo de recinto y tasa de comisión
- Se reutiliza ConfiguracionLiquidacionRepository para verificar configuración
