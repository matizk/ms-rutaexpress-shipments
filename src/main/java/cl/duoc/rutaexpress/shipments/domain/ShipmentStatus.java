package cl.duoc.rutaexpress.shipments.domain;

/** Estados permitidos para el ciclo de vida de un envío de RutaExpress. */
public enum ShipmentStatus {
    CREADO,
    ACEPTADO,
    EN_BODEGA,
    EN_RUTA,
    ENTREGADO,
    CANCELADO
}
