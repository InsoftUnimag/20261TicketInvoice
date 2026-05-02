# Implementation Plan: Informar estado de ingreso de tickets
**Date:** 01/05/2026  
**Spec:** `02_InformarEstadoDeIngreso.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura para consultar el estado de ingreso de tickets por evento. El sistema valida que exista informacion de ingresos y que cada registro sea consistente antes de responder.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Data JPA
- **Storage:** PostgreSQL (configurado en el modulo, no obligatorio para el mock del flujo).
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Clean/Hexagonal Architecture)
- **Constraints:** Respuestas consistentes, rechazo de registros invalidos, manejo explicito de evento sin ingresos.

## Project Structure (implementado)

`backend/liquidation-events-services/src/main/java/com/ticketevents/liquidation/`

- `domain/entities/RegistroIngreso.java`
- `domain/entities/EstadoIngreso.java`
- `domain/repositories/AccessControlRepository.java`
- `application/usecase/ConsultarEstadoIngresoUseCase.java`
- `infrastructure/adapter/output/external/dto/EstadoIngresoDto.java`
- `infrastructure/external/MockAccessControlRepository.java`
- `infrastructure/interfaces/api/EstadoIngresoController.java`
- `shared/errors/ErrorCode.java`
- `shared/errors/BusinessException.java`
- `shared/errors/TechnicalException.java`

## User Story 1 - Informar estado de ingreso de tickets (P1)
**Goal:** Exponer endpoint para consultar estado de ingreso de tickets por evento.

**Endpoint implementado**
- `GET /api/v1/eventos/{id}/estado-ingreso`

**Comportamiento esperado**
1. Evento con ingresos: `200 OK` con detalle de estados.
2. Evento sin ingresos: `404` por `BusinessException` con `ErrorCode.EVENT_NOT_FOUND`.
3. Registros invalidos: error por `TechnicalException` con `ErrorCode.INVALID_REQUEST`.

## Notas de implementacion alineadas a codigo
- El flujo consulta `AccessControlRepository.getIngresosByEvento(...)`.
- Se valida nulidad y contenido de cada `RegistroIngreso`.
- El nombre de evento se resuelve en `obtenerNombreEvento(...)` del use case.
- El mapeo de salida se realiza con `EstadoIngresoMapper`.

## Docker (agregado para ejecucion portable)
Archivos:
- `backend/liquidation-events-services/Dockerfile`
- `backend/liquidation-events-services/docker-compose.estado-ingreso.yml`
- `backend/liquidation-events-services/src/main/resources/application-docker-estado-ingreso.properties`

Comando:
```bash
cd backend/liquidation-events-services
docker compose -f docker-compose.estado-ingreso.yml up --build
```

## Checklist de consistencia
- [x] Nombres de clases y paquetes alineados al codigo actual
- [x] Codigos de error alineados: `EVENT_NOT_FOUND`, `INVALID_REQUEST`
- [x] Version de Spring Boot alineada: `4.0.5`
- [x] Endpoint GET alineado con controlador actual
- [x] Soporte Docker documentado para ejecucion en cualquier PC
