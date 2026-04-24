# Implementation Plan: Consultar resumen de ventas del evento
**Date:** 14/04/2026  
**Spec:** `Consultar_resumen_de_ventas_del_evento.md`

## Summary
Implementar un caso de uso y endpoint que permita al módulo de liquidación consultar el resumen consolidado de ventas de un evento cerrado, consumiendo internamente el servicio externo `/eventos/{id}/snapshot` del módulo de gestión de recintos. Se aplicará Arquitectura Hexagonal, abstracción del cliente HTTP externo, validación explícita del estado del evento y manejo estructurado de errores para garantizar la trazabilidad financiera y el bloqueo de liquidación ante fallos (SC-001, SC-003).

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado pero no usado en Spec 01 - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <1s p95 en llamadas externas, 100% de éxito para eventos cerrados (SC-001)
- **Constraints:** Tolerancia a fallos del servicio externo, validación estricta de estado `CERRADO`, bloqueo explícito de liquidación si falla (SC-003)
- **Scale/Scope:** Consultas on-demand por evento, picos de concurrencia durante ventanas de cierre/liquidación

## Project Structure

### Documentation (this feature)

```
docs/
└── plan/
    ├── plan-template.md
    └── 01_Plan_ConsultarResumenDeVentas.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── Evento.java
│   │   ├── Ticket.java
│   │   ├── ResumenVentasEvento.java    (Entidad/Value Object según spec)
│   │   ├── EstadoEvento.java           (enum)
│   │   └── CondicionLiquidacion.java  (enum)
│   └── repositories/
│       └── EventSnapshotRepository.java  (Puerto/Port)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── ConsultarResumenVentasUseCase.java

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarResumenVentasRequest.java
│       │       └── response/
│       │           ├── ConsultarResumenVentasResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── EventSnapshotDto.java   (DTO para API externa)
│   ├── external/
│   │   ├── EventSnapshotApiClient.java
│   │   └── MockEventSnapshotRepository.java
│   ├── interfaces/
│   │   └── api/
│   │       ├── ResumenVentasController.java
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
**Purpose:** Project initialization and basic structure
- [x] T001 Crear estructura de directorios según Project Structure
- [x] T002 Inicializar proyecto Java 21 con Spring Boot 4.0.5
- [x] T003 Configurar dependencias Maven

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Configurar PostgreSQL (application.properties)
- [x] T005 Implementar infraestructura de errores (shared/errors/)
- [x] T006 Configurar RestClient
- [x] T007 Crear entidades de dominio
- [x] T008 Definir puerto EventSnapshotRepository
- [ ] T009 Logging estructurado (pendiente)

## Phase 3: User Story 1 - Consultar resumen de ventas
**Goal:** Exponer capacidad para obtener el consolidado de ventas de un evento cerrado desde el servicio externo, validando estado y manejando fallos externos. Independent Test: Invocar GET /api/v1/eventos/{id}/resumen-ventas con un ID válido y estado CERRADO; validar respuesta JSON con conteos por condición y total recaudado. Validar errores 404 (evento inexistente), 409 (evento no cerrado) y 502 (servicio externo caído).

### Tests for User Story 1
- [ ] T010 Contract test para endpoint
- [ ] T011 Integration test con WireMock
- [x] T012 Unit tests para UseCase

### Implementation for User Story 1
- [x] T013 [US1] Crear Request/Response en infrastructure/dto/
- [x] T014 [US1] Implementar EventSnapshotApiClient
- [x] T015 [US1] Implementar ConsultarResumenVentasUseCase
- [x] T016 [US1] Implementar ResumenVentasController
- [x] T017 [US1] Integrar manejo de errores
- [ ] T018 [US1] Métricas y logs

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T019 Documentación OpenAPI/Swagger
- [ ] T020 Circuit Breaker (Resilience4j)
- [ ] T021 Tests de concurrencia
- [ ] T022 Validaciones más estrictas (@Valid, constraints)
- [ ] T023 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Can start immediately
- **Foundational:** Blocks all user stories
- **User Stories:** Depend on Foundational
- **Polish:** Depends on User Story 1

### Within Each User Story
1. DTOs & Domain Models before services
2. Repository Interface before External Client implementation
3. Core Use Case logic before Controller/Endpoint
4. Error handling & Logging after core implementation
5. Tests after implementation

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- El resumen se calcula dinámicamente. No se persiste ResumenVentasEvento
- SC-003 exige bloqueo de液态ación: el UseCase lanza excepciones no recuperables
- Validar explícitamente estadoEvento == CERRADO
- Mock disponible en MockEventSnapshotRepository para pruebas sin Módulo 1

---
