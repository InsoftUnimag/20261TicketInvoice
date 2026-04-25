# Implementation Plan: Consultar distribucion del recaudo
**Date:** 17/04/2026  
**Spec:** `Consultar_distribucion_del_recaudo.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura que permita consultar la distribución financiera final de un evento, validando que el estado sea `LIQUIDADO`, verificando consistencia matemática de los montos y retornando el desglose (promotor, plataforma, reembolsos). Se aplicará Arquitectura Hexagonal con transacciones `READ_ONLY`, validación estricta de consistencia financiera y bloqueo explícito ante discrepancias, garantizando transparencia y cumplimiento de SC-001/SC-002.

## Technical Context
- Language/Version: Java 21 LTS
- Primary Dependencies: Spring Boot 3.2+, Spring Data JPA, Hibernate Validator, JUnit 5, Mockito, Testcontainers
- Storage: PostgreSQL (tablas: `eventos`, `distribucion_recaudo`). Acceso exclusivo de lectura para este módulo.
- Testing: JUnit 5, MockMvc, Testcontainers, AssertJ
- Target Platform: Backend Service (Linux/Containerized)
- Project Type: Single Backend Service (Clean/Hexagonal Architecture)
- Performance Goals: <100ms p95 en consultas, consistencia de lectura garantizada
- Constraints: Validación de estado `LIQUIDADO`, verificación de consistencia aritmética (`promotor + plataforma + reembolsos == bruto`), inmutabilidad post-liquidación, bloqueo de consulta ante inconsistencias
- Scale/Scope: Consultas administrativas y de auditoría, alta criticidad para validación de dispersión de fondos

## Project Structure
Documentation (this feature)

specs/consultar-distribucion-recaudo/

├── plan.md

└── spec.md


Source Code (repository root)

src/

└── main/java/com/ticketevents/liquidation/

├── domain/

│   ├── entities/

│   │   ├── Evento.java

│   │   ├── DistribucionRecaudo.java

│   │   └── EstadoLiquidacion.java

│   └── repositories/

│       └── DistribucionConsultaRepository.java

├── application/

│   └── usecase/

│       └── ConsultarDistribucionRecaudoUseCase.java

├── infrastructure/

│   ├── adapter/

│   │   ├── input/

│   │   │   └── rest/

│   │   │       ├── request/

│   │   │       │   └── ConsultarDistribucionRequest.java

│   │   │       └── response/

│   │   │           ├── DistribucionResponse.java

│   │   │           └── ErrorResponse.java

│   │   └── output/

│   │       └── external/

│   │           └── dto/

│   │               └── DistribucionDto.java

│   ├── external/

│   │   ├── JpaDistribucionRepositoryAdapter.java

│   │   └── MockDistribucionRepository.java

│   ├── interfaces/

│   │   └── api/

│   │       ├── DistribucionRecaudoController.java

│   │       └── GlobalExceptionHandler.java

│   └── config/

│       └── PersistenceConfig.java

└── shared/

    └── errors/

        ├── ErrorCode.java

        ├── BusinessException.java

        └── TechnicalException.java


Structure Decision: Se aplica estrictamente la infraestructura base proporcionada. El dominio contiene entidades de solo lectura y el puerto `DistribucionConsultaRepository`. La capa `infrastructure/adapter` segrega contratos REST (`input`) y DTOs de mapeo (`output/external/dto`). `infrastructure/external` implementa el puerto vía JPA y expone `MockDistribucionRepository` para entornos aislados. `shared/errors` + `GlobalExceptionHandler` estandarizan respuestas HTTP. Los tests se mantienen segregados por nivel de abstracción.

---

## Phase 1: Setup (Shared Infrastructure)
Purpose: Project initialization and basic structure
- [ ] T001 Crear estructura de directorios según Project Structure definido
- [ ] T002 Inicializar proyecto Java 21 con Spring Boot 3.2 (Web, Data JPA, Validation, Actuator)
- [ ] T003 Configurar linting y formatters (Checkstyle/Spotless, SpotBugs) y baseline de SonarQube
Checkpoint: Repositorio listo, build sin errores, convenciones aplicadas.

## Phase 2: Foundational (Blocking Prerequisites)
Purpose: Core infrastructure that MUST be complete before ANY user story can be implemented

CRITICAL: No user story work can begin until this phase is complete
- [ ] T004 Configurar PostgreSQL y Flyway con esquemas de solo lectura para `eventos` y `distribucion_recaudo`
- [ ] T005 Implementar infraestructura de errores centralizada en `shared/errors/` y `GlobalExceptionHandler`
- [ ] T006 Configurar JPA (`PersistenceConfig.java`), pool de conexiones y transacciones `READ_ONLY` por defecto
- [ ] T007 Crear entidades de dominio: `Evento.java`, `DistribucionRecaudo.java`, `EstadoLiquidacion.java` (enum)
- [ ] T008 Definir puerto `DistribucionConsultaRepository.java` en `domain/repositories/`
- [ ] T009 Configurar logging estructurado (JSON), Correlation ID y métricas de auditoría financiera
Checkpoint: Foundation ready - user story implementation can now begin in parallel

## Phase 3: User Story 1 - Consultar distribucion del recaudo (Priority: P1)
Goal: Exponer endpoint de consulta para validar montos finales de liquidación, aplicando reglas estrictas de estado y consistencia aritmética.
Independent Test: Invocar `GET /api/v1/eventos/{id}/distribucion` con evento `LIQUIDADO` → `200 OK` con desglose. Evento no liquidado → `200 OK` con flag/mensaje. Evento inexistente → `404`. Inconsistencia detectada → `409` + notificación de auditoría.

### Tests for User Story 1
- [ ] T010 [P] [US1] Contract test para endpoint en `test/integration/DistribucionConsultaApiContractTest.java`
- [ ] T011 [P] [US1] Integration test con Testcontainers (validación estados, consistencia, errores) en `test/integration/ConsultarDistribucionIntegrationTest.java`
- [ ] T012 [US1] Unit tests para `ConsultarDistribucionRecaudoUseCase` en `test/unit/ConsultarDistribucionRecaudoUseCaseTest.java`

### Implementation for User Story 1
- [ ] T013 [P] [US1] Crear DTOs `ConsultarDistribucionRequest`/`Response` en `infrastructure/adapter/input/rest/`
- [ ] T014 [US1] Crear `DistribucionDto.java` en `infrastructure/adapter/output/external/dto/` para mapeo JPA ↔ Dominio
- [ ] T015 [US1] Implementar `JpaDistribucionRepositoryAdapter.java` en `infrastructure/external/` (proyecciones optimizadas, validación de consistencia en query)
- [ ] T016 [US1] Implementar `ConsultarDistribucionRecaudoUseCase` (validación existencia, validación estado `LIQUIDADO`, verificación aritmética, mapeo a response, manejo de inconsistencia)
- [ ] T017 [US1] Implementar `DistribucionRecaudoController.java` en `infrastructure/interfaces/api/` (endpoint `GET`, validación path, mapeo)
- [ ] T018 [US1] Integrar manejo de errores: `BusinessException` (evento inexistente/no liquidado), `TechnicalException` (inconsistencia detectada), registro de auditoría y alertas
Checkpoint: At this point, User Story 1 should be fully functional and testable independently

## Phase 4: Polish & Cross-Cutting Concerns
Purpose: Improvements that affect multiple user stories
- [ ] T019 Documentación OpenAPI/Swagger y guía de validación para administradores financieros
- [ ] T020 Implementar caché de lectura (`@Cacheable`) con invalidación controlada para evitar recalculos repetidos
- [ ] T021 Añadir tests de concurrencia en `test/concurrency/` para validar consistencia bajo lecturas masivas simultáneas
- [ ] T022 Hardening: validación estricta de inputs, sanitización de logs, rate limiting por rol `ADMIN_FINANCIERO`, verificación de firmas digitales de liquidación (si aplica)
- [ ] T023 Code cleanup, optimización de proyecciones JPA, revisión de arquitectura y compliance financiero
Checkpoint: Feature ready for staging deployment and financial UAT

---

## Dependencies & Execution Order

### Phase Dependencies
- Setup (Phase 1): No dependencies - can start immediately
- Foundational (Phase 2): Depends on Setup completion - BLOCKS all user stories
- User Stories (Phase 3): All depend on Foundational phase completion. Proceed sequentially for this feature.
- Polish (Phase 4): Depends on User Story 1 completion and test validation

### User Story Dependencies
- User Story 1 (P1): Can start after Foundational (Phase 2). Standalone for this feature.
- External Services: None. Lectura directa desde repositorio interno. `MockDistribucionRepository` habilita desarrollo offline.

### Within Each User Story
1. Domain Entities & Enums before repositories
2. Repository Port before JPA Adapter implementation
3. DTOs (input/output) before Use Case
4. Core Use Case logic before Controller/Endpoint
5. Error handling & Validation after business logic
6. Tests after implementation (or TDD if preferred)
7. Story complete before moving to Polish phase

### Notes
- [P] = Prioridad/Paralelizable, [US1] = Trazabilidad a Historia de Usuario 1
- FR-004 (Solo si Liquidado): Validación estricta en UseCase. Si `estadoEvento != LIQUIDADO`, retorna `200 OK` con payload `{ "disponible": false, "mensaje": "El evento aún no tiene liquidación disponible" }` para cumplir el escenario sin acoplar errores.
- Edge Case (Inconsistencias): El UseCase ejecuta `totalPagoPromotor + totalComisionPlataforma + totalReembolsos == totalRecaudoBruto` (con tolerancia `0.01` por redondeo `BigDecimal`). Si falla, lanza `TechnicalException` con código `INCONSISTENT_LIQUIDATION_AMOUNTS` y bloquea la respuesta, activando alerta de auditoría.
- FR-006 (Inmutabilidad): Transacciones marcadas `@Transactional(readOnly = true)`. No se expone ningún método de escritura. El `GlobalExceptionHandler` retorna `405 Method Not Allowed` para `POST/PUT/DELETE`.
- SC-001 & SC-002: Garantizados por validación de estado previa, consulta determinista vía JPA y verificación de integridad aritmética antes de retornar datos al cliente.
- Commit after each task or logical group. Stop at any checkpoint to validate story independently.
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence.