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

Implementado con Spring Boot, Java 21, Spring Web, Spring Data JPA, Validation y Oracle Driver.
La entidad `Shipment` se persistirá en Oracle mediante JPA cuando esté configurada la base de datos.

Las reglas de negocio se prueban sin base de datos con pruebas unitarias de `ShipmentService`.

La configuración de Oracle se mantiene fuera de Git. Usar
`src/main/resources/application-oracle.properties.example` como plantilla para crear
`application-local.properties` con las credenciales locales.

## Oracle y Docker para desarrollo local

La base de datos local está definida en `compose.local.yml`. Las contraseñas no se
versionan: primero copia `.env.example` como `.env` y reemplaza sus valores.

Con Docker Desktop instalado, inicia Oracle y el microservicio con:

```powershell
docker compose -f compose.local.yml up --build
```

El contenedor de desarrollo usa `spring.jpa.hibernate.ddl-auto=update` para crear la
tabla desde la entidad. En un ambiente compartido o de despliegue se debe ejecutar
`database/01-create-shipments.sql` con el usuario `RUTAEXPRESS` y usar
`spring.jpa.hibernate.ddl-auto=validate`.

La API queda disponible en `http://localhost:5000`.
