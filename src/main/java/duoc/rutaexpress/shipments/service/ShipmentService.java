package duoc.rutaexpress.shipments.service;

import duoc.rutaexpress.shipments.client.CatalogClient;
import duoc.rutaexpress.shipments.client.CatalogServiceSnapshot;
import duoc.rutaexpress.shipments.domain.Shipment;
import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import duoc.rutaexpress.shipments.dto.ChangeShipmentStatusRequest;
import duoc.rutaexpress.shipments.dto.CreateShipmentRequest;
import duoc.rutaexpress.shipments.dto.ShipmentResponse;
import duoc.rutaexpress.shipments.exception.BusinessRuleException;
import duoc.rutaexpress.shipments.exception.ResourceNotFoundException;
import duoc.rutaexpress.shipments.repository.ShipmentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShipmentService {

    private static final Map<ShipmentStatus, Set<ShipmentStatus>> TRANSICIONES_PERMITIDAS = transicionesPermitidas();
    private final ShipmentRepository shipmentRepository;
    private final CatalogClient catalogClient;

    public ShipmentService(ShipmentRepository shipmentRepository, CatalogClient catalogClient) {
        this.shipmentRepository = shipmentRepository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public ShipmentResponse create(CreateShipmentRequest request) {
        if (shipmentRepository.existsByCodigoSeguimiento(request.codigoSeguimiento())) {
            throw new BusinessRuleException("Ya existe un envío con ese código de seguimiento");
        }
        CatalogServiceSnapshot service = catalogClient.reserveCapacity(request.servicioId());
        Shipment shipment = new Shipment(request.codigoSeguimiento(), request.nombreDestinatario(),
                request.emailDestinatario(), request.direccionOrigen(), request.direccionDestino(), request.pesoKg(),
                service.id(), service.nombre());
        return ShipmentResponse.from(shipmentRepository.save(shipment));
    }

    public ShipmentResponse findById(Long id) {
        return ShipmentResponse.from(findEntityById(id));
    }

    public List<ShipmentResponse> findAll(ShipmentStatus status, LocalDate from, LocalDate to) {
        LocalDateTime fromDateTime = from == null ? null : from.atStartOfDay();
        LocalDateTime toDateTime = to == null ? null : to.atTime(LocalTime.MAX);
        return shipmentRepository.findByFilters(status, fromDateTime, toDateTime).stream()
                .map(ShipmentResponse::from).toList();
    }

    @Transactional
    public ShipmentResponse changeStatus(Long id, ChangeShipmentStatusRequest request) {
        Shipment shipment = findEntityById(id);
        ShipmentStatus currentStatus = shipment.getEstado();
        ShipmentStatus newStatus = request.estado();
        if (!TRANSICIONES_PERMITIDAS.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new BusinessRuleException("No se puede cambiar un envío de %s a %s"
                    .formatted(currentStatus, newStatus));
        }
        shipment.cambiarEstado(newStatus);
        return ShipmentResponse.from(shipmentRepository.save(shipment));
    }

    private Shipment findEntityById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el envío con id " + id));
    }

    private static Map<ShipmentStatus, Set<ShipmentStatus>> transicionesPermitidas() {
        Map<ShipmentStatus, Set<ShipmentStatus>> transitions = new EnumMap<>(ShipmentStatus.class);
        transitions.put(ShipmentStatus.CREADO, Set.of(ShipmentStatus.ACEPTADO, ShipmentStatus.CANCELADO));
        transitions.put(ShipmentStatus.ACEPTADO, Set.of(ShipmentStatus.EN_BODEGA, ShipmentStatus.CANCELADO));
        transitions.put(ShipmentStatus.EN_BODEGA, Set.of(ShipmentStatus.EN_RUTA, ShipmentStatus.CANCELADO));
        transitions.put(ShipmentStatus.EN_RUTA, Set.of(ShipmentStatus.ENTREGADO, ShipmentStatus.CANCELADO));
        transitions.put(ShipmentStatus.ENTREGADO, Set.of());
        transitions.put(ShipmentStatus.CANCELADO, Set.of());
        return Map.copyOf(transitions);
    }
}
