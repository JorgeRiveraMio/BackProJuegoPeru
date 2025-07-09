package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SesionDto {
    private int pacienteId;
    private Integer empleadoTerapeutaId;
    private int tipoSesionId;
    private Integer empleadoAdminId; // ✅ Agregado
    private LocalDate fechaSesion;
    private LocalTime hora;
    private String estado;
}
