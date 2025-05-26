package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SesionDto {
    private Long pacienteId;
    private Long empleadoTerapeutaId;
    private Long tipoSesionId;
    private LocalDate fechaSesion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
}
