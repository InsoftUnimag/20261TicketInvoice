# Implementation Plan: Informar estado de ingreso de tickets
**Date:** 21/04/2026  
**Spec:** `02_InformarEstadoDeIngreso.md`

## Summary
Implementar un caso de uso y endpoint que permita al módulo de liquidación consultar el estado de ingreso (check-in) de los tickets asociados a un evento, consumiendo el servicio externo del módulo de operación y control de accesos. Se aplicará Arquitectura Hexagonal, abstracción del cliente HTTP, mapeo de estados (CHECKED_IN / NOT_ATTENDED), manejo de ausencia de datos como "No asistido" y bloqueo explícito del flujo de liquidación ante fallos externos o respuestas incompletas (SC-001).

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado pero no usado en Spec 02 - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <500ms p95 por consulta batch, 100% de tasa de respuestas válidas (SC-001)
- **Constraints:** Tolerancia cero a inconsistencias financieras, fallback a NOT_ATTENDED si no hay registro, bloqueo de liquidación si servicio externo falla
- **Scale/Scope:** Consultas por evento (cientos/miles de tickets), batch processing

## Project Structure

### Documentation (this feature)

```
docs/
├── specs/
│   └── 02_InformarEstadoDeIngreso.md
└── plan/
    └── 02_plan_InformarEstadoDeIngreso.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── RegistroIngreso.java        (nuevo)
│   │   ├── EstadoIngreso.java         (enum, nuevo)
│   │   ├── Evento.java              (existente)
│   │   └── Ticket.java              (existente)
│   └── repositories/
│       └── AccessControlRepository.java (Puerto/Port, nuevo)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── ConsultarEstadoIngresoUseCase.java (nuevo)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarEstadoIngresoRequest.java
│       │       └── response/
│       │           ├── ConsultarEstadoIngresoResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── EventSnapshotDto.java
│   ├── external/
│   │   ├── AccessControlApiClient.java
│   │   └── MockAccessControlRepository.java (mock para pruebas)
│   ├── interfaces/
│   │   └── api/
│   │       ├── EstadoIngresoController.java
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
- [x] T001 Estructura de directorios (ya existe del Spec 01)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del Spec 01)
- [x] T003 Dependencias Maven (ya existe del Spec 01)

## Phase 2: Foundational
**Purpose:** Core infrastructure para Spec 02
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 RestClient (ya existe del Spec 01)
- [x] T006 Entidades de dominio Evento, Ticket (ya existen del Spec 01)
- [x] T007 Puerto AccessControlRepository (definir)

### Nuevas tareas para Spec 02
- [x] T008 Crear entidad `RegistroIngreso.java` en domain/entities/
- [x] T009 Crear enum `EstadoIngreso.java` en domain/entities/
- [x] T010 Definir puerto `AccessControlRepository.java` en domain/repositories/

## Phase 3: User Story 1 - Consultar estado de ingreso
**Goal:** Exponer capacidad para obtener el estado de check-in de los tickets de un evento desde el servicio externo, aplicando reglas de negocio para tickets sin registro y garantizando integridad para liquidación.

**Independent Test:** Invocar GET /api/v1/eventos/{id}/estado-ingreso con ID válido. Validar respuesta con lista de tickets y estados (CHECKED_IN, NOT_ATTENDED). Verificar errores 404 (evento inexistente) y 502 (servicio caído).

### Tests for User Story 1
- [x] T011 Unit tests para ConsultarEstadoIngresoUseCase

### Implementation for User Story 1
- [x] T012 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [x] T013 [US1] Implementar AccessControlApiClient en infrastructure/external/
- [x] T014 [US1] Implementar MockAccessControlRepository
- [x] T015 [US1] Implementar ConsultarEstadoIngresoUseCase
- [x] T016 [US1] Implementar EstadoIngresoController
- [x] T017 [US1] Integrar manejo de errores
- [x] T018 [US1] Tests unitarios

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
- **Setup:** Listo (Spec 01)
- **Foundational:** Listo (Spec 01) + tareas nuevas para Spec 02
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

### Within Each User Story
1. DTOs & Domain Models before services
2. Repository Interface before External Client implementation
3. Core Use Case logic before Controller/Endpoint
4. Error handling & Logging after core implementation
5. Tests after implementation

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- SC-001 exige 100% de cobertura válida: el UseCase debe validar que la respuesta externa contenga estados para todos los tickets
- Edge case "Sin registro" → mapeo automático a NOT_ATTENDED en el dominio
- Si el servicio externo falla (timeout, 5xx, red), bloqueo de liquidación
- Mock disponible en MockAccessControlRepository para pruebas sin Módulo 2