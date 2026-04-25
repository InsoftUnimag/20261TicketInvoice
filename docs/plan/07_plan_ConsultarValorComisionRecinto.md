# Implementation Plan: Consultar valor comisión recinto
**Date:** 17/04/2026  
**Spec:** `Consultar_valor_comision_recinto.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura para consultar la configuración de comisión asociada a un recinto. El sistema diferenciará tres estados: recinto con comisión configurada (retorna tipo y valor), recinto sin comisión (retorna 200 OK con flag informativo), y recinto inexistente (retorna 404 controlado). Se aplicará Arquitectura Hexagonal con la estructura base proporcionada, garantizando trazabilidad, consistencia en respuestas y cumplimiento de los criterios de éxito SC-001 y SC-002.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 3.2+, Spring Data JPA
- **Storage:** PostgreSQL (tablas: `recintos`, `comisiones_recinto`). Lectura pura, sin escrituras desde este módulo.
- **Testing:** JUnit 5, MockMvc, Testcontainers, AssertJ
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Clean/Hexagonal Architecture)
- **Performance Goals:** <30ms p95 en consultas, consistencia de lectura garantizada, soporte a alta concurrencia de verificación pre-liquidación
- **Constraints:** Respuestas deterministas, manejo explícito de "sin comisión" vs "no existe", inmutabilidad de datos de referencia
- **Scale/Scope:** Consultas administrativas frecuentes, crítico para validación previa a cálculos de dispersión

## Project Structure
Documentation (this feature)

specs/consultar-comision-recinto/

├── plan.md # This file

└── spec.md # Feature specification


Source Code (repository root)

src/

└── main/java/com/ticketevents/liquidation/

├── domain/

│ ├── entities/

│ │ ├── Recinto.java

│ │ ├── ComisionConfig.java

│ │ └── TipoComision.java

│ └── repositories/

│ └── ComisionConsultaRepository.java

├── application/

│ └── usecase/

│ └── ConsultarComisionRecintoUseCase.java

├── infrastructure/

│ ├── adapter/

│ │ ├── input/

│ │ │ └── rest/

│ │ │ ├── request/

│ │ │ │ └── ConsultarComisionRequest.java

│ │ │ └── response/

│ │ │ ├── ComisionResponse.java

│ │ │ └── ErrorResponse.java

│ │ └── output/

│ │ └── external/

│ │ └── dto/

│ │ └── ComisionDto.java

│ ├── external/

│ │ ├── JpaComisionRepositoryAdapter.java

│ │ └── MockComisionRepository.java

│ ├── interfaces/

│ │ └── api/

│ │ ├── ComisionRecintoController.java

│ │ └── GlobalExceptionHandler.java

│ └── config/

│ └── JpaConfig.java

└── shared/

└── errors/

├── ErrorCode.java

├── BusinessException.java

└── TechnicalException.java

src/test/java/com/ticketevents/liquidation/

├── unit/

├── integration/

└── concurrency/


**Structure Decision:** Se aplica estrictamente la infraestructura base proporcionada. El dominio contiene entidades de solo lectura y el puerto `ComisionConsultaRepository`. La capa `infrastructure/adapter` segrega contratos REST (`input`) y mapeos de persistencia (`output/external/dto`). `infrastructure/external` implementa el puerto vía JPA y expone `MockComisionRepository` para entornos aislados. `shared/errors` + `GlobalExceptionHandler` estandarizan respuestas HTTP. Los tests se mantienen segregados por nivel de abstracción.

---

## Phase 1: Setup (Shared Infrastructure)
**Purpose:** Project initialization and basic structure
- [ ] T001 Crear estructura de directorios según `Project Structure` definido
- [ ] T002 Inicializar proyecto Java 21 con Spring Boot 3.2 (Web, Data JPA, Validation, Actuator)
- [ ] T003 Configurar linting y formatters (Checkstyle/Spotless, SpotBugs) y baseline de SonarQube
**Checkpoint:** Repositorio listo, build sin errores, convenciones aplicadas.

## Phase 2: Foundational (Blocking Prerequisites)
**Purpose:** Core infrastructure that MUST be complete before ANY user story can be implemented
CRITICAL: No user story work can begin until this phase is complete
- [ ] T004 Configurar PostgreSQL y Flyway con esquemas de solo lectura para `recintos` y `comisiones_recinto`
- [ ] T005 Implementar infraestructura de errores centralizada en `shared/errors/` y `GlobalExceptionHandler`
- [ ] T006 Configurar JPA (`JpaConfig.java`), pool de conexiones y transacciones `READ_ONLY` por defecto
- [ ] T007 Crear entidades de dominio: `Recinto.java`, `ComisionConfig.java`, `TipoComision.java` (enum)
- [ ] T008 Definir puerto `ComisionConsultaRepository.java` en `domain/repositories/`
- [ ] T009 Configurar logging estructurado (JSON), Correlation ID y métricas de consulta para auditoría pre-liquidación
**Checkpoint:** Foundation ready - user story implementation can now begin in parallel

## Phase 3: User Story 1 - Consultar valor comisión recinto (Priority: P1)
**Goal:** Exponer endpoint de consulta para verificar tipo y valor de comisión de un recinto, manejando explícitamente los casos "configurada", "sin configurar" y "inexistente".
**Independent Test:** Invocar `GET /api/v1/recintos/{id}/comision` con recinto existente y comisión → `200 OK` con `tipo` y `valor`. Invocar con recinto sin comisión → `200 OK` con `configurada: false` y mensaje informativo. Invocar con ID inexistente → `404` con `BusinessException`.

### Tests for User Story 1
- [ ] T010 [P] [US1] Contract test para endpoint en `test/integration/ComisionConsultaApiContractTest.java`
- [ ] T011 [P] [US1] Integration test con Testcontainers (validación 3 escenarios, mapeo DB→Response) en `test/integration/ConsultarComisionIntegrationTest.java`
- [ ] T012 [US1] Unit tests para `ConsultarComisionRecintoUseCase` en `test/unit/ConsultarComisionRecintoUseCaseTest.java`

### Implementation for User Story 1
- [ ] T013 [P] [US1] Crear DTOs `ConsultarComisionRequest`/`Response` en `infrastructure/adapter/input/rest/`
- [ ] T014 [US1] Crear `ComisionDto.java` en `infrastructure/adapter/output/external/dto/` para mapeo JPA ↔ Dominio
- [ ] T015 [US1] Implementar `JpaComisionRepositoryAdapter.java` en `infrastructure/external/` (consultas optimizadas `LEFT JOIN` para detectar ausencia)
- [ ] T016 [US1] Implementar `ConsultarComisionRecintoUseCase` (validación existencia, recuperación comisión, mapeo a respuesta con flag `configurada`, manejo de `null`)
- [ ] T017 [US1] Implementar `ComisionRecintoController.java` en `infrastructure/interfaces/api/` (endpoint `GET`, mapeo request/response, validación path)
- [ ] T018 [US1] Integrar manejo de errores: `BusinessException` (recinto no existe), lógica de respuesta `200` con advertencia para sin comisión, registro de auditoría
**Checkpoint:** At this point, User Story 1 should be fully functional and testable independently

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements that affect multiple user stories
- [ ] T019 Documentación OpenAPI/Swagger y guía de validación para administradores de recinto y liquidación
- [ ] T020 Implementar caché de lectura (`@Cacheable`) para recintos con comisión activa, invalidación manual tras actualizaciones
- [ ] T021 Añadir tests de concurrencia y carga en `test/concurrency/` para validar consistencia bajo picos de verificación pre-liquidación
- [ ] T022 Hardening: validación estricta de inputs, sanitización de logs, rate limiting por IP/rol `ADMIN_RECINTO`
- [ ] T023 Code cleanup, optimización de proyecciones JPA (evitar selección de columnas innecesarias), revisión de arquitectura
**Checkpoint:** Feature ready for staging deployment and financial UAT

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1):** No dependencies - can start immediately
- **Foundational (Phase 2):** Depends on Setup completion - **BLOCKS** all user stories
- **User Stories (Phase 3):** All depend on Foundational phase completion. Proceed sequentially for this feature.
- **Polish (Phase 4):** Depends on User Story 1 completion and test validation

### User Story Dependencies
- **User Story 1 (P1):** Can start after Foundational (Phase 2). Standalone for this feature.
- **External Services:** None. Lectura directa desde repositorio interno. `MockComisionRepository` habilita desarrollo offline.

### Within Each User Story
1. Domain Entities & Enums before repositories
2. Repository Port before JPA Adapter implementation
3. DTOs (input/output) before Use Case
4. Core Use Case logic before Controller/Endpoint
5. Error handling & Validation after business logic
6. Tests after implementation (or TDD if preferred)
7. Story complete before moving to Polish phase

### Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- **FR-003 (Sin comisión):** No se lanza excepción. Se retorna `HTTP 200` con estructura `{ "configurada": false, "mensaje": "El recinto no tiene una comisión registrada", "tipoComision": null, "valorComision": null }`. Esto evita acoplamiento con manejo de errores y cumple el requisito de "informar".
- **Edge Case (Recinto inexistente):** Retorna `HTTP 404` con `ErrorCode.VENUE_NOT_FOUND`. Mensaje controlado: `"El recinto no está registrado"`.
- **SC-001:** Garantizado por consultas deterministas y mapeo explícito de `TipoComision` a response. Validación de nulidad en `UseCase` asegura que el 100% de las consultas con configuración retornen el valor correcto.
- **SC-002:** Validación de existencia vía `RecintoRepository` antes de consultar comisión. Si no existe, flujo corta inmediatamente con `BusinessException`.
- **Optimización de Lectura:** Se utiliza `@Query` con proyección directa a `ComisionDto` para evitar carga de entidades completas y reducir latencia.
- Commit after each task or logical group. Stop at any checkpoint to validate story independently.
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence.