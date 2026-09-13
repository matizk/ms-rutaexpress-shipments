package com.rutaexpress.shipments.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rutaexpress.shipments.clients.CatalogoClient;
import com.rutaexpress.shipments.entities.Shipment;
import com.rutaexpress.shipments.repositories.ShipmentRepository;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CatalogoClient catalogoClient;

    public ShipmentService(
            ShipmentRepository shipmentRepository,
            CatalogoClient catalogoClient) {

        this.shipmentRepository = shipmentRepository;
        this.catalogoClient = catalogoClient;
    }

    public List<Shipment> listar() {
        return shipmentRepository.findAll();
    }

    public Shipment obtener(Long id) {

        return shipmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Envio no encontrado"
                        ));
    }

    public Shipment guardar(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public void eliminar(Long id) {

        if (!shipmentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Envio no encontrado"
            );
        }

        shipmentRepository.deleteById(id);
    }

    public Shipment cambiarEstado(Long id, String nuevoEstado) {

        Shipment shipment = obtener(id);

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe ingresar un estado"
            );
        }

        nuevoEstado = nuevoEstado.trim().toUpperCase();

        String estadoActual = shipment.getEstado();

        if (!transicionValida(estadoActual, nuevoEstado)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede cambiar de "
                            + estadoActual
                            + " a "
                            + nuevoEstado
            );
        }

        /*
         * Cuando un envio pasa de CREADO a ACEPTADO,
         * se descuenta una unidad de capacidad
         * del servicio seleccionado en Catalogo.
         */
        if (nuevoEstado.equals("ACEPTADO")) {

            if (shipment.getServicioId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El envio no tiene un servicio asociado"
                );
            }

            catalogoClient.disminuirCapacidad(
                    shipment.getServicioId()
            );
        }

        shipment.setEstado(nuevoEstado);

        return shipmentRepository.save(shipment);
    }

    private boolean transicionValida(
            String estadoActual,
            String nuevoEstado) {

        if (estadoActual == null) {
            return false;
        }

        return switch (estadoActual) {

            case "CREADO" ->
                    nuevoEstado.equals("ACEPTADO")
                    || nuevoEstado.equals("CANCELADO");

            case "ACEPTADO" ->
                    nuevoEstado.equals("EN_BODEGA")
                    || nuevoEstado.equals("CANCELADO");

            case "EN_BODEGA" ->
                    nuevoEstado.equals("EN_RUTA")
                    || nuevoEstado.equals("CANCELADO");

            case "EN_RUTA" ->
                    nuevoEstado.equals("ENTREGADO")
                    || nuevoEstado.equals("CANCELADO");

            default -> false;
        };
    }
}