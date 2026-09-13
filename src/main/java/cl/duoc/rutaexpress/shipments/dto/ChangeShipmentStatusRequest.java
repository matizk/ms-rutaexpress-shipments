package cl.duoc.rutaexpress.shipments.dto;

import cl.duoc.rutaexpress.shipments.domain.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeShipmentStatusRequest(
        @NotNull(message = "El nuevo estado es obligatorio") ShipmentStatus estado) {
}
