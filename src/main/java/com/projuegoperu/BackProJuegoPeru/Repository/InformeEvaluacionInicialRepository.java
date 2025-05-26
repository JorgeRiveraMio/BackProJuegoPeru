package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformeEvaluacionInicialRepository extends JpaRepository<InformeEvaluacionInicial, Integer> {
}
