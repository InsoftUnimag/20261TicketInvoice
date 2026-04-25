# Implementation Plan: Informar tipo de recinto y asignación de tasas
*Date:* 17/04/2026
*Spec:* Informar_tipo_de_recinto.md

## Summary
Implementar la gestión de creación y consulta de recintos con asignación obligatoria de tipo (ESTADIO / TEATRO) y cálculo automático de la tasa de comisión correspondiente. Se aplicará Arquitectura Hexagonal con persistencia JPA, reglas de dominio estrictas para inmutabilidad post-creación, validación de permisos administrativos para excepciones y trazabilidad auditiva completa, garantizando el 100% de integridad financiera en liquidaciones (SC-001, SC-002, SC-003).

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado - auditoria futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en creación/consulta
- **Constraints:** Inmutabilidad de tipoRecinto tras creación, asignación automática de tasas
- **Scale/Scope:** Catálogo de recintos, baja frecuencia de escritura, alta lectura

## Project Structure

### Documentation (this feature)

```
docs/
├── specs/
│   └── 03_InformarTipoDeRecinto.md
└── plan/
    └── 03_plan_informar-tipo-de-recinto.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── Recinto.java              (nuevo)
│   │   ├── TipoRecinto.java         (enum, nuevo)
│   │   └── ConfiguracionComision.java
│   └── repositories/
│       └── RecintoRepository.java     (Puerto/Port, nuevo)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── ConsultarRecintoUseCase.java (nuevo)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarRecintoRequest.java
│       │       └── response/
│       │           ├── ConsultarRecintoResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── RecintoDto.java
│   ├── external/
│   │   ├── RecintoApiClient.java
│   │   └── MockRecintoRepository.java (mock para pruebas)
│   ├── interfaces/
│   │   └── api/
│   │       ├── RecintoController.java
│   │       └── GlobalExceptionHandler.java
│   └── config/
│       └── RestClientConfig.java

└── shared/                             ← CAPA 4: COMPARTIDO

    └── errors/
        ├── ErrorCode.java
        ├── BusinessException.java
        └── TechnicalException.java
```


*Structure Decision:* Se adopta la arquitectura Hexagonal/Clean v2 corregida. La entidad Recinto gestiona el estado y la inmutabilidad del tipo. ConfiguracionComision centraliza las tasas por tipo para evitar hardcoding. La capa infrastructure/persistence implementa los puertos JPA. La validación de permisos y auditoría se integra en el UseCase y se delega a Spring Security + interceptores de auditoría. Los tests se segregan en unit/, integration/ y concurrency/ para validar reglas de negocio, flujos completos con DB embebida y bloqueos bajo carga.

---

## Phase 1: Setup
**Purpose:** Project initialization
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)

### Nuevas tareas para Spec 03
- [ ] T006 Crear entidad `Recinto.java` en domain/entities/
- [ ] T007 Crear enum `TipoRecinto.java` en domain/entities/
- [ ] T008 Definir puerto `RecintoRepository.java` en domain/repositories/

## Phase 3: User Story 1 - Consultar tipo de recinto
**Goal:** Exponer endpoint para consultar el tipo de recinto y su tasa de comisión.

**Independent Test:** Invocar GET /api/v1/recintos/{id} con ID válido. Validar respuesta con tipo de recinto y tasa de comisión.

### Tests for User Story 1
- [ ] T009 Unit tests para ConsultarRecintoUseCase

### Implementation for User Story 1
- [ ] T010 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T011 [US1] Implementar RecintoApiClient en infrastructure/external/
- [ ] T012 [US1] Implementar MockRecintoRepository
- [ ] T013 [US1] Implementar ConsultarRecintoUseCase
- [ ] T014 [US1] Implementar RecintoController
- [ ] T015 [US1] Integrar manejo de errores
- [ ] T016 [US1] Tests unitarios

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T017 OpenAPI/Swagger
- [ ] T018 Circuit Breaker (Resilience4j)
- [ ] T019 Tests de concurrencia
- [ ] T020 Validaciones más estrictas
- [ ] T021 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo + tareas nuevas
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

### Within Each User Story
1. Entidades & Enums antes de repositorios
2. Interfaces de repositorio antes de implementaciones
3. Lógica de Use Case antes de Controller
4. Manejo de errores después de lógica de negocio
5. Tests después de implementación

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- El tipo de recinto (ESTADIO/TEATRO) determina la tasa de comisión
- Inmutabilidad: tipoRecinto se congelar tras creación
- Mock disponible en MockRecintoRepository para pruebas sin Módulo 1