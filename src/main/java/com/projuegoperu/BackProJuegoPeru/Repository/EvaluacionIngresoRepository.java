package com.projuegoperu.BackProJuegoPeru.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.EvaluacionIngreso;

@Repository
public interface EvaluacionIngresoRepository extends JpaRepository<EvaluacionIngreso, Integer> {
    List<EvaluacionIngreso> findByPacienteId(int pacienteId);
}

