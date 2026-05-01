# Implementation Plan: Consultar Distribución del Recaudo
**Date:** 30/04/2026  
**Spec:** `09_ConsultarDistribucionDelRecaudo.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador financiero consultar la distribución del recaudo generado por un evento para validar que la liquidación se haya ejecutado correctamente según el modelo de negocio configurado. Se debe mostrar el detalle de montos distribuidos al promotor, comisión de plataforma, y estado de la liquidación.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en consulta
- **Constraints:** Evento en estado "Liquidado", distribución disponible, montos consistentes

## Project Structure

### Documentation (this feature)
```
docs/
├── specs/
│   └── 09_ConsultarDistribucionDelRecaudo.md
└── plan/
    └── 09_plan_ConsultarDistribucionDelRecaudo.md
```

### Source Code (Hexagonal Architecture)
```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO
│   ├── entities/
│   │   ├── DistribucionRecaudo.java  (de Spec 08)
│   │   └── Evento.java                 (ya existe, opcional)
│   └── repositories/
│       ├── DistribucionRecaudoRepository.java (de Spec 08)
│       └── EventoRepository.java             (ya existe, opcional)

├── application/                         ← CAPA 2: APLICACIÓN
│   └── usecase/
│       ├── ConsultarDistribucionRecaudoUseCase.java (nuevo)
│       └── CalcularDistribucionRecaudoUseCase.java (de Spec 08, relacionado)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA
│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── ConsultarDistribucionRequest.java (nuevo, opcional)
│       │       └── response/
│       │           ├── ConsultarDistribucionResponse.java (nuevo)
│       │           ├── DetalleDistribucionResponse.java (nuevo)
│       │           └── DistribucionRecaudoResponse.java (de Spec 08, reusable)
│       └── output/
│           └── external/
│               └── dto/
│                   ├── DistribucionRecaudoDto.java (de Spec 08)
│                   └── EventoDto.java           (nuevo, opcional)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ConsultarDistribucionController.java (nuevo)
│   │       └── CalcularDistribucionController.java (de Spec 08, relacionado)
│   └── mappers/
│       ├── ConsultarDistribucionMapper.java (nuevo)
│       └── DistribucionRecaudoMapper.java  (de Spec 08, reusable)

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
**Purpose:** Core infrastructure (depende de Spec 08)
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)
- [ ] T006 Entidad DistribucionRecaudo.java (de Spec 08)
- [ ] T007 Puerto DistribucionRecaudoRepository.java (de Spec 08)

## Phase 3: User Story 1 - Consultar Distribución del Recaudo de Evento Liquidado
**Goal:** Permitir al administrador financiero consultar la distribución del recaudo de un evento en estado "Liquidado".

**Independent Test:** Dado que un evento se encuentra en estado "Liquidado", cuando el administrador financiero consulta la distribución del recaudo, el sistema muestra el detalle de los montos distribuidos al promotor y comisión de plataforma.

### Tests for User Story 1
- [ ] T008 Unit tests para ConsultarDistribucionRecaudoUseCase
- [ ] T009 Integration tests para endpoint

### Implementation for User Story 1
- [ ] T010 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T011 [US1] Implementar ConsultarDistribucionRecaudoUseCase (retorna DistribucionRecaudoDto)
- [ ] T012 [US1] Implementar ConsultarDistribucionController (GET endpoint)
- [ ] T013 [US1] Implementar ConsultarDistribucionMapper (Application DTO → Response DTO)
- [ ] T014 [US1] Integrar manejo de errores
- [ ] T015 [US1] Tests unitarios

## Phase 4: User Story 2 - Consultar Distribución de Evento No Liquidado
**Goal:** Manejar el caso cuando se intenta consultar la distribución de un evento que no ha sido liquidado.

**Independent Test:** Dado que un evento no ha sido liquidado, cuando el administrador financiero intenta consultar la distribución del recaudo, el sistema muestra un mensaje indicando que el evento aún no tiene liquidación disponible.

### Tests for User Story 2
- [ ] T016 Unit tests para caso no liquidado
- [ ] T017 Integration tests para error controlado

### Implementation for User Story 2
- [ ] T018 [US2] Actualizar Use Case para validar estado "Liquidado"
- [ ] T019 [US2] Retornar mensaje de "sin liquidación disponible"
- [ ] T020 [US2] Tests unitarios

## Phase 5: Edge Cases
**Purpose:** Manejo de casos especiales

### Tests for Edge Cases
- [ ] T021 Unit tests: Evento no existe (error 404)
- [ ] T022 Unit tests: Inconsistencias en montos calculados (bloquear consulta)
- [ ] T023 Unit tests: Montos no modificables después de consultados

### Implementation for Edge Cases
- [ ] T024 [EC] Validar existencia de evento
- [ ] T025 [EC] Validar consistencia de montos
- [ ] T026 [EC] Bloquear modificaciones posteriores

## Phase 6: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T027 OpenAPI/Swagger documentation
- [ ] T028 Circuit Breaker (Resilience4j)
- [ ] T029 Tests de concurrencia
- [ ] T030 Validaciones más estrictas
- [ ] T031 Code cleanup

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup:** Listo (proyecto base)
- **Foundational:** Depende de Spec 08 (DistribucionRecaudo entity)
- **User Stories:** Depende de Foundational
- **Edge Cases:** Depende de User Stories
- **Polish:** Depende de todo lo anterior

### Within Each User Story
1. Entidades antes de repositorios (de Spec 08)
2. Interfaces de repositorio antes de implementaciones
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
- DistribucionRecaudoDto debe existir de Spec 08
- El sistema debe validar que el evento esté en estado "Liquidado"
- Los montos mostrados deben ser 100% consistentes con la liquidación generada

---

## Flujo de Trabajo (Hexagonal Architecture)

```
Cliente → ConsultarDistribucionController → ConsultarDistribucionRecaudoUseCase → Domain → Infrastructure
                                                                                         ↓
                                                                DistribucionRecaudoDto (output/external/dto)
                                                                                                    ↓
                                                                Mapper: DistribucionRecaudoDto → ConsultarDistribucionResponse
                                                                                                    ↓
Cliente ← ConsultarDistribucionResponse ← ConsultarDistribucionController
```

**Ejemplo:**
1. Cliente envía GET `/api/v1/eventos/{id}/distribucion-recaudo`
2. `ConsultarDistribucionController` recibe el request
3. Llama a `ConsultarDistribucionRecaudoUseCase.execute(eventoId)`
4. Use Case valida que el evento esté en estado "Liquidado"
5. Use Case retorna `DistribucionRecaudoDto` con:
   - Total pago al promotor
   - Total comisión plataforma
   - Estado de la liquidación
6. Controller usa `ConsultarDistribucionMapper.toResponse(dto)` → `ConsultarDistribucionResponse`
7. Cliente recibe el detalle de montos distribuidos

---

## Diferencia con CalcularDistribucionDelRecaudo (Spec 08)

| Característica | Calcular (Spec 08) | Consultar (Spec 09) |
|----------------|----------------------|-----------------------|
| Endpoint | `POST /api/v1/eventos/{id}/calcular-distribucion` | `GET /api/v1/eventos/{id}/distribucion-recaudo` |
| Propósito | Calcular distribución preliminar | Consultar distribución ya liquidada |
| Estado evento | Finalizado (sin estado específico) | "Liquidado" (validación estricta) |
| Retorno | `DistribucionRecaudoDto` (preliminar) | `DistribucionRecaudoDto` (consolidado) |
| Use Case | CalcularDistribucionRecaudoUseCase | ConsultarDistribucionRecaudoUseCase |
| Controller | CalcularDistribucionController | ConsultarDistribucionController |
| Mapper | DistribucionRecaudoMapper | ConsultarDistribucionMapper |

---

## Criterios de Consistencia (Success Criteria)

- **SC-001**: El 100% de las consultas de distribución deben mostrar montos consistentes con la liquidación generada.
- **SC-002**: El 100% de los eventos en estado "Liquidado" deben tener una distribución disponible para consulta.
- **SC-003**: El sistema debe bloquear consultas de eventos no liquidados.
- **SC-004**: Una vez consultada la distribución, los datos no deben permitir modificaciones.
