package duoc.rutaexpress.shipments.service;

import duoc.rutaexpress.shipments.domain.Usuario;
import duoc.rutaexpress.shipments.dto.CreateUserRequest;
import duoc.rutaexpress.shipments.dto.UserResponse;
import duoc.rutaexpress.shipments.exception.BusinessRuleException;
import duoc.rutaexpress.shipments.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (usuarioRepository.existsByCognitoSub(request.cognitoSub())) {
            throw new BusinessRuleException("Ya existe un usuario con ese cognito_sub");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Ya existe un usuario con ese correo");
        }
        return UserResponse.from(usuarioRepository.save(new Usuario(request)));
    }
}
