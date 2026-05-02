# Implementation Plan: Informar tipo de recinto y tasa de comision
**Date:** 01/05/2026  
**Spec:** `03_InformarTipoDeRecinto.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura para consultar informacion de recinto por ID, incluyendo tipo y tasa de comision. El sistema valida existencia y coherencia basica de la tasa de comision.

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Data JPA
- **Storage:** PostgreSQL (configurado en el modulo, no obligatorio para el mock del flujo).
- **Testing:** JUnit 5, Mockito
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Clean/Hexagonal Architecture)
- **Constraints:** Respuestas consistentes, validacion de tasa de comision, error explicito para recinto inexistente.

## Project Structure (implementado)

`backend/liquidation-events-services/src/main/java/com/ticketevents/liquidation/`

- `domain/entities/Recinto.java`
- `domain/entities/TipoRecinto.java`
- `domain/repositories/RecintoRepository.java`
- `application/usecase/ConsultarRecintoUseCase.java`
- `infrastructure/adapter/output/external/dto/RecintoDto.java`
- `infrastructure/external/MockRecintoRepository.java`
- `infrastructure/interfaces/api/RecintoController.java`
- `shared/errors/ErrorCode.java`
- `shared/errors/BusinessException.java`
- `shared/errors/TechnicalException.java`

## User Story 1 - Informar tipo de recinto y tasa de comision (P1)
**Goal:** Exponer endpoint para consultar tipo y tasa de comision de un recinto.

**Endpoint implementado**
- `GET /api/v1/recintos/{id}`

**Comportamiento esperado**
1. Recinto existente: `200 OK` con datos del recinto.
2. Recinto inexistente: `404` por `BusinessException` con `ErrorCode.RECINTO_NOT_FOUND`.
3. Recinto con tasa invalida: error por `TechnicalException` con `ErrorCode.INVALID_REQUEST`.

## Notas de implementacion alineadas a codigo
- El flujo consulta `RecintoRepository.findById(...)`.
- Se valida nulidad del recinto y se controla `RECINTO_NOT_FOUND`.
- Se valida coherencia de tasa en `validarRecinto(...)` del use case.
- El mapeo de salida se realiza con `RecintoMapper`.

## Docker (agregado para ejecucion portable)
Archivos:
- `backend/liquidation-events-services/Dockerfile`
- `backend/liquidation-events-services/docker-compose.recinto.yml`
- `backend/liquidation-events-services/src/main/resources/application-docker-recinto.properties`

Comando:
```bash
cd backend/liquidation-events-services
docker compose -f docker-compose.recinto.yml up --build
```

## Checklist de consistencia
- [x] Nombres de clases y paquetes alineados al codigo actual
- [x] Codigos de error alineados: `RECINTO_NOT_FOUND`, `INVALID_REQUEST`
- [x] Version de Spring Boot alineada: `4.0.5`
- [x] Endpoint GET alineado con controlador actual
- [x] Soporte Docker documentado para ejecucion en cualquier PC
