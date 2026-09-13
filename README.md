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

Repositorio inicial. No contiene aún código de aplicación ni configuraciones con secretos.
