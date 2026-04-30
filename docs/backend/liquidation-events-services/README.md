# Consultar comisión por recinto

## Endpoint
- `GET /api/v1/recintos/{id}/comision`

## Respuestas
- `200` con comisión:
```json
{"configurada":true,"mensaje":"Comisión configurada","tipoComision":"PORCENTUAL","valorComision":7.50}
```
- `200` sin comisión:
```json
{"configurada":false,"mensaje":"El recinto no tiene una comisión registrada","tipoComision":null,"valorComision":null}
```
- `404` recinto inexistente:
```json
{"code":"VENUE_NOT_FOUND","message":"El recinto no está registrado","timestamp":"2026-04-29T00:00:00Z"}
```

## Ejecutar en cualquier PC con Docker (recomendado)
Requisitos: Docker Desktop instalado.

1. Entra al módulo:
```bash
cd backend/liquidation-events-services
```
2. Levanta todo:
```bash
docker compose up --build
```
3. Prueba:
```bash
curl http://localhost:8080/api/v1/recintos/1/comision
curl http://localhost:8080/api/v1/recintos/2/comision
curl http://localhost:8080/api/v1/recintos/999/comision
```
4. Ejecutar en cualquier pc:
```bash
Sí, sí se puede.
Con la app arriba, abre en el navegador:
•
http://localhost:8080/api/v1/recintos/1/comision
•
http://localhost:8080/api/v1/recintos/2/comision
•
http://localhost:8080/api/v1/recintos/999/comision
Vas a ver el JSON directamente en pantalla.
Si no abre, revisa que docker compose up siga corriendo en la otra terminal.

```
## Ejecutar sin Docker
Requisitos:
- Java 21
- Maven 3.9+
- PostgreSQL 16+

Variables opcionales:
- `DB_URL` (default `jdbc:postgresql://localhost:5432/ticket_events`)
- `DB_USER` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `PORT` (default `8080`)

Comandos:
```bash
mvn spring-boot:run
```
