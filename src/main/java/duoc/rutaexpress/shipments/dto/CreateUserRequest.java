package duoc.rutaexpress.shipments.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotBlank String cognitoSub,
        @NotBlank @Email String email,
        String rut,
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Pattern(regexp = "Admin|Operador|Auditor") String rol) {
}
