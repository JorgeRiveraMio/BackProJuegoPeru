package com.projuegoperu.BackProJuegoPeru.Models.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerapeutaDisponibilidadDto {


    @NotNull(message = "El id del empleado no debe estar vacio")
    private int empleadoId;
    @NotNull(message = "El dia de la semana no debe estar vacio")
    private String diaSemana;
    @NotNull(message = "La hora de inicio no debe estar vacio")
    private LocalTime horaInicio;
    @NotNull(message = "La hora de fin no debe estar vacio")
    private LocalTime horaFin;

}

