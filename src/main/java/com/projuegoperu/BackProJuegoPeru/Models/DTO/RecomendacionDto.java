package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionDto {
    private Long id;
    private String descripcion;
    private LocalDateTime fecha;
    private Integer pacienteId;
    private Integer terapeutaId;
    private Integer adminId;
}
