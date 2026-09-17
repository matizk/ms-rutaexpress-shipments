package duoc.rutaexpress.shipments.repository;

import duoc.rutaexpress.shipments.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCognitoSub(String cognitoSub);
    boolean existsByEmail(String email);
    Optional<Usuario> findByCognitoSub(String cognitoSub);
}
