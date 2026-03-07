# Feature Specification: [Registrar valor comision recinto]

**Created**: [DATE]  

## User Scenarios & Testing *(mandatory)*

### User Story 1 - [Registrar valor comision recinto] (Priority: P1)

Como **administrador financiero** quiero **registrar el valor de la comisión asociada a un recinto** para que el sistema pueda aplicar las condiciones económicas establecidas en el acuerdo durante el proceso de liquidación de los eventos.

**Why this priority**: Es necesario registrar el valor de la comisión del recinto para garantizar que el sistema pueda calcular correctamente la distribución del recaudo según las condiciones económicas establecidas.

**Independent Test**: Permitir registrar el valor de la comisión para un recinto mostrando:

- Identificador del recinto  
- Tipo de comisión (porcentaje o valor fijo)  
- Valor de la comisión  
- Fecha de registro  

**Acceptance Scenarios**:

1. **Scenario**: Registro de comisión para un recinto existente  
   - **Given** Dado que existe un recinto registrado en el sistema  
   - **When** Cuando el administrador financiero registra el valor de la comisión del recinto  
   - **Then** Entonces el sistema guarda el valor de la comisión asociado al recinto  

2. **Scenario**: Actualización de comisión de un recinto  
   - **Given** Dado que el recinto ya tiene una comisión registrada  
   - **When** Cuando el administrador financiero registra un nuevo valor de comisión  
   - **Then** Entonces el sistema actualiza la comisión asociada al recinto  

---

### Edge Cases

- **¿Qué pasa si se intenta registrar la comisión para un recinto que no existe?**  
El sistema rechaza la operación e informa que el recinto no está registrado.

- **¿Qué pasa si el valor de comisión ingresado es inválido (negativo o vacío)?**  
El sistema rechaza el registro y solicita ingresar un valor válido.

- **¿Qué pasa si el recinto ya tiene eventos previamente liquidados?**  
El sistema debe registrar la nueva comisión sin afectar las liquidaciones ya ejecutadas.

---

## Requirements *(mandatory)*

### Functional Requirements

**FR-001:** El sistema DEBE permitir registrar el valor de la comisión para un recinto.

**FR-002:** El sistema DEBE validar que el recinto exista antes de registrar la comisión.

**FR-003:** El sistema DEBE validar que el valor de la comisión sea un valor numérico válido.

**FR-004:** El sistema DEBE permitir actualizar el valor de la comisión cuando se registre una nueva configuración.

**FR-005:** El sistema DEBE almacenar el valor de la comisión asociado al recinto para su uso en el cálculo de liquidaciones.

---

## Key Entities *(include if feature involves data)*

### **Recinto**

Atributos:

idRecinto  
nombreRecinto  
tipoRecinto (Estadio / Teatro)  

### **ComisionRecinto**

Atributos:

idComision  
idRecinto  
tipoComision (Porcentaje / ValorFijo)  
valorComision  
fechaRegistro  

Relaciones:

Un recinto puede tener una configuración de comisión asociada utilizada en el cálculo de liquidaciones.

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

**SC-001:** El 100% de los recintos utilizados en eventos deben tener una comisión registrada antes de ejecutar la liquidación.

**SC-002:** El sistema debe validar correctamente el valor de comisión en el 100% de los registros.
