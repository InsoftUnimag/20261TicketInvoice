# Implementation Plan: Consultar Valor Comisión Recinto
**Date:** 30/04/2026  
**Spec:** `07_ConsultarValorComisionRecinto.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador de recinto consultar el valor de la comisión asociada al recinto, para verificar las condiciones económicas previamente acordadas. Se debe retornar el tipo de comisión (porcentaje o valor fijo) y el valor configurado, utilizando Arquitectura Hexagonal con Application DTOs.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Validación de existencia de recinto, comisión configurada previamente

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 07_ConsultarValorComisionRecinto.md
└── plan/
    └── 07_plan_ConsultarValorComisionRecinto.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── ComisionRecinto.java      (ya existe)
│   │   └── Recinto.java              (ya existe)
│   └── repositories/
│       ├── ComisionRecintoRepository.java (ya existe)
│       └── RecintoRepository.java        (ya existe)

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── ConsultarValorComisionRecintoUseCase.java (nuevo)
│       └── RegistrarComisionRecintoUseCase.java (ya existe, relacionado)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarValorComisionRecintoRequest.java (nuevo, opcional)
│       │       └── response/
│       │           ├── ConsultarValorComisionRecintoResponse.java (nuevo)
│       │           └── ComisionRecintoResponse.java (ya existe, reusable)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── ComisionRecintoDto.java (ya existe)
│                   └── RecintoDto.java        (ya existe)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ValorComisionRecintoController.java (nuevo)
│   │       └── ComisionRecintoController.java  (ya existe, relacionado)
│   └── mappers/
│       ├── ValorComisionRecintoMapper.java (nuevo)
│       └── ComisionRecintoMapper.java      (ya existe, reusable)

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

## Phase 1: Setup
**Purpose:** Project initialization
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)
- [x] T006 Entidad ComisionRecinto.java (ya existe)
- [x] T007 Entidad Recinto.java (ya existe)
- [x] T008 Puerto ComisionRecintoRepository.java (ya existe)
- [x] T009 Puerto RecintoRepository.java (ya existe)

## Phase 3: User Story 1 - Consultar Valor Comisión Recinto
**Goal:** Permitir al administrador de recinto consultar el valor de la comisión asociada al recinto.

**Independent Test:** Consultar un recinto que tenga una comisión configurada previamente y validar que el sistema muestre correctamente el porcentaje o valor de comisión asociado.

### Tests for User Story 1
- [ ] T010 Unit tests para ConsultarValorComisionRecintoUseCase
- [ ] T011 Integration tests para endpoint

### Implementation for User Story 1
- [ ] T012 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T013 [US1] Implementar ConsultarValorComisionRecintoUseCase (retorna ComisionRecintoDto)
- [ ] T014 [US1] Implementar ValorComisionRecintoController (GET endpoint)
- [ ] T015 [US1] Implementar ValorComisionRecintoMapper (Application DTO → Response DTO)
- [ ] T016 [US1] Integrar manejo de errores
- [ ] T017 [US1] Tests unitarios

## Phase 4: User Story 2 - Recinto sin comisión registrada
**Goal:** Manejar el caso cuando se consulta la comisión de un recinto que no tiene comisión registrada.

**Independent Test:** Consultar la comisión de un recinto sin configurar y validar que el sistema informa que no tiene comisión registrada.

### Tests for User Story 2
- [ ] T018 Unit tests para caso sin comisión
- [ ] T019 Integration tests para error controlado

### Implementation for User Story 2
- [ ] T020 [US2] Actualizar Use Case para manejar caso sin comisión
- [ ] T021 [US2] Actualizar Response para incluir mensaje de "sin comisión"
- [ ] T022 [US2] Tests unitarios

## Phase 5: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T023 OpenAPI/Swagger documentation
- [ ] T024 Circuit Breaker (Resilience4j)
- [ ] T025 Tests de concurrencia
- [ ] T026 Validaciones más estrictas
- [ ] T027 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Listo (entidades y puertos ya existen)
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Stories

### Within Each User Story
1. Entidades & Enums antes de repositorios (ya completado)
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
- ComisionRecintoDto ya existe en `infrastructure/adapter/output/external/dto/`
- RegistrarComisionRecintoUseCase ya existe y puede reutilizarse para consulta

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → ValorComisionRecintoController → ConsultarValorComisionRecintoUseCase → Domain → Infrastructure
                                                                             ↓
                                                            ComisionRecintoDto (output/external/dto)
                                                                                        ↓
                                                            Mapper: ComisionRecintoDto → ConsultarValorComisionRecintoResponse
                                                                                        ↓
Cliente ← ConsultarValorComisionRecintoResponse ← ValorComisionRecintoController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/recintos/{id}/comision`
2. `ValorComisionRecintoController` recibe el request
3. Llama a `ConsultarValorComisionRecintoUseCase.execute(recintoId)`
4. Use Case retorna `ComisionRecintoDto` (o null si no tiene)
5. Controller usa `ValorComisionRecintoMapper.toResponse(dto)` → `ConsultarValorComisionRecintoResponse`
6. Cliente recibe el valor de comisión o mensaje de "sin comisión"

---

## Diferencia con RegistrarComisionRecintoUseCase (ya implementado)

| Característica | RegistrarComisionRecintoUseCase | ConsultarValorComisionRecintoUseCase |
|----------------|--------------------------------------|-------------------------------------------|
| Endpoint | `POST /api/v1/recintos/{id}/comision` | `GET /api/v1/recintos/{id}/comision` |
| Propósito | Registrar una nueva comisión | Consultar comisión existente |
| Retorno | `ComisionRecintoDto` | `ComisionRecintoDto` (o null) |
| Use Case existente | Sí (ya implementado) | No (nuevo) |
| Controller existente | Sí (ComisionRecintoController) | No (ValorComisionRecintoController) |
| Mapper existente | Sí (ComisionRecintoMapper) | Sí (reusable) |
