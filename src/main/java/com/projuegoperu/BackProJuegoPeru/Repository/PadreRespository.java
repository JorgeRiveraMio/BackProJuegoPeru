package com.projuegoperu.BackProJuegoPeru.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projuegoperu.BackProJuegoPeru.Models.Padre;

import java.util.Optional;

@Repository
public interface PadreRespository extends JpaRepository<Padre, Integer> {
    Padre findByUsername(String username);
}
