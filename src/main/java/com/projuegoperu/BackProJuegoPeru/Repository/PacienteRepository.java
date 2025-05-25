package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository   extends JpaRepository<Paciente, Integer> {
    boolean existsByDni(String dni);
    Optional<Paciente> findByDni(String dni); // ✅ agrega esto si no existe
    
}
