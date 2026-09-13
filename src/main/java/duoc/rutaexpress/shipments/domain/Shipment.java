package duoc.rutaexpress.shipments.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 50)
    private String codigoSeguimiento;

    @Column(name = "nombre_destinatario", nullable = false, length = 120)
    private String nombreDestinatario;

    @Column(name = "email_destinatario", nullable = false, length = 160)
    private String emailDestinatario;

    @Column(name = "direccion_origen", nullable = false, length = 250)
    private String direccionOrigen;

    @Column(name = "direccion_destino", nullable = false, length = 250)
    private String direccionDestino;

    @Column(name = "peso_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShipmentStatus estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    protected Shipment() {
        // Constructor requerido por JPA.
    }

    public Shipment(String codigoSeguimiento, String nombreDestinatario, String emailDestinatario,
                    String direccionOrigen, String direccionDestino, BigDecimal pesoKg) {
        this.codigoSeguimiento = codigoSeguimiento;
        this.nombreDestinatario = nombreDestinatario;
        this.emailDestinatario = emailDestinatario;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.pesoKg = pesoKg;
        this.estado = ShipmentStatus.CREADO;
    }

    @PrePersist
    void assignCreationDates() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaActualizacion = now;
    }

    @PreUpdate
    void updateModifiedDate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public String getNombreDestinatario() { return nombreDestinatario; }
    public String getEmailDestinatario() { return emailDestinatario; }
    public String getDireccionOrigen() { return direccionOrigen; }
    public String getDireccionDestino() { return direccionDestino; }
    public BigDecimal getPesoKg() { return pesoKg; }
    public ShipmentStatus getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public void cambiarEstado(ShipmentStatus nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
