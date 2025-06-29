package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.TutorRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private UsuarioRespository usuarioRespository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public Optional<Paciente> obtenerPorId(Integer id) {
        return pacienteRepository.findById(id);
    }
    public List<Paciente> obtenerPacientesConTutor() {
        return pacienteRepository.findAll();  // Esto obtiene todos los pacientes, y debes asegurarte de que la relación Tutor esté cargada
    }

    public Paciente Guardar(Paciente u) {
        // Obtener el tutor real desde la base de datos
        int tutorId = u.getTutor().getIdUsuario();  // suponiendo que el paciente tiene un tutor con id asignado
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado con id: " + tutorId));
        // En el servicio del paciente
        if (pacienteRepository.existsByDni(u.getDni()) || usuarioRespository.existsByDni(u.getDni())) {
            throw new RuntimeException("El DNI ya está registrado en otra entidad");
        }

        u.setTutor(tutor);

        // Guardar el paciente
        return pacienteRepository.save(u);
    }



    public Optional<Paciente> ObtenerPaciente(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    public List<Paciente> obtenerPacientesPorTutorId(Integer tutorId) {
        return pacienteRepository.findByTutor_IdUsuario(tutorId);
    }

    public Paciente ActualizarUsuario(Paciente usu) {
        Paciente usuarioExistente = pacienteRepository.findByDni(usu.getDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con su DNI: " + usu.getDni()));
        int tutorId = usu.getTutor().getIdUsuario();  // suponiendo que el paciente tiene un tutor con id asignado
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado con id: " + tutorId));
        // En el servicio del paciente
        usu.setTutor(tutor);
        return pacienteRepository.save(usuarioExistente);
    }
    public void Eliminar(int id) {
        pacienteRepository.deleteById(id);
    }
}
