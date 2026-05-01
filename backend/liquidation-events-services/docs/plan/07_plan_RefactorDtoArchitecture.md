# Implementation Plan: Refactor DTO Architecture
**Date:** 30/04/2026
**Spec:** `refactor-dtos-v4`

## Summary
Refactorizar la arquitectura de DTOs en el proyecto para cumplir con Arquitectura Hexagonal. Los Use Cases en la capa de Application deben retornar Application DTOs (ubicados en `infrastructure/adapter/output/external/dto/`), no Response DTOs ni entidades de dominio. Los Controllers deben convertir Application DTOs → Response DTOs usando Mappers.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en configuración
- **Constraints:** DTOs inmutables (records o clases con getters/setters), separación clara de capas

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── refactor-dtos-v4.md
└── plan/
    └── 07_plan_RefactorDtoArchitecture.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── ConfiguracionLiquidacion.java
│   │   ├── ComisionRecinto.java
│   │   ├── Recinto.java
│   │   ├── ResumenVentasEvento.java
│   │   └── RegistroIngreso.java
│   └── repositories/
│       ├── ConfiguracionLiquidacionRepository.java
│       ├── ComisionRecintoRepository.java
│       ├── RecintoRepository.java
│       └── AccessControlRepository.java

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── DeterminarTipoLiquidacionUseCase.java
│       ├── ConsultarResumenVentasUseCase.java
│       ├── ConsultarRecintoUseCase.java
│       ├── ConsultarEstadoIngresoUseCase.java
│       ├── ConsultarIngresosTicketsUseCase.java
│       └── RegistrarComisionRecintoUseCase.java

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   ├── DeterminarTipoLiquidacionRequest.java
│       │       │   ├── RegistrarComisionRecintoRequest.java
│       │       │   └── ConsultarEstadoIngresoRequest.java
│       │       └── response/
│       │           ├── DeterminarTipoLiquidacionResponse.java
│       │           ├── ConsultarResumenVentasResponse.java
│       │           ├── ConsultarRecintoResponse.java
│       │           ├── ConsultarEstadoIngresoResponse.java
│       │           ├── RegistrarComisionRecintoResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   ├── ConfiguracionLiquidacionDto.java  ← Application DTO
│                   ├── ComisionRecintoDto.java        ← Application DTO
│                   ├── RecintoDto.java                ← Application DTO
│                   ├── EventSnapshotDto.java           ← Application DTO
│                   ├── EstadoIngresoDto.java           ← Application DTO
│                   └── TicketIngresoDto.java          ← Application DTO
│   ├── interfaces/
│   │   └── api/
│   │       ├── ConfiguracionLiquidacionController.java
│   │       ├── ResumenVentasController.java
│   │       ├── RecintoController.java
│   │       ├── EstadoIngresoController.java
│   │       ├── ComisionRecintoController.java
│   │       └── GlobalExceptionHandler.java
│   └── mappers/
│       ├── ConfiguracionLiquidacionMapper.java  ← Application DTO → Response DTO
│       ├── ComisionRecintoMapper.java        ← Application DTO → Response DTO
│       ├── RecintoMapper.java                ← Application DTO → Response DTO
│       ├── ResumenVentasMapper.java           ← Application DTO → Response DTO
│       └── EstadoIngresoMapper.java           ← Application DTO → Response DTO

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
**Purpose:** Verificar estado actual
- [x] T001 Verificar Use Cases actuales y sus tipos de retorno
- [x] T002 Identificar DTOs existentes en infrastructure/adapter/output/external/dto/

## Phase 2: Foundational
**Purpose:** Configurar estructura de DTOs y Mappers
- [x] T003 Verificar que DTOs estén en infrastructure/adapter/output/external/dto/
- [x] T004 Crear EstadoIngresoDto.java (nuevo, para ConsultarEstadoIngreso)
- [x] T005 Actualizar Mappers para convertir Domain → Application DTO → Response DTO

## Phase 3: User Story 1 - Refactor Use Cases
**Goal:** Hacer que todos los Use Cases retornen Application DTOs

**Independent Test:** Ejecutar `mvn test` y verificar que todos los tests pasen (35 tests).

### Tests for User Story 1
- [x] T006 Unit tests para DeterminarTipoLiquidacionUseCase (actualizar mocks)
- [x] T007 Unit tests para ConsultarResumenVentasUseCase (actualizar mocks)
- [x] T008 Unit tests para ConsultarRecintoUseCase (actualizar mocks)
- [x] T009 Unit tests para ConsultarEstadoIngresoUseCase (actualizar mocks)
- [x] T010 Unit tests para RegistrarComisionRecintoUseCase (actualizar mocks)

### Implementation for User Story 1
- [x] T011 [US1] Actualizar DeterminarTipoLiquidacionUseCase para retornar ConfiguracionLiquidacionDto
- [x] T012 [US1] Actualizar ConsultarResumenVentasUseCase para retornar EventSnapshotDto
- [x] T013 [US1] Actualizar ConsultarRecintoUseCase para retornar RecintoDto
- [x] T014 [US1] Actualizar ConsultarEstadoIngresoUseCase para retornar EstadoIngresoDto
- [x] T015 [US1] Actualizar RegistrarComisionRecintoUseCase para retornar ComisionRecintoDto
- [x] T016 [US1] Actualizar Controllers para usar Mappers (Application DTO → Response DTO)

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Mejoras y validación final
- [x] T017 Ejecutar Maven compile y verificar 0 errores
- [x] T018 Ejecutar Maven test y verificar 35 tests pasan
- [ ] T019 Actualizar documentación en docs/plan/ para reflejar arquitectura correcta
- [ ] T020 Commit de cambios en rama refactor-dtos-v4

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo
- **Foundational:** Listo
- **User Stories:** Depende de Foundational
- **Polish:** Depende de User Story 1

### Within Each User Story
1. Entidades & Enums antes de repositorios
2. Interfaces de repositorio antes de implementaciones
3. Lógica de Use Case antes de Controller
4. Mappers después de Use Cases
5. Tests después de implementación

---

## Notes
- Los DTOs en `infrastructure/adapter/output/external/dto/` son clases regulares con getters/setters (no records)
- Los Use Cases retornan Application DTOs, no Response DTOs
- Los Controllers usan Mappers para convertir: Application DTO → Response DTO
- Request DTOs están en `infrastructure/adapter/input/rest/request/` y solo se usan para recibir datos del cliente
- Response DTOs están en `infrastructure/adapter/input/rest/response/` y son los que se retornan al cliente
- La rama de trabajo es `refactor-dtos-v4`

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → Controller → Use Case → Domain → Infrastructure
                            ↓
                    Application DTO (output/external/dto)
                            ↓
                    Mapper convierte: Application DTO → Response DTO
                            ↓
Cliente ← Response DTO ← Controller
```

**Ejemplo con ConsultarEstadoIngreso:**
1. Cliente envía GET `/api/v1/eventos/1/estado-ingreso`
2. `EstadoIngresoController` recibe el request
3. Llama a `ConsultarEstadoIngresoUseCase.execute(eventoId)`
4. Use Case retorna `EstadoIngresoDto`
5. Controller usa `EstadoIngresoMapper.toResponse(dto)` → `ConsultarEstadoIngresoResponse`
6. Cliente recibe `ConsultarEstadoIngresoResponse`
