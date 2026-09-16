package duoc.rutaexpress.shipments.dto;

import duoc.rutaexpress.shipments.domain.Shipment;
import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import java.time.LocalDateTime;

public record PublicTrackingResponse(
        String codigoSeguimiento,
        ShipmentStatus estado,
        String direccionOrigen,
        String direccionDestino,
        LocalDateTime fechaActualizacion) {

    public static PublicTrackingResponse from(Shipment shipment) {
        return new PublicTrackingResponse(shipment.getCodigoSeguimiento(), shipment.getEstado(),
                shipment.getDireccionOrigen(), shipment.getDireccionDestino(), shipment.getFechaActualizacion());
    }
}
