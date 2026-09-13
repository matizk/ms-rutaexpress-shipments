# ms-rutaexpress-shipments

Microservicio de envíos de RutaExpress para la Entrega 1 de DSY1107.

## Responsabilidad

- Crear y consultar envíos.
- Cambiar el estado de un envío.
- Coordinar la capacidad asociada al envío.

## Endpoints definidos por el caso

- `POST /api/shipments`
- `GET /api/shipments/{id}`
- `PUT /api/shipments/{id}/status`
- `GET /api/shipments?status=...&from=...&to=...`

La regla de negocio mínima es que un envío no puede pasar a `EN_RUTA` sin haber sido `ACEPTADO`.

## Estado

Base técnica creada con Spring Boot, Java 21, Spring Web, Spring Data JPA, Validation y Oracle Driver.

La configuración de Oracle se mantiene fuera de Git. Usar
`src/main/resources/application-oracle.properties.example` como plantilla para crear
`application-local.properties` con las credenciales locales.
