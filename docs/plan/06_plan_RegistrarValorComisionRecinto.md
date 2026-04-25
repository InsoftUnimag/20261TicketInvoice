# Implementation Plan: Registrar Valor Comisión Recinto
**Date:** 24/04/2026  
**Spec:** `06_RegistrarValorComisionRecinto.md`

## Summary
Implementar un caso de uso y endpoint que permita al administrador financiero registrar (o actualizar) el valor de la comisión asociada a un recinto. Se soportarán dos tipos de comisión: porcentaje o valor fijo. Se aplicarán validaciones estrictas de existencia de recinto y valores numéricos válidos. Se utilizará Arquitectura Hexagonal.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Web, Spring Data JPA, RestClient, JUnit 5, Mockito, Jackson
- **Storage:** PostgreSQL (configurado - auditoría futura)
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Hexagonal Architecture)
- **Performance Goals:** <100ms p95 en registros
- **Constraints:** Validación de existencia de recinto, tipos de comisión predefinidos, valores numéricos positivos
- **Scale/Scope:** Registro por recinto, frecuencia media de escritura

## Project Structure

### Documentation (this feature)

```
docs/
├── specs/
│   └── 06_RegistrarValorComisionRecinto.md
└── plan/
    └── 06_plan_RegistrarValorComisionRecinto.md
```

### Source Code (Hexagonal Architecture)

```
src/
└── main/java/com/ticketevents/liquidation/

├── domain/                              ← CAPA 1: DOMINIO

│   ├── entities/
│   │   ├── TipoComision.java          (enum, nuevo)
│   │   └── ComisionRecinto.java        (entidad, nuevo)
│   └── repositories/
│       └── ComisionRecintoRepository.java (Puerto/Port, nuevo)

├── application/                         ← CAPA 2: APLICACIÓN

│   └── usecase/
│       └── RegistrarComisionRecintoUseCase.java (nuevo)

├── infrastructure/                    ← CAPA 3: INFRAESTRUCTURA

│   └── adapter/
│       ├── input/
│       │   └── rest/
│       │       ├── request/
│       │       │   └── RegistrarComisionRecintoRequest.java
│       │       └── response/
│       │           ├── RegistrarComisionRecintoResponse.java
│       │           └── ErrorResponse.java
│       └── output/
│           └── external/
│               └── dto/
│                   └── ComisionRecintoDto.java
│   ├── external/
│   │   ├── ComisionRecintoApiClient.java
│   │   └── MockComisionRecintoRepository.java (mock para pruebas)
│   ├── interfaces/
│   │   └── api/
│   │       ├── ComisionRecintoController.java
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

### Nuevas tareas para Spec 06
- [ ] T006 Crear enum `TipoComision.java` (PORCENTAJE, VALOR_FIJO)
- [ ] T007 Crear entidad `ComisionRecinto.java`
- [ ] T008 Definir puerto `ComisionRecintoRepository.java`

## Phase 3: User Story 1 - Registrar Valor Comisión Recinto (Priority: P1)
**Goal:** Permitir al administrador financiero registrar o actualizar el valor de la comisión para un recinto existente, eligiendo entre porcentaje o valor fijo.

**Independent Test:** Invocar `POST /api/v1/recintos/{id}/comision` con tipo y valor. Validar respuesta `201` con comisión guardada. Verificar `404` para recinto inexistente, `400` para valor inválido.

### Tests for User Story 1
- [ ] T009 Unit tests para RegistrarComisionRecintoUseCase

### Implementation for User Story 1
- [ ] T010 [US1] Crear Request/Response en infrastructure/adapter/input/rest/
- [ ] T011 [US1] Implementar MockComisionRecintoRepository
- [ ] T012 [US1] Implementar RegistrarComisionRecintoUseCase
- [ ] T013 [US1] Implementar ComisionRecintoController (POST endpoint)
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
- Tipos de comisión: PORCENTAJE (0.0-1.0) o VALOR_FIJO (monto fijo)
- Validar existencia del recinto antes de registrar
- Si el recinto ya tiene comisión, se actualiza (no crea duplicado)
- Mock disponible en MockComisionRecintoRepository para pruebas