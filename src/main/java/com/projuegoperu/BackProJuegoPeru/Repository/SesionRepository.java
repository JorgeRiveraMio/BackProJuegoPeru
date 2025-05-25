package com.projuegoperu.BackProJuegoPeru.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;

public interface  SesionRepository extends JpaRepository<Sesion, Integer>{
    List<Sesion> findByPacienteId(Integer pacienteId);
}
