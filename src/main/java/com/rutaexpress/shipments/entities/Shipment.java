package com.rutaexpress.shipments.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    private Long id;

    private String remitente;

    private String destinatario;

    private String direccionOrigen;

    private String direccionDestino;

    private String estado;

    private LocalDateTime fechaCreacion;

    private Long servicioId;
}