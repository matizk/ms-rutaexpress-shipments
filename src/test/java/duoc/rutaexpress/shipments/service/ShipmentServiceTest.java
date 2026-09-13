package duoc.rutaexpress.shipments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import duoc.rutaexpress.shipments.domain.Shipment;
import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import duoc.rutaexpress.shipments.dto.ChangeShipmentStatusRequest;
import duoc.rutaexpress.shipments.dto.CreateShipmentRequest;
import duoc.rutaexpress.shipments.dto.ShipmentResponse;
import duoc.rutaexpress.shipments.exception.BusinessRuleException;
import duoc.rutaexpress.shipments.repository.ShipmentRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        shipmentService = new ShipmentService(shipmentRepository);
    }

    @Test
    void createsShipmentWithCreatedStatus() {
        CreateShipmentRequest request = validRequest();
        when(shipmentRepository.existsByCodigoSeguimiento(request.codigoSeguimiento())).thenReturn(false);
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShipmentResponse response = shipmentService.create(request);

        ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(shipmentCaptor.capture());
        assertEquals(ShipmentStatus.CREADO, shipmentCaptor.getValue().getEstado());
        assertEquals("RX-0001", response.codigoSeguimiento());
        assertEquals(ShipmentStatus.CREADO, response.estado());
    }

    @Test
    void allowsTransitionFromCreatedToAccepted() {
        Shipment shipment = shipmentInStatus(ShipmentStatus.CREADO);
        when(shipmentRepository.findById(10L)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        ShipmentResponse response = shipmentService.changeStatus(10L,
                new ChangeShipmentStatusRequest(ShipmentStatus.ACEPTADO));

        assertEquals(ShipmentStatus.ACEPTADO, response.estado());
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void rejectsRouteStatusBeforeAcceptance() {
        Shipment shipment = shipmentInStatus(ShipmentStatus.CREADO);
        when(shipmentRepository.findById(10L)).thenReturn(Optional.of(shipment));

        assertThrows(BusinessRuleException.class, () -> shipmentService.changeStatus(10L,
                new ChangeShipmentStatusRequest(ShipmentStatus.EN_RUTA)));

        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    private CreateShipmentRequest validRequest() {
        return new CreateShipmentRequest("RX-0001", "Ana Pérez", "ana@example.com", "Santiago",
                "Valparaíso", new BigDecimal("2.50"));
    }

    private Shipment shipmentInStatus(ShipmentStatus status) {
        Shipment shipment = new Shipment("RX-0001", "Ana Pérez", "ana@example.com", "Santiago",
                "Valparaíso", new BigDecimal("2.50"));
        if (status != ShipmentStatus.CREADO) {
            shipment.cambiarEstado(status);
        }
        return shipment;
    }
}
