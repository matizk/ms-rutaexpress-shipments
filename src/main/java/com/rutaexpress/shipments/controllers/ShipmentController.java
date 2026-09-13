package com.rutaexpress.shipments.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutaexpress.shipments.entities.Shipment;
import com.rutaexpress.shipments.services.ShipmentService;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:4200")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<Shipment> listarShipments() {
        return shipmentService.listar();
    }

    @GetMapping("/{id}")
    public Shipment obtenerShipment(@PathVariable Long id) {
        return shipmentService.obtener(id);
    }

    @PostMapping
    public Shipment crearShipment(@RequestBody Shipment shipment) {

        if (shipment.getFechaCreacion() == null) {
            shipment.setFechaCreacion(LocalDateTime.now());
        }

        if (shipment.getEstado() == null) {
            shipment.setEstado("CREADO");
        }

        return shipmentService.guardar(shipment);
    }

    @PutMapping("/{id}")
    public Shipment actualizarShipment(
            @PathVariable Long id,
            @RequestBody Shipment shipment) {

        shipment.setId(id);

        return shipmentService.guardar(shipment);
    }

    @PutMapping("/{id}/status")
    public Shipment cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return shipmentService.cambiarEstado(
                id,
                body.get("status")
        );
    }

    @DeleteMapping("/{id}")
    public void eliminarShipment(@PathVariable Long id) {
        shipmentService.eliminar(id);
    }
}