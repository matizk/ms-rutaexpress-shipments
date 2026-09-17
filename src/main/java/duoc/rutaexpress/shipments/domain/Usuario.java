package duoc.rutaexpress.shipments.domain;

import duoc.rutaexpress.shipments.dto.CreateUserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cognito_sub", nullable = false, unique = true, length = 100)
    private String cognitoSub;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(length = 20)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    protected Usuario() {
    }

    public Usuario(CreateUserRequest request) {
        this.cognitoSub = request.cognitoSub();
        this.email = request.email();
        this.rut = request.rut();
        this.nombre = request.nombre();
        this.apellido = request.apellido();
        this.rol = request.rol();
    }

    @PrePersist
    void assignCreationDate() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public String getCognitoSub() { return cognitoSub; }
    public String getEmail() { return email; }
    public String getRut() { return rut; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getRol() { return rol; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
