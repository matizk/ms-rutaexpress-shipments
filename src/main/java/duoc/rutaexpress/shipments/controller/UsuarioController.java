package duoc.rutaexpress.shipments.controller;

import duoc.rutaexpress.shipments.dto.CreateUserRequest;
import duoc.rutaexpress.shipments.dto.UserResponse;
import duoc.rutaexpress.shipments.service.UsuarioService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = usuarioService.create(request);
        return ResponseEntity.created(URI.create("/api/users/" + user.id())).body(user);
    }
}
