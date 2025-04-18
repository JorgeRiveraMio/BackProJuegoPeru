package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.RolDao;
//import com.projuegoperu.BackProJuegoPeru.Models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository extends JpaRepository<RolDao, Integer> {
//    List<Rol> findByNameIn(List<String> nombres);
List<RolDao> findByNameIn(List<String> nombres);

}
