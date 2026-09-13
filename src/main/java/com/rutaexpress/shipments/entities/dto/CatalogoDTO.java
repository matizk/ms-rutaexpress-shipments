package com.rutaexpress.shipments.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private Integer cantidadDisponible;
}