# Implementation Plan: Consultar estado de ingreso de tickets
*Date:* 17/04/2026  
*Spec:* Consultar_estado_de_ingreso_de_tickets.md

## Summary
Implementar un caso de uso y endpoint que permita al módulo de liquidación consultar el estado de ingreso (check-in) de los tickets asociados a un evento, consumiendo el servicio externo del módulo de operación y control de accesos. Se aplicará Arquitectura Hexagonal, abstracción del cliente HTTP, mapeo de estados ("Check-in realizado" vs "Sin registro"), manejo de ausencia de datos como "No asistido" y bloqueo explícito del flujo de liquidación ante fallos externos o respuestas incompletas (SC-001).

## Technical Context
- *Language/Version:* Java 21 LTS
- *Primary Dependencies:* Spring Boot 3.2+, Spring Web, WebClient/RestClient
- *Storage:* PostgreSQL (auditoría de consultas y logs de dispersión). No se persiste el estado de ingreso (se consulta on-demand para garantizar frescura financiera).
- *Testing:* JUnit 5, MockMvc, WireMock (contract/integration), Testcontainers
- *Target Platform:* Backend Service (Linux/Containerized)
- *Project Type:* Single Backend Service (Clean/Hexagonal Architecture)
- *Performance Goals:* <500ms p95 por consulta batch/paginada, 100% de tasa de respuestas válidas antes de iniciar liquidación (SC-001)
- *Constraints:* Tolerancia cero a inconsistencias financieras, fallback a "No asistido" si no hay registro, bloqueo de liquidación si el servicio externo falla o devuelve parciales
- *Scale/Scope:* Consultas por evento (potencialmente cientos/miles de tickets), requiere paginación o batch processing para evitar timeouts

## Project Structure
Documentation (this feature)

specs/consultar-estado-ingreso/
├── plan.md # This file
└── spec.md # Feature specification


Source Code (repository root)

src/
├── main/java/com/empresa/ingreso/
│ ├── domain/
│ │ ├── entities/
│ │ │ ├── Ticket.java
│ │ │ ├── Evento.java
│ │ │ └── RegistroIngreso.java
│ │ └── repositories/
│ │ └── AccessControlRepository.java (Puerto para servicio externo)
│ ├── application/
│ │ ├── usecase/
│ │ │ └── ConsultarEstadoIngresoUseCase.java
│ │ └── dto/
│ │ ├── ConsultarEstadoIngresoRequest.java
│ │ ├── ConsultarEstadoIngresoResponse.java
│ │ └── ErrorResponse.java
│ ├── infrastructure/
│ │ ├── external/
│ │ │ └── AccessControlApiClient.java (Adaptador WebClient/RestClient)
│ │ ├── interfaces/
│ │ │ └── api/
│ │ │ └── EstadoIngresoController.java
│ │ ├── config/
│ │ │ ├── RestClientConfig.java
│ │ │ └── TransactionConfig.java
│ │ └── concurrency/
│ │ └── (Batch processing / throttling para consultas masivas)
│ └── shared/
│ ├── errors/
│ │ ├── ErrorCode.java
│ │ ├── BusinessException.java
│ │ └── TechnicalException.java
│ └── constants/
│ └── SystemConstants.java
└── test/java/com/empresa/ingreso/
├── unit/
├── integration/
└── concurrency/


*Structure Decision:* Se mantiene la arquitectura Hexagonal/Clean v2 corregida. El servicio externo de control de accesos se abstrae detrás del puerto domain/repositories/AccessControlRepository.java. RegistroIngreso se modela como entidad/VO de dominio inmutable, calculada y mapeada en el UseCase. Los tests se segregan en unit/, integration/ y concurrency/ para validar lógica aislada, flujos con WireMock y rendimiento bajo carga batch.

---

## Phase 1: Setup (Shared Infrastructure)
*Purpose:* Project initialization and basic structure
- [ ] T001 Crear estructura de directorios según Project Structure definido
- [ ] T002 Inicializar proyecto Java 21 con Spring Boot 3.2 (Web, Validation, JPA, Actuator)
- [ ] T003 Configurar linting y formatters (Checkstyle/Spotless, SpotBugs) y baseline de SonarQube
  *Checkpoint:* Repositorio listo, build sin errores, convenciones aplicadas.

## Phase 2: Foundational (Blocking Prerequisites)
*Purpose:* Core infrastructure that MUST be complete before ANY user story can be implemented
CRITICAL: No user story work can begin until this phase is complete
- [ ] T004 Configurar PostgreSQL y Flyway para tablas de auditoría y logs de liquidación
- [ ] T005 Implementar infraestructura de errores centralizada (shared/errors/) y @RestControllerAdvice
- [ ] T006 Configurar cliente HTTP externo (WebClient/RestClient) con timeouts, circuit breaker base y logging estructurado
- [ ] T007 Crear entidades de dominio: Ticket.java, Evento.java, RegistroIngreso.java en domain/entities/
- [ ] T008 Definir puerto AccessControlRepository.java en domain/repositories/
- [ ] T009 Configurar trazabilidad de requests (Correlation ID), métricas base y logging JSON
  *Checkpoint:* Foundation ready - user story implementation can now begin in parallel

## Phase 3: User Story 1 - Consultar estado de ingreso (Priority: P1)
*Goal:* Exponer capacidad para obtener el estado de check-in de los tickets de un evento desde el servicio externo, aplicando reglas de negocio para tickets sin registro y garantizando integridad para liquidación.
*Independent Test:* Invocar GET /api/v1/eventos/{id}/estado-ingreso con ID válido. Validar respuesta con lista de tickets y estados (CHECKED_IN, NOT_ATTENDED). Verificar que eventos inexistentes retornen 404 y fallos externos retornen 502 con flag de bloqueo.

### Tests for User Story 1
- [ ] T010 [P] [US1] Contract test para endpoint en test/integration/EstadoIngresoApiContractTest.java
- [ ] T011 [P] [US1] Integration test con WireMock simulando respuestas parciales y caídas en test/integration/ConsultarEstadoIngresoIntegrationTest.java
- [ ] T012 [US1] Unit tests para ConsultarEstadoIngresoUseCase en test/unit/ConsultarEstadoIngresoUseCaseTest.java

### Implementation for User Story 1
- [ ] T013 [P] [US1] Crear DTOs Request/Response en application/dto/
- [ ] T014 [US1] Implementar AccessControlApiClient en infrastructure/external/ (mapeo a endpoint externo, paginación/batch si aplica)
- [ ] T015 [US1] Implementar ConsultarEstadoIngresoUseCase en application/usecase/ (validación evento, llamada externa, mapeo a CHECKED_IN/NOT_ATTENDED, validación SC-001)
- [ ] T016 [US1] Implementar EstadoIngresoController en infrastructure/interfaces/api/
- [ ] T017 [US1] Integrar manejo de errores: BusinessException (evento no existe) y TechnicalException (servicio no disponible/respuesta incompleta)
- [ ] T018 [US1] Añadir métricas de cobertura de respuesta y logs de auditoría para trazabilidad financiera
  *Checkpoint:* At this point, User Story 1 should be fully functional and testable independently

## Phase 4: Polish & Cross-Cutting Concerns
*Purpose:* Improvements that affect multiple user stories
- [ ] T019 Documentación OpenAPI/Swagger y guía de integración para módulo de liquidación
- [ ] T020 Implementar Retry Policy y Circuit Breaker robusto (Resilience4j) para resiliencia ante caídas del control de accesos
- [ ] T021 Añadir tests de concurrencia y carga batch en test/concurrency/ para validar timeouts y rate limiting
- [ ] T022 Hardening: validación estricta de inputs, sanitización de PII en logs, idempotencia de consultas
- [ ] T023 Code cleanup, optimización de mapeos DTO/Domain y revisión de arquitectura por pares
  *Checkpoint:* Feature ready for staging deployment and financial UAT

---

## Dependencies & Execution Order

### Phase Dependencies
- *Setup (Phase 1):* No dependencies - can start immediately
- *Foundational (Phase 2):* Depends on Setup completion - *BLOCKS* all user stories
- *User Stories (Phase 3):* All depend on Foundational phase completion. Proceed sequentially for this feature.
- *Polish (Phase 4):* Depends on User Story 1 completion and test validation

### User Story Dependencies
- *User Story 1 (P1):* Can start after Foundational (Phase 2). Standalone for this feature.
- *External Access Control Service:* Runtime dependency. Must be available/mocked during development. Abstracted correctly to not block build.

### Within Each User Story
1. DTOs & Domain Models before services
2. Repository Interface before External Client implementation
3. Core Use Case logic before Controller/Endpoint
4. Error handling & Logging after core implementation
5. Tests after implementation (or TDD if preferred)
6. Story complete before moving to Polish phase

### Notes
- [P] = Prioridad/Paralelizable, [US1] = Trazabilidad a Historia de Usuario 1
- SC-001 exige 100% de cobertura válida: el UseCase debe validar que la respuesta externa contenga estados para todos los tickets consultados. Si hay omisiones, lanzar TechnicalException con flag BLOCK_LIQUIDATION.
- Edge case "Sin registro" → mapeo automático a NOT_ATTENDED en el dominio. No se requiere intervención manual.
- Si el servicio externo falla (timeout, 5xx, red), el sistema debe fallar rápido y propagar bloqueo, cumpliendo el requisito de seguridad financiera.
- Commit after each task or logical group. Stop at any checkpoint to validate story independently.
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence.