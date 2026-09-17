# ms-rutaexpress-shipments

Microservicio de envíos de RutaExpress para la Entrega 1 de DSY1107.

## Responsabilidad

- Crear y consultar envíos.
- Cambiar el estado de un envío.
- Filtrar envíos por estado y fecha de creación.

## Endpoints definidos por el caso

- `POST /api/shipments`
- `GET /api/shipments/{id}`
- `PUT /api/shipments/{id}/status`
- `GET /api/shipments?status=...&from=...&to=...`

Los estados permitidos son `CREADO`, `ACEPTADO`, `EN_BODEGA`, `EN_RUTA`,
`ENTREGADO` y `CANCELADO`.

El flujo controlado es:

`CREADO → ACEPTADO → EN_BODEGA → EN_RUTA → ENTREGADO`

Un envío puede cancelarse antes de terminar. No se permiten saltos: en particular,
un envío no puede pasar a `EN_RUTA` sin haber sido aceptado y enviado previamente a bodega.

## Ejemplo de creación

```json
{
  "codigoSeguimiento": "RX-0001",
  "nombreDestinatario": "Ana Pérez",
  "emailDestinatario": "ana@example.com",
  "direccionOrigen": "Santiago",
  "direccionDestino": "Valparaíso",
  "pesoKg": 2.50
}
```

Para cambiar el estado se envía, por ejemplo, `PUT /api/shipments/1/status`:

```json
{ "estado": "ACEPTADO" }
```

## Estado

Implementado con Spring Boot, Java 21, Spring Web, Spring Data JPA, Validation y PostgreSQL Driver.
La entidad `Shipment` se persiste en la base `shipments_db` mediante JPA.

Las reglas de negocio se prueban sin base de datos con pruebas unitarias de `ShipmentService`.

La configuración de PostgreSQL se mantiene fuera de Git. Usar
`src/main/resources/application-postgresql.properties.example` como plantilla para crear
`application-local.properties` con las credenciales locales.

## PostgreSQL y Docker para desarrollo local

La base de datos local está definida en `compose.local.yml`. Las contraseñas no se
versionan: primero copia `.env.example` como `.env` y reemplaza `SHIPMENTS_DB_PASSWORD`.

Con Docker Desktop instalado, inicia PostgreSQL y el microservicio con:

```powershell
docker compose -f compose.local.yml up --build
```

Para levantar la integración completa en contenedores, desde esta
carpeta y con los cinco repositorios ubicados como carpetas hermanas, usa
`compose.stack.yml`. Primero agrega `CATALOG_DB_PASSWORD`, `SHIPMENTS_DB_PASSWORD`,
`COGNITO_ISSUER_URI` y `COGNITO_CLIENT_ID` al `.env`. Luego ejecuta:

```powershell
docker compose -f compose.stack.yml up --build
```

Este Compose inicia PostgreSQL, Catálogo, Envíos, Reportes, BFF y frontend usando
nombres de servicio internos (`catalog-db`, `shipments-db`, `catalog`, `shipments`, `report`). Detén
primero los procesos locales que ocupen los puertos 4200, 5000, 8080, 8081 y 8082.
Los servicios Spring tienen healthchecks en `/actuator/health`, por lo que Compose
espera a que cada dependencia esté saludable antes de continuar.

Los contenedores de desarrollo usan `spring.jpa.hibernate.ddl-auto=update` para crear
las tablas desde las entidades. En un ambiente compartido o de despliegue se debe ejecutar
`database/01-create-shipments.sql` en `shipments_db` con el usuario `rutaexpress` y usar
`spring.jpa.hibernate.ddl-auto=validate`.
La tabla de usuarios alineada con Cognito está en `database/02-create-users.sql` y
se crea automáticamente en un volumen PostgreSQL nuevo. Luego se debe registrar el
`cognito_sub` real después del primer login, sin guardar contraseñas.

La API queda disponible en `http://localhost:5000`.

## Verificacion de salud

El endpoint `GET /actuator/health` confirma que la aplicacion esta en ejecucion.
Es el endpoint que usaremos para comprobar el servicio en Docker y posteriormente
como evidencia de despliegue en AWS:

```powershell
Invoke-RestMethod http://localhost:5000/actuator/health
```

La respuesta esperada es:

```json
{ "status": "UP" }
```
