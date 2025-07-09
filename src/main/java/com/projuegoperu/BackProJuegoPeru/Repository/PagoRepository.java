package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Pago;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByTutor(Tutor tutor);
    List<Pago> findBySesion(Sesion sesion);
    Optional<Pago> findByReferenciaPago(String referenciaPago);
}
