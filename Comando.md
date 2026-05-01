# Ejecutar Docker (consulta de comisión recinto)

## 1) Ir al módulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.consulta-comision.yml up --build
```

## 3) Probar el endpoint de consulta
En otra terminal:
```bash
curl http://localhost:8080/api/v1/recintos/1/comision
curl http://localhost:8080/api/v1/recintos/2/comision
```

## 4) Apagar contenedores
```bash
docker compose -f docker-compose.consulta-comision.yml down
```

## 5) Si aparece error de base inexistente (`resumen_ventas`)
Reinicia eliminando volumen para que Postgres cree la base en limpio:
```bash
docker compose -f docker-compose.consulta-comision.yml down -v
docker compose -f docker-compose.consulta-comision.yml up --build
```

# Ejecutar Docker (consultar resumen de ventas)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.resumen-ventas.yml up --build
```

## 3) Probar endpoint `ConsultarResumenVentasUseCase`
En otra terminal:
```bash
curl http://localhost:8080/api/v1/eventos/1/resumen-ventas
curl http://localhost:8080/api/v1/eventos/4/resumen-ventas
```

## 4) Apagar contenedores
```bash
docker compose -f docker-compose.resumen-ventas.yml down
```

## 5) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.resumen-ventas.yml down -v
docker compose -f docker-compose.resumen-ventas.yml up --build
```
