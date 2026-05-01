# Implementation Plan: Consultar valor comision recinto
**Date:** 01/05/2026  
**Spec:** `Consultar_valor_comision_recinto.md`

## Summary
Implementar un caso de uso y endpoint de solo lectura para consultar la configuracion de comision asociada a un recinto. El sistema diferencia tres estados: recinto con comision configurada (retorna tipo y valor), recinto sin comision (retorna 200 OK con flag informativo), y recinto inexistente (retorna 404 controlado).

## Technical Context
- **Language/Version:** Java 21 LTS
- **Primary Dependencies:** Spring Boot 4.0.5, Spring Data JPA
- **Storage:** PostgreSQL (tablas: `recintos`, `comisiones_recinto`). Lectura pura, sin escrituras desde este flujo.
- **Testing:** JUnit 5, MockMvc, Testcontainers, AssertJ
- **Target Platform:** Backend Service (Linux/Containerized)
- **Project Type:** Single Backend Service (Clean/Hexagonal Architecture)
- **Constraints:** Respuestas deterministas, manejo explicito de "sin comision" vs "no existe".

## Project Structure (implementado)

`backend/liquidation-events-services/src/main/java/com/ticketevents/liquidation/`

- `domain/entities/ComisionConfig.java`
- `domain/entities/TipoComision.java`
- `domain/repositories/ComisionConsultaRepository.java`
- `application/usecase/ConsultarComisionRecintoUseCase.java`
- `application/usecase/ConsultarComisionRecintoResult.java`
- `infrastructure/adapter/input/rest/response/ComisionResponse.java`
- `infrastructure/adapter/output/external/dto/ComisionDto.java`
- `infrastructure/external/JpaComisionRepositoryAdapter.java`
- `infrastructure/external/MockComisionRepository.java`
- `infrastructure/interfaces/api/ComisionRecintoController.java`
- `shared/errors/ErrorCode.java`
- `shared/errors/BusinessException.java`
- `shared/errors/TechnicalException.java`

## User Story 1 - Consultar valor comision recinto (P1)
**Goal:** Exponer endpoint para verificar tipo y valor de comision de un recinto, manejando explicitamente los casos "configurada", "sin configurar" y "inexistente".

**Endpoint implementado**
- `GET /api/v1/recintos/{id}/comision`

**Comportamiento esperado**
1. Recinto existente con comision: `200 OK` con `configurada=true`, `tipoComision`, `valorComision`.
2. Recinto existente sin comision: `200 OK` con `configurada=false`, `tipoComision=null`, `valorComision=null`.
3. Recinto inexistente: `404` por `BusinessException` con `ErrorCode.RECINTO_NOT_FOUND`.

## Notas de implementacion alineadas a codigo
- La validacion de existencia se hace via `ComisionConsultaRepository.existsRecintoById(...)`.
- La lectura de comision se hace via `ComisionConsultaRepository.findComisionByRecintoId(...)`.
- `JpaComisionRepositoryAdapter` usa `LEFT JOIN` para detectar ausencia de comision.
- Para desarrollo aislado, `MockComisionRepository` se habilita con:
  - `app.repository.mock-enabled=true`
- `JpaComisionRepositoryAdapter` queda activo cuando:
  - `app.repository.mock-enabled=false` (o no definido)

## Docker (agregado para ejecucion portable)
Archivos:
- `backend/liquidation-events-services/Dockerfile`
- `backend/liquidation-events-services/docker-compose.consulta-comision.yml`
- `backend/liquidation-events-services/src/main/resources/application-docker.properties`

Comando:
```bash
cd backend/liquidation-events-services
docker compose -f docker-compose.consulta-comision.yml up --build
```

## Checklist de consistencia
- [x] Nombres de clases y paquetes alineados al codigo actual
- [x] Codigo de error alineado: `RECINTO_NOT_FOUND`
- [x] Version de Spring Boot alineada: `4.0.5`
- [x] Endpoint GET alineado con controlador actual
- [x] Soporte Docker documentado para ejecucion en cualquier PC
