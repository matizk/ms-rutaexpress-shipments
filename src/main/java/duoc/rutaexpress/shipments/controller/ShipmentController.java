package duoc.rutaexpress.shipments.controller;

import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import duoc.rutaexpress.shipments.dto.ChangeShipmentStatusRequest;
import duoc.rutaexpress.shipments.dto.CreateShipmentRequest;
import duoc.rutaexpress.shipments.dto.ShipmentResponse;
import duoc.rutaexpress.shipments.dto.PublicTrackingResponse;
import duoc.rutaexpress.shipments.service.ShipmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentResponse shipment = shipmentService.create(request);
        return ResponseEntity.created(URI.create("/api/shipments/" + shipment.id())).body(shipment);
    }

    @GetMapping("/{id}")
    public ShipmentResponse findById(@PathVariable Long id) {
        return shipmentService.findById(id);
    }

    @GetMapping("/track/{codigoSeguimiento}")
    public PublicTrackingResponse track(@PathVariable String codigoSeguimiento) {
        return shipmentService.track(codigoSeguimiento);
    }

    @GetMapping
    public List<ShipmentResponse> findAll(
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return shipmentService.findAll(status, from, to);
    }

    @PutMapping("/{id}/status")
    public ShipmentResponse changeStatus(@PathVariable Long id,
                                         @Valid @RequestBody ChangeShipmentStatusRequest request) {
        return shipmentService.changeStatus(id, request);
    }
}
