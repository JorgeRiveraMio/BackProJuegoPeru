package com.projuegoperu.BackProJuegoPeru.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoEvaluacion;

@Repository
public interface TipoEvaluacionRepository extends JpaRepository<TipoEvaluacion, Integer> {
}
