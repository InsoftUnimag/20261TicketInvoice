# Feature Specification: Feature

**Created**: [21/02/2026]  

## User Scenarios & Testing *(mandatory)*


### User Story 1 - [Determinar Tipo liquidacion final] (Priority: P1)

Como administrador financiero, quiero poder registrar el tipo de liquidacion final que se va a usar para pagar los eventos donde se realizara , para que el sistema aplique el modelo correcto de calculo.

**Why this priority**: Es necesario definir los modelos de liquidacion, para establecer el valor de la comision de los recintos en el sistema.

**Independent Test**: 
Configurar un recinto con:
- Tarifa plana
- -Reparto de ingesos

Y validar que el sistema registre correctamente el tipo seleccionado.

**Acceptance Scenarios**:

1. **Scenario**: Configuracion de tarifa plana:
   - **Given** Dado que existe un evento asociado a un recinto.
   - **When** Cuando el administrador financiero selecciona "Tarifa Plana" define un monto fijo.
   - **Then** Entonces el sistema guarda el modelo financiero asociado al evento.

2. **Scenario**: Configuracion de reparto de ingresos
   - **Given** Dado que existe un evento asociado a un recinto.
   - **When** Cuando el administrador financiero selecciona "reparto de ingresos" define un porcentaje sobre la venta bruta.
   - **Then** Entonces el sistema guarda el modelo financiero asociado al evento.



---

#

[Add more user stories as needed, each with an assigned priority]

### Edge Cases

¿Que pasa cuando se configura el tipo de tarifa cuando el recinto no existe?

Dado que el recinto no exite, lanzar un mensaje de error al momento de seleccionar una opcion diciendo: "Error, no se puede asignar un tipo a un recinto no existente".

## Requirements *(mandatory)*


### Functional Requirements

-**FR-001**: El sistema DEBE permitir configurar el tipo de liquidación por evento

-**FR-002**: El sistema DEBE permitir configurar porcentaje de comisión por recinto




### Key Entities *(include if feature involves data)*

- **[Entity 1]**: Evento

  -idEvento

  -tipoLiquidacion

- **[Entity 2]**: Recinto

  -idRecinto

  -tipo

  -porcentajeComision


## Success Criteria *(mandatory)*


### Measurable Outcomes

- **SC-001**: 100% de eventos configurados pueden ser liquidados sin error"

- **SC-002**: 100% de liquidaciones para recintos deben tener un tipo asignado "

