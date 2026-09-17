package duoc.rutaexpress.shipments.repository;

import duoc.rutaexpress.shipments.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCognitoSub(String cognitoSub);
    boolean existsByEmail(String email);
}
