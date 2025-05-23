package com.projuegoperu.BackProJuegoPeru.Repository;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapeutaDisponibilidadRepository extends JpaRepository<TerapeutaDisponibilidad,Integer> {
    List<TerapeutaDisponibilidad> findByEmpleadoIdUsuario(Integer empleadoId);
}
