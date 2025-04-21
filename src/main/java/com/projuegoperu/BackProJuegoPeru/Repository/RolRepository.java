package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
//import com.projuegoperu.BackProJuegoPeru.Models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {
//    List<Rol> findByNameIn(List<String> nombres);
List<Rol> findByNameIn(List<String> nombres);

}
