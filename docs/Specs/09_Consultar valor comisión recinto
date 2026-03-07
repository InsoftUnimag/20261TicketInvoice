# Feature Specification: Feature

**Created**: [21/02/2026]  

## User Scenarios & Testing *(mandatory)*

### User Story 1 - [Consultar valor comisión recinto] (Priority: P1)

Como administrador de recinto, quiero poder consultar el valor de la comisión asociada al recinto, para verificar que se registraron las condiciones económicas previamente acordadas para los eventos realizados en ese recinto.

**Why this priority**: Permite verificar las condiciones económicas configuradas para el recinto antes de realizar cálculos de liquidación o distribución del recaudo.

**Independent Test**:  
Consultar un recinto que tenga una comisión configurada previamente y validar que el sistema muestre correctamente el porcentaje o valor de comisión asociado.

**Acceptance Scenarios**:

1. **Scenario**: Consulta exitosa de comisión del recinto
   - **Given** Dado que existe un recinto registrado en el sistema con una comisión definida.
   - **When** Cuando el administrador de recinto consulta el valor de comisión del recinto.
   - **Then** Entonces el sistema muestra el valor de la comisión configurada para ese recinto.

2. **Scenario**: Recinto sin comisión registrada
   - **Given** Dado que existe un recinto registrado en el sistema.
   - **When** Cuando el administrador consulta la comisión del recinto y no existe una comisión configurada.
   - **Then** Entonces el sistema informa que el recinto no tiene una comisión registrada.

---

#

### Edge Cases

¿Que pasa cuando se intenta consultar la comisión de un recinto que no existe?

Dado que el recinto no existe, el sistema debe mostrar un mensaje de error indicando que el recinto no está registrado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir consultar el valor de comisión asociado a un recinto.

- **FR-002**: El sistema MUST mostrar el tipo de comisión configurado (porcentaje o valor fijo).

- **FR-003**: El sistema MUST informar cuando un recinto no tenga una comisión registrada.

---

### Key Entities *(include if feature involves data)*

- **[Entity 1]**: Recinto

  - idRecinto  
  - tipo  
  - porcentajeComision  

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% de las consultas de comisión deben mostrar correctamente el valor configurado para el recinto.

- **SC-002**: 100% de las consultas a recintos inexistentes deben generar un mensaje de error controlado.
