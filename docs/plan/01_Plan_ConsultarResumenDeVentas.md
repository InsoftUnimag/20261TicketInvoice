# Implementation Plan: Consultar resumen de ventas del evento
**Date:** 01/05/2026  
**Spec:** `Consultar_resumen_de_ventas_del_evento.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura para consultar el resumen consolidado de ventas de un evento. El sistema valida que el evento exista, que el snapshot sea consistente y que el estado del evento sea `CERRADO`.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Data JPA
- **Storage:** PostgreSQL (configurado en el modulo, no obligatorio para el mock del flujo).
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Clean/Hexagonal Architecture)
- **Constraints:** Respuestas deterministas, bloqueo del flujo ante estado no cerrado o snapshot invalido.

## Project Structure (implementado)

`backend/liquidation-events-services/src/main/java/com/ticketevents/liquidation/`

- `domain/entities/ResumenVentasEvento.java`
- `domain/repositories/EventSnapshotRepository.java`
- `application/usecase/ConsultarResumenVentasUseCase.java`
- `infrastructure/adapter/output/external/dto/EventSnapshotDto.java`
- `infrastructure/external/MockEventSnapshotRepository.java`
- `infrastructure/interfaces/api/ResumenVentasController.java`
- `shared/errors/ErrorCode.java`
- `shared/errors/BusinessException.java`
- `shared/errors/TechnicalException.java`

## User Story 1 - Consultar resumen de ventas del evento (P1)
**Goal:** Exponer endpoint para consultar resumen de ventas de un evento, validando estado `CERRADO`.

**Endpoint implementado**
- `GET /api/v1/eventos/{id}/resumen-ventas`

**Comportamiento esperado**
1. Evento existente y cerrado: `200 OK` con resumen de ventas.
2. Evento inexistente: `404` por `BusinessException` con `ErrorCode.EVENT_NOT_FOUND`.
3. Evento no cerrado: `409` por `BusinessException` con `ErrorCode.EVENT_NOT_CLOSED`.

## Notas de implementacion alineadas a codigo
- El flujo consulta `EventSnapshotRepository.getSnapshot(...)`.
- Se valida `snapshot.validar()` antes del mapeo.
- Se valida estado de evento con `esEstadoCerrado(...)` en el use case.
- El mapeo de salida se realiza con `ResumenVentasMapper`.

## Docker (agregado para ejecucion portable)
Archivos:
- `backend/liquidation-events-services/Dockerfile`
- `backend/liquidation-events-services/docker-compose.resumen-ventas.yml`
- `backend/liquidation-events-services/src/main/resources/application-docker-resumen.properties`

Comando:
```bash
cd backend/liquidation-events-services
docker compose -f docker-compose.resumen-ventas.yml up --build
```

## Checklist de consistencia
- [x] Nombres de clases y paquetes alineados al codigo actual
- [x] Codigos de error alineados: `EVENT_NOT_FOUND`, `EVENT_NOT_CLOSED`
- [x] Version de Spring Boot alineada: `4.0.5`
- [x] Endpoint GET alineado con controlador actual
- [x] Soporte Docker documentado para ejecucion en cualquier PC
