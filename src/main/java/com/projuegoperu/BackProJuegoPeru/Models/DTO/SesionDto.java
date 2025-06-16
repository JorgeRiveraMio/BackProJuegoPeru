package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SesionDto {
    private int pacienteId;
    private int empleadoTerapeutaId;
    private int tipoSesionId;
    private int empleadoAdminId; // ✅ Agregado
    private LocalDate fechaSesion;
    private LocalTime horaInicio;
    private String estado;
}
