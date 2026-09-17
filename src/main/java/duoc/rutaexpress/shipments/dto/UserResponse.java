package duoc.rutaexpress.shipments.dto;

import duoc.rutaexpress.shipments.domain.Usuario;
import java.time.LocalDateTime;

public record UserResponse(Long id, String cognitoSub, String email, String rut,
                           String nombre, String apellido, String rol, LocalDateTime creadoEn) {
    public static UserResponse from(Usuario user) {
        return new UserResponse(user.getId(), user.getCognitoSub(), user.getEmail(), user.getRut(),
                user.getNombre(), user.getApellido(), user.getRol(), user.getCreadoEn());
    }
}
