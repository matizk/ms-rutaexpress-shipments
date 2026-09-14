package duoc.rutaexpress.shipments.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import duoc.rutaexpress.shipments.dto.ShipmentResponse;
import duoc.rutaexpress.shipments.service.ShipmentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ShipmentControllerTest {

    @Mock
    private ShipmentService shipmentService;

    @InjectMocks
    private ShipmentController shipmentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shipmentController).build();
    }

    @Test
    void returnsShipmentAsJson() throws Exception {
        ShipmentResponse shipment = new ShipmentResponse(1L, "RX-0001", "Ana Pérez",
                "ana@example.com", "Santiago", "Valparaíso", new BigDecimal("2.50"),
                ShipmentStatus.ACEPTADO, LocalDateTime.of(2026, 9, 13, 12, 0),
                LocalDateTime.of(2026, 9, 13, 12, 5));
        when(shipmentService.findById(1L)).thenReturn(shipment);

        mockMvc.perform(get("/api/shipments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigoSeguimiento").value("RX-0001"))
                .andExpect(jsonPath("$.estado").value("ACEPTADO"));
    }
}
