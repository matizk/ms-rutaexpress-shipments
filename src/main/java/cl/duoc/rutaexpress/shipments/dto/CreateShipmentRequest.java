package cl.duoc.rutaexpress.shipments.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateShipmentRequest(
        @NotBlank(message = "El código de seguimiento es obligatorio") String codigoSeguimiento,
        @NotBlank(message = "El nombre del destinatario es obligatorio") String nombreDestinatario,
        @NotBlank(message = "El email del destinatario es obligatorio")
        @Email(message = "El email del destinatario no es válido") String emailDestinatario,
        @NotBlank(message = "La dirección de origen es obligatoria") String direccionOrigen,
        @NotBlank(message = "La dirección de destino es obligatoria") String direccionDestino,
        @NotNull(message = "El peso es obligatorio")
        @Positive(message = "El peso debe ser mayor que cero") BigDecimal pesoKg) {
}
