# Implementation Plan: Calcular Distribucion del Recaudo
**Date:** 17/04/2026  
**Spec:** `Calcular_Distribucion_del_Recaudo.md`

## Summary
Implementar un caso de uso y endpoint que calcule automáticamente la distribución preliminar del recaudo para eventos finalizados, aplicando deducciones por tickets cancelados/cortesías, validando existencia de comisiones configuradas y registrando el resultado financiero con estado "Preliminar" o "Sin recaudo". Se garantizará idempotencia, trazabilidad auditiva y cumplimiento estricto del límite de rendimiento (<112s para ≤500 tickets), asegurando el 95% de precisión sin correcciones manuales (SC-001, SC-002, SC-003).

## Technical Context
- Language/Version: Java 21 LTS
- Primary Dependencies: Spring Boot 3.2+, Spring Data JPA
- Storage: PostgreSQL (tablas: `eventos`, `tickets`, `comisiones_recinto`, `distribucion_recaudo`)
- Testing: JUnit 5, MockMvc, Testcontainers, AssertJ, JMH (opcional para benchmark de rendimiento)
- Target Platform: Backend Service (Linux/Containerized)
- Project Type: Single Backend Service (Clean/Hexagonal Architecture)
- Performance Goals: <112s p99 para cálculos con ≤500 tickets, consistencia ACID, idempotencia garantizada
- Constraints: Bloqueo si falta comisión, deducción exacta de cancelaciones/cortesías, estado inmutable post-cálculo, validación de integridad financiera
- Scale/Scope: Cálculos on-demand o programados por evento, alto impacto en motor de liquidación, requiere optimización de queries agregadas

## Project Structure
Documentation (this feature)

specs/calcular-distribucion-recaudo/

├── plan.md

└── spec.md


Source Code (repository root)

src/

└── main/java/com/ticketevents/liquidation/

├── domain/

│ ├── entities/

│ │ ├── Evento.java

│ │ ├── Ticket.java

│ │ ├── DistribucionRecaudo.java

│ │ └── EstadoLiquidacion.java

│ └── repositories/

│ └── DistribucionRecaudoRepository.java

├── application/

│ └── usecase/

│ └── CalcularDistribucionRecaudoUseCase.java

├── infrastructure/

│ ├── adapter/

│ │ ├── input/

│ │ │ └── rest/

│ │ │ ├── request/

│ │ │ │ └── CalcularDistribucionRequest.java

│ │ │ └── response/

│ │ │ ├── DistribucionResponse.java

│ │ │ └── ErrorResponse.java

│ │ └── output/

│ │ └── external/

│ │ └── dto/

│ │ └── DistribucionDto.java

│ ├── external/

│ │ ├── JpaDistribucionRepositoryAdapter.java

│ │ └── MockDistribucionRepository.java

│ ├── interfaces/

│ │ └── api/

│ │ ├── DistribucionRecaudoController.java

│ │ └── GlobalExceptionHandler.java

│ └── config/

│ └── PersistenceConfig.java

└── shared/

└── errors/

├── ErrorCode.java

├── BusinessException.java

└── TechnicalException.java

src/test/java/com/ticketevents/liquidation/

├── unit/

├── integration/

└── concurrency/


Structure Decision: Se aplica estrictamente la infraestructura base proporcionada. El dominio modela `DistribucionRecaudo` y `EstadoLiquidacion` (enum: PRELIMINAR, SIN_RECAUDO, BLOQUEADO). El puerto `DistribucionRecaudoRepository` abstrae la persistencia y consultas financieras. `infrastructure/adapter` segrega contratos REST (`input`) y DTOs de mapeo JPA (`output`). `infrastructure/external` implementa el adaptador JPA y expone `MockDistribucionRepository` para desarrollo aislado. `shared/errors` + `GlobalExceptionHandler` garantizan respuestas HTTP estandarizadas. Los tests se mantienen segregados por nivel de abstracción y validación de carga.

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
- [ ] T004 Configurar PostgreSQL y Flyway con esquemas: `eventos`, `tickets`, `comisiones_recinto`, `distribucion_recaudo` + índices para agregaciones por `eventoId`
- [ ] T005 Implementar infraestructura de errores centralizada en `shared/errors/` y `GlobalExceptionHandler`
- [ ] T006 Configurar JPA (`PersistenceConfig.java`), pool de conexiones, transacciones por defecto y auditoría temporal
- [ ] T007 Crear entidades de dominio: `Evento.java`, `Ticket.java`, `DistribucionRecaudo.java`, `EstadoLiquidacion.java` (enum)
- [ ] T008 Definir puerto `DistribucionRecaudoRepository.java` en `domain/repositories/`
- [ ] T009 Configurar logging estructurado (JSON), Correlation ID y métricas de tiempo de cálculo para cumplir SC-002
Checkpoint: Foundation ready - user story implementation can now begin in parallel

## Phase 3: User Story 1 - Calcular Distribucion del Recaudo (Priority: P1)
Goal: Exponer capacidad para calcular y persistir la distribución preliminar del recaudo, validando comisiones, aplicando deducciones y manejando escenarios de cero ventas.
Independent Test: Invocar `POST /api/v1/eventos/{id}/distribucion` → validar cálculo correcto, estado `PRELIMINAR` y persistencia. Validar bloqueo `400` si falta comisión. Validar `200` con estado `SIN_RECAUDO` si no hay ventas. Verificar tiempo <112s con dataset de 500 tickets.

### Tests for User Story 1
- [ ] T010 [P] [US1] Contract test para endpoint en `test/integration/DistribucionRecaudoApiContractTest.java`
- [ ] T011 [P] [US1] Integration test con Testcontainers (validación lógica de cálculo, deducciones, estados, tiempos) en `test/integration/CalcularDistribucionIntegrationTest.java`
- [ ] T012 [US1] Unit tests para `CalcularDistribucionRecaudoUseCase` en `test/unit/CalcularDistribucionRecaudoUseCaseTest.java`

### Implementation for User Story 1
- [ ] T013 [P] [US1] Crear DTOs `CalcularDistribucionRequest`/`Response` en `infrastructure/adapter/input/rest/`
- [ ] T014 [US1] Crear `DistribucionDto.java` en `infrastructure/adapter/output/external/dto/` para mapeo JPA ↔ Dominio
- [ ] T015 [US1] Implementar `JpaDistribucionRepositoryAdapter.java` en `infrastructure/external/` (consultas optimizadas, agregaciones, persistencia transaccional)
- [ ] T016 [US1] Implementar `CalcularDistribucionRecaudoUseCase` (validación evento finalizado, validación comisión, filtro cancelaciones/cortesías, cálculo bruto/neto/distribuible, manejo de 0 ventas, idempotencia)
- [ ] T017 [US1] Implementar `DistribucionRecaudoController.java` en `infrastructure/interfaces/api/` (endpoint POST, validación path, mapeo)
- [ ] T018 [US1] Integrar manejo de errores: `BusinessException` (comisión faltante, evento no finalizado), `TechnicalException` (fallo DB/timeout), registro de auditoría para SC-001
Checkpoint: At this point, User Story 1 should be fully functional and testable independently

## Phase 4: Polish & Cross-Cutting Concerns
Purpose: Improvements that affect multiple user stories
- [ ] T019 Documentación OpenAPI/Swagger y guía de cálculo financiero para administradores
- [ ] T020 Implementar validación de idempotencia (`@Transactional` + chequeo de existencia previa) para evitar duplicados por reintentos
- [ ] T021 Añadir tests de concurrencia y carga en `test/concurrency/` para validar límite de 112s bajo estrés y consistencia de locks
- [ ] T022 Hardening: validación estricta de decimales (`BigDecimal`), sanitización de logs, rate limiting por rol `ADMIN_FINANCIERO`
- [ ] T023 Code cleanup, optimización de queries JPA (proyecciones directas, evitar N+1), revisión de arquitectura y compliance financiero
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
- External Services: None. Cálculo interno basado en datos locales. `MockDistribucionRepository` habilita desarrollo offline.

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
- Edge Case "Sin ventas": Si `totalTicketsValidados == 0`, el Use Case persiste `DistribucionRecaudo` con montos `0.00`, fecha actual y estado `SIN_RECAUDO`. Retorna `200 OK`.
- Edge Case "Comisión indefinida": El Use Case valida existencia y vigencia de comisión antes de calcular. Si falta, lanza `BusinessException` con código `MISSING_VENUE_COMMISSION` y bloquea el flujo (FR-02/Edge Case).
- SC-001: Garantizado por validaciones estrictas previas y transacciones ACID. Eventos sin datos completos no permiten cálculo.
- SC-002 (<1m 52s): Se logra mediante consultas agregadas nativas (`@Query`), uso de `BigDecimal` para precisión, y ausencia de procesamiento en memoria innecesario. Se valida en `T021`.
- SC-003 (95% sin corrección): Se asegura con reglas de dominio inmutables, idempotencia y validación de integridad de `Ticket.estado` antes del cálculo.
- Cálculo financiero: Usa `BigDecimal.ROUND_HALF_UP` para evitar desviaciones de centavos. Todos los valores se normalizan a 2 decimales antes de persistir.
- Commit after each task or logical group. Stop at any checkpoint to validate story independently.
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence.