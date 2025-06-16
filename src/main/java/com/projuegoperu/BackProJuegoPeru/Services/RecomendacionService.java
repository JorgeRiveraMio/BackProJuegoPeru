package com.projuegoperu.BackProJuegoPeru.Services;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.RecomendacionDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Recomendacion;
import com.projuegoperu.BackProJuegoPeru.Repository.EmpleadoRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.RecomendacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
public class RecomendacionService {
    @Autowired
    private  RecomendacionRepository recomendacionRepository;
    @Autowired
    private  PacienteRepository pacienteRepository;
    @Autowired
    private  EmpleadoRepository empleadoRepository;


    public Recomendacion registrar(RecomendacionDto dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Empleado terapeuta = empleadoRepository.findById(dto.getTerapeutaId())
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado"));

        Empleado admin = empleadoRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setFecha(LocalDate.now());
        recomendacion.setMotivo(dto.getDescripcion());
        recomendacion.setPaciente(paciente);
        recomendacion.setTerapeuta(terapeuta);
        recomendacion.setCreadoPor(admin);

        return recomendacionRepository.save(recomendacion);
    }

    public Recomendacion editar(int id, RecomendacionDto dto) {
        Recomendacion recomendacion = recomendacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendacion no encontrada"));

        Empleado terapeuta = empleadoRepository.findById(dto.getTerapeutaId())
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado"));

        recomendacion.setMotivo(dto.getDescripcion());
        recomendacion.setTerapeuta(terapeuta);
        return recomendacionRepository.save(recomendacion);
    }

    public void eliminar(int id) {
        if (!recomendacionRepository.existsById(id)) {
            throw new RuntimeException("Recomendacion no encontrada");
        }
        recomendacionRepository.deleteById(id);
    }

    public List<Recomendacion> listarPorPaciente(int pacienteId) {
        return recomendacionRepository.findByPacienteId(pacienteId);
    }

    public Optional<Recomendacion> buscarPorId(int id) {
        return recomendacionRepository.findById(id);
    }
}
