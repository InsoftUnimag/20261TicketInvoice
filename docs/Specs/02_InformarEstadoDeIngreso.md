# Feature Specification: Consultar estado de ingreso de tickets
Created: [DATE]

## User Scenarios & Testing (mandatory)

### User Story 1 - [Consultar estado de ingreso] (Priority: P1)

Como *módulo de liquidación y dispersión de fondos* quiero *consultar el estado de ingreso de los tickets vendidos para un evento desde el módulo de operación de eventos y control de accesos* para *determinar qué tickets fueron utilizados durante el evento y usar esta información en el cálculo de la liquidación final*.

*Why this priority*:  
El estado de ingreso de los tickets permite identificar si un ticket vendido fue efectivamente utilizado durante el evento. Esta información es necesaria para determinar la condición financiera final del ticket para el calculo del pago final y poder determinar los montos a dispersar.

*Independent Test*: Permitir visualizar el estado de ingreso del ticket dentro de los siguientes tipos:

- Check-in realizado (Asistió al evento)  
- Sin check-in registrado (No asistió al evento)

*Acceptance Scenarios*:

1. *Scenario: Ticket con check-in exitoso*
   - *Given* Dado que se vendió un ticket para un evento  
   - *When* Cuando el módulo de liquidación consulta el estado de ingreso del ticket  
   - *Then* Entonces el sistema recibe que el ticket tiene estado *Check-in realizado*, indicando que el asistente ingresó al evento  

2. *Scenario: Ticket sin check-in*
   - *Given* Dado que se vendió un ticket para un evento  
   - *When* Cuando el módulo de liquidación consulta el estado de ingreso del ticket  
   - *Then* Entonces el sistema recibe que el ticket *no tiene registro de check-in*, indicando que el asistente no ingresó al evento  

3. *Scenario: Consulta de estado de ingreso para evento inexistente*
   - *Given* Dado que se intenta consultar el estado de ingreso de un evento inexistente  
   - *When* Cuando el módulo de liquidación realiza la consulta  
   - *Then* Entonces el sistema debe retornar un error indicando que el evento no está registrado  

---

## Edge Cases

- ¿Qué pasaría si un ticket vendido no tiene información de ingreso registrada?

El sistema debe considerar el ticket como *no asistido* y continuar el proceso de cálculo de liquidación.

- ¿Qué pasaría si el servicio del módulo de control de accesos no está disponible?

El sistema debe registrar un error y bloquear el proceso de liquidación hasta obtener la información necesaria.

---

## Requirements (mandatory)

### Functional Requirements

*FR-001:* El sistema DEBE permitir consultar el estado de ingreso de los tickets asociados a un evento.

*FR-002:* El sistema DEBE consumir el endpoint REST expuesto por el módulo de *operación de eventos y control de accesos* para obtener el estado de ingreso de los tickets.

*FR-003:* El sistema DEBE recibir el estado de ingreso del ticket con los siguientes valores:

- Check-in realizado  
- Sin check-in registrado  

*FR-004:* El sistema DEBE utilizar esta información como insumo para el cálculo de la liquidación del evento.

*FR-005:* El sistema DEBE manejar errores cuando el evento consultado no exista o el servicio externo no esté disponible.

---

## Key Entities (include if feature involves data)

- *TicketIngreso*

Atributos:

idTicket  
idEvento  
estadoIngreso (CheckIn / SinCheckIn)  
fechaIngreso  

Relaciones:  
Representa el registro de ingreso de un ticket en el sistema de control de accesos y es utilizado por el módulo de liquidación para determinar si el ticket fue utilizado durante el evento.

---

## Success Criteria (mandatory)

### Measurable Outcomes

- *SC-001*: El 100% de los tickets consultados deben retornar un estado de ingreso válido antes de realizar el cálculo de liquidación.
