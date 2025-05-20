package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
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



    public List<TerapeutaDisponibilidad> Listar() {
        return terapeutaDisponibilidadRepository.findAll();
    }

    public TerapeutaDisponibilidad Guardar(TerapeutaDisponibilidad u) {

        return terapeutaDisponibilidadRepository.save(u);
    }



//    public Optional<Paciente> ObtenerPaciente(String dni) {
//        return terapeutaDisponibilidadRepository.findByDni(dni);
//    }
//
//    public Paciente ActualizarUsuario(Paciente usu) {
//        Paciente usuarioExistente = terapeutaDisponibilidadRepository.findByDni(usu.getDni())
//                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con su DNI: " + usu.getDni()));
//
//
//        return terapeutaDisponibilidadRepository.save(usuarioExistente);
//    }
    public void Eliminar(int id) {
        terapeutaDisponibilidadRepository.deleteById(id);
    }

}
