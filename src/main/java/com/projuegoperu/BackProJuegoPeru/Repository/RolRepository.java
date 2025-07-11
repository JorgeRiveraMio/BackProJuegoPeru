package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
//import com.projuegoperu.BackProJuegoPeru.Models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name); 

}
