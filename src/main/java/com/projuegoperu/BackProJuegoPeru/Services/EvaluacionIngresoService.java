package com.projuegoperu.BackProJuegoPeru.Services;

import java.util.List;
import java.util.Optional;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.EvaluacionIngresoDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.EvaluacionIngreso;

public interface EvaluacionIngresoService {
    EvaluacionIngreso crearEvaluacion(EvaluacionIngresoDto dto);
    List<EvaluacionIngreso> obtenerPorPaciente(int pacienteId);
    Optional<EvaluacionIngreso> obtenerPorId(int id);
}
