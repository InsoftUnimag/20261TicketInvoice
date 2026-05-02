# feature 7 
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
# feature 1
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

# feature 2
# Ejecutar Docker (consultar estado de ingreso)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.estado-ingreso.yml up --build
```

## 3) Probar endpoint `ConsultarEstadoIngresoUseCase`
En otra terminal:
```bash
curl http://localhost:8080/api/v1/eventos/1/estado-ingreso
curl http://localhost:8080/api/v1/eventos/2/estado-ingreso
```

## 4) Apagar contenedores
```bash
docker compose -f docker-compose.estado-ingreso.yml down
```

## 5) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.estado-ingreso.yml down -v
docker compose -f docker-compose.estado-ingreso.yml up --build
```

# feature 3
# Ejecutar Docker (consultar recinto)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.recinto.yml up --build
```

## 3) Probar endpoint `ConsultarRecintoUseCase`
En otra terminal:
```bash
curl http://localhost:8080/api/v1/recintos/1
curl http://localhost:8080/api/v1/recintos/2
```

## 4) Apagar contenedores
```bash
docker compose -f docker-compose.recinto.yml down
```

## 5) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.recinto.yml down -v
docker compose -f docker-compose.recinto.yml up --build
```

# feature 4
# Ejecutar Docker (consultar ingresos de tickets)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.ingresos.yml up --build
```

## 3) Probar endpoint `ConsultarIngresosTicketsUseCase`
En otra terminal:
```bash
curl http://localhost:8080/api/v1/eventos/1/ingresos
curl http://localhost:8080/api/v1/eventos/2/ingresos
```

## 4) Apagar contenedores
```bash
docker compose -f docker-compose.ingresos.yml down
```

## 5) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.ingresos.yml down -v
docker compose -f docker-compose.ingresos.yml up --build
```

# feature 5
# Ejecutar Docker (determinar tipo de liquidacion)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.liquidacion.yml up --build
```

## 3) Registrar configuracion (POST)
`id` del evento va en la URL (ejemplo: `/eventos/1/...`) y no puede ser `null`.
En otra terminal (CMD):
```bash
curl -X POST "http://localhost:8080/api/v1/eventos/1/configuracion-liquidacion" -H "Content-Type: application/json" -d "{\"tipoLiquidacion\":\"REPARTO_INGRESOS\",\"valorComision\":50000,\"porcentaje\":10}"
```

## 4) Consultar en navegador (GET)
Abrir en navegador:
```bash
http://localhost:8080/api/v1/eventos/1/configuracion-liquidacion
```

## 5) Si hiciste cambios de codigo, reiniciar aplicando build
```bash
docker compose -f docker-compose.liquidacion.yml down
docker compose -f docker-compose.liquidacion.yml up --build
```

## 6) Apagar contenedores
```bash
docker compose -f docker-compose.liquidacion.yml down
```

## 7) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.liquidacion.yml down -v
docker compose -f docker-compose.liquidacion.yml up --build
```
# feature 6
# Ejecutar Docker (registrar comision de recinto)

## 1) Ir al modulo backend
```bash
cd backend/liquidation-events-services
```

## 2) Levantar contenedores (app + postgres)
```bash
docker compose -f docker-compose.registrar-comision.yml up --build
```

## 3) Registrar comision (POST)
`id` del recinto va en la URL (ejemplo: `/recintos/1/...`) y no puede ser `null`.
En otra terminal (CMD):
```bash
curl -X POST "http://localhost:8080/api/v1/recintos/1/comision" -H "Content-Type: application/json" -d "{\"tipoComision\":\"PORCENTAJE\",\"valorComision\":7.5}"
```

## 4) Consultar en navegador (GET)
Abrir en navegador:
```bash
http://localhost:8080/api/v1/recintos/1/comision
```

## 5) Si hiciste cambios de codigo, reiniciar aplicando build
```bash
docker compose -f docker-compose.registrar-comision.yml down
docker compose -f docker-compose.registrar-comision.yml up --build
```

## 6) Apagar contenedores
```bash
docker compose -f docker-compose.registrar-comision.yml down
```

## 7) Si aparece error de base inexistente (`resumen_ventas`)
```bash
docker compose -f docker-compose.registrar-comision.yml down -v
docker compose -f docker-compose.registrar-comision.yml up --build
```
docker compose -f docker-compose.liquidacion.yml logs --tail=200 liquidation-events-services
