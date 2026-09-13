package cl.duoc.rutaexpress.shipments.dto;

import cl.duoc.rutaexpress.shipments.domain.Shipment;
import cl.duoc.rutaexpress.shipments.domain.ShipmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShipmentResponse(
        Long id,
        String codigoSeguimiento,
        String nombreDestinatario,
        String emailDestinatario,
        String direccionOrigen,
        String direccionDestino,
        BigDecimal pesoKg,
        ShipmentStatus estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getId(), shipment.getCodigoSeguimiento(),
                shipment.getNombreDestinatario(), shipment.getEmailDestinatario(),
                shipment.getDireccionOrigen(), shipment.getDireccionDestino(), shipment.getPesoKg(),
                shipment.getEstado(), shipment.getFechaCreacion(), shipment.getFechaActualizacion());
    }
}
