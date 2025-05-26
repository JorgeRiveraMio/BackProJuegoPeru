package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EvaluacionIngresoDto {
    private Integer id;
    private Integer pacienteId;
    private String tipo;
    private Boolean tieneInformes;
    private Boolean requiereEvaluacion;
    private LocalDate fechaEvaluacion;
    private Integer planALasSesiones;
    private String observaciones;
    private Integer creadoPorId;
    private LocalDateTime fechaCreacion;
    private Integer tipoEvaluacionId;
}
