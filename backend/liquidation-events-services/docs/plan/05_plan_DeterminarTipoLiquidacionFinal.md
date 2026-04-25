# Implementation Plan: Determinar Tipo Liquidación Final
**Date:** 24/04/2026  
**Spec:** `05_DeterminarTipoLiquidacionFinal.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador financiero configurar el tipo de liquidación final para un evento (TARIFA_PLANA o REPARTO_INGRESOS). Se aplicarán validaciones estrictas de existencia de evento, se permitirán dos modelos de cálculo: monto fijo o porcentaje sobre venta bruta. Se utilizará Arquitectura Hexagonal.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en configuración
- **Constraints:** Validación de existencia de evento, tipos de liquidación predefinidos
- **Scale/Scope:** Configuración por evento, baja frecuencia de escritura

## Project Structure

### Documentation (this feature)

```
docs/
├── specs/
│   └── 05_DeterminarTipoLiquidacionFinal.md
└── plan/
    └── 05_plan_DeterminarTipoLiquidacionFinal.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── TipoLiquidacion.java       (enum, nuevo)
│   │   └── ConfiguracionLiquidacion.java (entidad, nuevo)
│   └── repositories/
│       └── ConfiguracionLiquidacionRepository.java (Puerto/Port, nuevo)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── DeterminarTipoLiquidacionUseCase.java (nuevo)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── DeterminarTipoLiquidacionRequest.java
│       │       └── response/
│       │           ├── DeterminarTipoLiquidacionResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── ConfiguracionLiquidacionDto.java
│   ├── external/
│   │   ├── ConfiguracionLiquidacionApiClient.java
│   │   └── MockConfiguracionLiquidacionRepository.java (mock para pruebas)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ConfiguracionLiquidacionController.java
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
- [x] T001 Estructura de directorios (ya existe del proyecto base)
- [x] T002 Proyecto Java 21 con Spring Boot 4.0.5 (ya existe del proyecto base)
- [x] T003 Dependencias Maven (ya existe del proyecto base)

## Phase 2: Foundational
**Purpose:** Core infrastructure
- [x] T004 Infraestructura de errores (ya existe del Spec 01)
- [x] T005 Configuraciones base (ya existe del proyecto)

### Nuevas tareas para Spec 05
- [ ] T006 Crear enum `TipoLiquidacion.java` (TARIFA_PLANA, REPARTO_INGRESOS)
- [ ] T007 Crear entidad `ConfiguracionLiquidacion.java`
- [ ] T008 Definir puerto `ConfiguracionLiquidacionRepository.java`

## Phase 3: User Story 1 - Determinar Tipo Liquidación Final
**Goal:** Permitir al administrador financiero configurar el tipo de liquidación para un evento, eligiendo entre tarifa plana (monto fijo) o reparto de ingresos (porcentaje sobre venta bruta).

**Independent Test:** Invocar `POST /api/v1/eventos/{id}/configuracion-liquidacion` con tipo y valor. Validar respuesta `201` con configuración guardada. Verificar `404` para evento inexistente, `400` para datos inválidos.

### Tests for User Story 1
- [ ] T009 Unit tests para DeterminarTipoLiquidacionUseCase

### Implementation for User Story 1
- [ ] T010 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T011 [US1] Implementar MockConfiguracionLiquidacionRepository
- [ ] T012 [US1] Implementar DeterminarTipoLiquidacionUseCase
- [ ] T013 [US1] Implementar ConfiguracionLiquidacionController (POST endpoint)
- [ ] T014 [US1] Integrar manejo de errores
- [ ] T015 [US1] Tests unitarios

## Phase 4: Polish & Cross-Cutting Concerns
**Purpose:** Improvements
- [ ] T016 OpenAPI/Swagger
- [ ] T017 Circuit Breaker (Resilience4j)
- [ ] T018 Tests de concurrencia
- [ ] T019 Validaciones más estrictas
- [ ] T020 Code cleanup

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
- Tipos de liquidación: TARIFA_PLANA (monto fijo) o REPARTO_INGRESOS (porcentaje)
- Validar existencia del evento antes de configurar
- Mock disponible en MockConfiguracionLiquidacionRepository para pruebas