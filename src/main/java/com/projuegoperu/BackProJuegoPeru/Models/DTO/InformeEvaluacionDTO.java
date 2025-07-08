package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformeEvaluacionDTO {
    private Integer pacienteId;
    private LocalDate fechaUltimaTerapia;
    private String observaciones;
    private String urlArchivo;
}
