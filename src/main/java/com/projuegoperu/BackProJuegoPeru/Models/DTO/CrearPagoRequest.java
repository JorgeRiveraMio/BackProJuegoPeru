package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPagoRequest {
    private String descripcion;
    private BigDecimal monto;
    private Long tutorId;
    private Long sesionId;
    
}
