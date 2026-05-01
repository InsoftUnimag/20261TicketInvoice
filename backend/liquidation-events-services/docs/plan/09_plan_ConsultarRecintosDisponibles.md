# Implementation Plan: Consultar Recintos Disponibles
**Date:** 30/04/2026
**Spec:** `09_ConsultarRecintosDisponibles.md`

## Summary
Implementar un caso de uso y endpoint que permita consultar todos los recintos disponibles en el sistema, con sus características y estado. Se debe retornar la lista de recintos utilizando Arquitectura Hexagonal con Application DTOs.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Recintos activos e inactivos, datos completos de cada recinto

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 09_ConsultarRecintosDisponibles.md
└── plan/
    └── 09_plan_ConsultarRecintosDisponibles.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/
│   ├── entities/
│   │   ├── Recinto.java                     (ya existe)
│   │   └── TipoRecinto.java                 (enum, ya existe)
│   └── repositories/
│       └── RecintoRepository.java           (Puerto/Port, ya existe)

├── application/
│   └── usecase/
│       ├── ConsultarRecintosDisponiblesUseCase.java (nuevo)
│       └── ConsultarRecintoUseCase.java            (ya existe)

├── infrastructure/
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarRecintosDisponiblesRequest.java (nuevo)
│       │       └── response/
│       │           ├── ConsultarRecintosDisponiblesResponse.java (nuevo)
│       │           ├── RecintoDisponibleResponse.java (nuevo)
│       │           └── ConsultarRecintoResponse.java (ya existe)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── RecintoDto.java           (ya existe)
│                   └── RecintosDisponiblesDto.java (nuevo)
│   ├── external/
│   │   └── RecintoApiClient.java          (ya existe o nuevo)
│   ├── interfaces/
│   │   └── api/
│   │       ├── RecintosDisponiblesController.java (nuevo)
│   │       └── RecintoController.java            (ya existe)
│   └── mappers/
│       ├── RecintosDisponiblesMapper.java (nuevo)
│       └── RecintoMapper.java              (ya existe)
│
└── shared/
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

## Phase 1: Setup
**Purpose:** Project initialization
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)
- [x] T006 Entidad Recinto.java (ya existe)
- [x] T007 Enum TipoRecinto.java (ya existe)
- [x] T008 Puerto RecintoRepository.java (ya existe)

## Phase 3: User Story 1 - Consultar Recintos Disponibles
**Goal:** Permitir consultar todos los recintos disponibles en el sistema con sus características y estado.

**Independent Test:** Invocar `GET /api/v1/recintos`. Validar respuesta `200` con lista de recintos. Verificar que incluya información completa (nombre, tipo, tasa de comisión, estado).

### Tests for User Story 1
- [ ] T009 Unit tests para ConsultarRecintosDisponiblesUseCase
- [ ] T010 Integration tests para endpoint

### Implementation for User Story 1
- [ ] T011 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T012 [US1] Implementar RecintoApiClient o usar existente
- [ ] T013 [US1] Implementar ConsultarRecintosDisponiblesUseCase (retorna List<RecintoDto>)
- [ ] T014 [US1] Implementar RecintosDisponiblesController (GET endpoint)
- [ ] T015 [US1] Implementar RecintosDisponiblesMapper (Application DTO → Response DTO)
- [ ] T016 [US1] Integrar manejo de errores
- [ ] T017 [US1] Tests unitarios

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T018 OpenAPI/Swagger documentation
- [ ] T019 Circuit Breaker (Resilience4j)
- [ ] T020 Tests de concurrencia
- [ ] T021 Validaciones más estrictas
- [ ] T022 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo (entidades y puertos ya existen)
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

### Within Each User Story
1. Entidades antes de repositorios (ya completado)
2. Interfaces de repositorio antes de implementaciones (ya completado)
3. Lógica de Use Case antes de Controller
4. Mappers después de Use Cases
5. Tests después de implementación

---

## Notes
- `[P]` = Prioridad/Paralelizable, `[US1]` = Trazabilidad a Historia de Usuario 1
- Los Use Cases retornan Application DTOs (de `infrastructure/adapter/output/external/dto/`)
- Los Controllers usan Mappers para convertir: Application DTO → Response DTO
- Request DTOs están en `infrastructure/adapter/input/rest/request/`
- Response DTOs están en `infrastructure/adapter/input/rest/response/`
- RecintoDto ya existe en `infrastructure/adapter/output/external/dto/`
- ConsultarRecintoUseCase ya existe y retorna `RecintoDto` (puede reutilizarse)

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → RecintosDisponiblesController → ConsultarRecintosDisponiblesUseCase → Domain → Infrastructure
                                                                              ↓
                                            List<RecintoDto> (output/external/dto)
                                                                        ↓
                                            Mapper: List<RecintoDto> → ConsultarRecintosDisponiblesResponse
                                                                        ↓
Cliente ← ConsultarRecintosDisponiblesResponse ← RecintosDisponiblesController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/recintos`
2. `RecintosDisponiblesController` recibe el request
3. Llama a `ConsultarRecintosDisponiblesUseCase.execute()`
4. Use Case retorna `List<RecintoDto>` (usando RecintoRepository)
5. Controller usa `RecintosDisponiblesMapper.toResponse(dtos)` → `ConsultarRecintosDisponiblesResponse`
6. Cliente recibe lista de recintos con nombre, tipo, tasa de comisión y estado

---

## Diferencia con ConsultarRecintoUseCase (ya implementado)

| Característica | ConsultarRecintoUseCase | ConsultarRecintosDisponiblesUseCase |
|----------------|---------------------------|--------------------------------------|
| Endpoint | `GET /api/v1/recintos/{id}` | `GET /api/v1/recintos` |
| Retorno | `RecintoDto` (uno solo) | `List<RecintoDto>` (lista) |
| Propósito | Consultar un recinto específico | Consultar todos los recintos disponibles |
| Use Case existente | Sí (ya implementado) | No (nuevo) |
| Controller existente | Sí (RecintoController) | No (RecintosDisponiblesController) |
