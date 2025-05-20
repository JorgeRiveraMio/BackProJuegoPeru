package com.projuegoperu.BackProJuegoPeru.Repository;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerapeutaDisponibilidadRepository extends JpaRepository<TerapeutaDisponibilidad,Integer> {

}
