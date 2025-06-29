package com.projuegoperu.BackProJuegoPeru.Repository;

import java.util.List;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository   extends JpaRepository<Paciente, Integer> {
    boolean existsByDni(String dni);
    Optional<Paciente> findByDni(String dni); // ✅ agrega esto si no existe
    List<Paciente> findByTutor_IdUsuario(Integer idUsuario);
    
    @Query("SELECT p FROM Paciente p JOIN FETCH p.tutor")
    List<Paciente> findAllWithTutor();

}
