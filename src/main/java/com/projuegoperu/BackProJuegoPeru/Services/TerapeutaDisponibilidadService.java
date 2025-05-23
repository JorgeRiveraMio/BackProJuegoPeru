package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.TerapeutaDisponibilidadDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Repository.EmpleadoRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.TerapeutaDisponibilidadRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerapeutaDisponibilidadService {
    @Autowired
    private TerapeutaDisponibilidadRepository terapeutaDisponibilidadRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<TerapeutaDisponibilidad> Listar() {
        return terapeutaDisponibilidadRepository.findAll();
    }

    public TerapeutaDisponibilidad Guardar(TerapeutaDisponibilidadDto u) {
        Empleado empleado = empleadoRepository.findById(u.getEmpleadoId()).orElse(null);
        if (empleado == null) {
            throw new IllegalArgumentException("El id del empleado no existe");
        }
        TerapeutaDisponibilidad terapeutaDisponibilidad = new TerapeutaDisponibilidad();
        terapeutaDisponibilidad.setDiaSemana(u.getDiaSemana());
        terapeutaDisponibilidad.setHoraInicio(u.getHoraInicio());
        terapeutaDisponibilidad.setHoraFin(u.getHoraFin());
        terapeutaDisponibilidad.setEmpleado(empleado);

        return terapeutaDisponibilidadRepository.save(terapeutaDisponibilidad);
    }


    public Optional<TerapeutaDisponibilidad> ObtenerDisponibilidad(int id) {
        return terapeutaDisponibilidadRepository.findById(id);    }

    public List<TerapeutaDisponibilidad> buscarDisponibilidadPorEmpleado(Integer empleadoId) {
        return terapeutaDisponibilidadRepository.findByEmpleadoIdUsuario(empleadoId);
    }


    public TerapeutaDisponibilidad ActualizarDisponibilidad(TerapeutaDisponibilidad usu) {

        return terapeutaDisponibilidadRepository.save(usu);
    }
    public void Eliminar(int id) {
        terapeutaDisponibilidadRepository.deleteById(id);
    }

}
