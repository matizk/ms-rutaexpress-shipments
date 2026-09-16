package duoc.rutaexpress.shipments.dto;

import duoc.rutaexpress.shipments.domain.Shipment;
import duoc.rutaexpress.shipments.domain.ShipmentStatus;
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
        Long servicioId,
        String servicioNombre,
        ShipmentStatus estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getId(), shipment.getCodigoSeguimiento(),
                shipment.getNombreDestinatario(), shipment.getEmailDestinatario(),
                shipment.getDireccionOrigen(), shipment.getDireccionDestino(), shipment.getPesoKg(),
                shipment.getServicioId(), shipment.getServicioNombre(),
                shipment.getEstado(), shipment.getFechaCreacion(), shipment.getFechaActualizacion());
    }
}
