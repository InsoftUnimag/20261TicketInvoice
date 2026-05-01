# Implementation Plan: Consultar Distribución del Recaudo
**Date:** 30/04/2026  
**Spec:** `09_ConsultarDistribucionDelRecaudo.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador financiero consultar la distribución del recaudo generado por un evento para validar que la liquidación se haya ejecutado correctamente según el modelo de negocio configurado.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Evento en estado "Liquidado", distribución disponible, montos consistentes

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 09_ConsultarDistribucionDelRecaudo.md
└── plan/
    └── 09_plan_ConsultarDistribucionDelRecaudo.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── DistribucionRecaudo.java  (de Spec 08)
│   │   └── Evento.java                 (ya existe, opcional)
│   └── repositories/
│       ├── DistribucionRecaudoRepository.java (de Spec 08)
│       └── EventoRepository.java             (ya existe, opcional)

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── ConsultarDistribucionRecaudoUseCase.java (nuevo)
│       └── CalcularDistribucionRecaudoUseCase.java (de Spec 08, relacionado)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarDistribucionRequest.java (nuevo, opcional)
│       │       └── response/
│       │           ├── ConsultarDistribucionResponse.java (nuevo)
│       │           ├── DetalleDistribucionResponse.java (nuevo)
│       │           └── DistribucionRecaudoResponse.java (de Spec 08, reusable)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── DistribucionRecaudoDto.java (de Spec 08)
│                   └── EventoDto.java           (nuevo, opcional)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ConsultarDistribucionController.java (nuevo)
│   │       └── CalcularDistribucionController.java (de Spec 08, relacionado)
│   └── mappers/
│       ├── ConsultarDistribucionMapper.java (nuevo)
│       └── DistribucionRecaudoMapper.java  (de Spec 08, reusable)

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
**Purpose:** Core infrastructure (depende de Spec 08)

### Existing entities from Spec 08
- [ ] T004 Entidad DistribucionRecaudo.java (de Spec 08)
- [ ] T005 Puerto DistribucionRecaudoRepository.java (de Spec 08)

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Consultar Distribución de Evento Liquidado (Priority: P1)
**Goal:** Permitir al administrador financiero consultar la distribución del recaudo de un evento en estado "Liquidado".

**Independent Test**: Dado que un evento se encuentra en estado "Liquidado", cuando el administrador financiero consulta la distribución del recaudo del evento, entonces el sistema muestra el detalle de los montos distribuidos al promotor y comisión de plataforma.

### Tests for User Story 1
- [ ] T006 [P] [US1] Unit test para ConsultarDistribucionRecaudoUseCase
- [ ] T007 [P] [US1] Integration test para endpoint

### Implementation for User Story 1
- [ ] T008 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T009 [US1] Implementar ConsultarDistribucionRecaudoUseCase (retorna DistribucionRecaudoDto)
- [ ] T010 [US1] Implementar ConsultarDistribucionController (GET endpoint)
- [ ] T011 [US1] Implementar ConsultarDistribucionMapper (Application DTO → Response DTO)
- [ ] T012 [US1] Integrar manejo de errores
- [ ] T013 [US1] Tests unitarios

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Consultar Distribución de Evento No Liquidado (Priority: P2)
**Goal:** Manejar el caso cuando se intenta consultar la distribución de un evento que no ha sido liquidado.

**Independent Test**: Dado que un evento no ha sido liquidado, cuando el administrador financiero intenta consultar la distribución del recaudo, entonces el sistema muestra un mensaje indicando que el evento aún no tiene liquidación disponible.

### Tests for User Story 2
- [ ] T014 [P] [US2] Unit test para caso no liquidado
- [ ] T015 [P] [US2] Integration test para error controlado

### Implementation for User Story 2
- [ ] T016 [US2] Actualizar Use Case para validar estado "Liquidado"
- [ ] T017 [US2] Retornar mensaje de "sin liquidación disponible"
- [ ] T018 [US2] Tests unitarios

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: Edge Cases
**Purpose:** Manejo de casos especiales

### Tests for Edge Cases
- [ ] T019 Unit tests: Evento no existe (error 404)
- [ ] T020 Unit tests: Inconsistencias en montos calculados (bloquear consulta)
- [ ] T021 Unit tests: Montos no modificables después de consultados

### Implementation for Edge Cases
- [ ] T022 Validar existencia de evento
- [ ] T023 Validar consistencia de montos
- [ ] T024 Bloquear modificaciones posteriores

**Checkpoint**: All user stories should now be independently functional

---

## Phase N: Polish & Cross-Cutting Concerns
**Purpose:** Improvements that affect multiple user stories
- [ ] T025 OpenAPI/Swagger documentation
- [ ] T026 Circuit Breaker (Resilience4j)
- [ ] T027 Tests de concurrencia
- [ ] T028 Validaciones más estrictas
- [ ] T029 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Spec 08 (DistribucionRecaudo entity)
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Edge Cases**: Depends on User Stories
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies
- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **Edge Cases**: Depend on User Stories

### Within Each User Story
- Models before services (de Spec 08)
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
- DistribucionRecaudoDto debe existir de Spec 08
- El sistema debe validar que el evento esté en estado "Liquidado"
- Los montos mostrados deben ser 100% consistentes con la liquidación generada

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → ConsultarDistribucionController → ConsultarDistribucionRecaudoUseCase → Domain → Infrastructure
                                                                             ↓
                                                    DistribucionRecaudoDto (output/external/dto)
                                                                                        ↓
                                                    Mapper: DistribucionRecaudoDto → ConsultarDistribucionResponse
                                                                                        ↓
Cliente ← ConsultarDistribucionResponse ← ConsultarDistribucionController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/eventos/{id}/distribucion-recaudo`
2. `ConsultarDistribucionController` recibe el request
3. Llama a `ConsultarDistribucionRecaudoUseCase.execute(eventoId)`
4. Use Case valida que el evento esté en estado "Liquidado"
5. Use Case retorna `DistribucionRecaudoDto` con:
   - Total pago al promotor
   - Total comisión plataforma
   - Estado de la liquidación
6. Controller usa `ConsultarDistribucionMapper.toResponse(dto)` → `ConsultarDistribucionResponse`
7. Cliente recibe el detalle de montos distribuidos

---

## Diferencia con CalcularDistribucionDelRecaudo (Spec 08)

| Característica | Calcular (Spec 08) | Consultar (Spec 09) |
|----------------|----------------------|-----------------------|
| Endpoint | `POST /api/v1/eventos/{id}/calcular-distribucion` | `GET /api/v1/eventos/{id}/distribucion-recaudo` |
| Propósito | Calcular distribución preliminar | Consultar distribución ya liquidada |
| Estado evento | Finalizado (sin estado específico) | "Liquidado" (validación estricta) |
| Retorno | `DistribucionRecaudoDto` (preliminar) | `DistribucionRecaudoDto` (consolidado) |
| Use Case | CalcularDistribucionRecaudoUseCase | ConsultarDistribucionRecaudoUseCase |
| Controller | CalcularDistribucionController | ConsultarDistribucionController |
| Mapper | DistribucionRecaudoMapper | ConsultarDistribucionMapper |

---

## Criterios de Consistencia (Success Criteria)

- **SC-001**: El 100% de las consultas de distribución deben mostrar montos consistentes con la liquidación generada.
- **SC-002**: El 100% de los eventos en estado "Liquidado" deben tener una distribución disponible para consulta.
- **SC-003**: El sistema debe bloquear consultas de eventos no liquidados.
- **SC-004**: Una vez consultada la distribución, los datos no deben permitir modificaciones.
