package com.projuegoperu.BackProJuegoPeru.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.*;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoSesion;
import com.projuegoperu.BackProJuegoPeru.Repository.*;
import org.springframework.stereotype.Service;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.SesionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionRepository sesionRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PacienteRepository pacienteRepository;
    private final TipoSesionRepository tipoSesionRepository;

    public Sesion crearSesion(SesionDto request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Empleado terapeuta = empleadoRepository.findById(request.getEmpleadoTerapeutaId())
                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado"));


        TipoSesion tipoSesion = tipoSesionRepository.findById(request.getTipoSesionId())
                .orElseThrow(() -> new RuntimeException("Tipo de sesión no encontrado"));

        Empleado administrador = empleadoRepository.findById(request.getEmpleadoAdminId())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));



        // Validar disponibilidad aquí si es necesario

        Sesion sesion = new Sesion();
        sesion.setPaciente(paciente);
        sesion.setTerapeuta(terapeuta);
        sesion.setFechaSesion(request.getFechaSesion());
        sesion.setHora(request.getHoraInicio());
        sesion.setTipoSesion(tipoSesion);
        sesion.setAdministrador(administrador);
        sesion.setFechaRegistro(LocalDateTime.now()); // O LocalDate.now() si es solo fecha

        sesion.setEstado(EstadoSesion.PROGRAMADA);

        return sesionRepository.save(sesion);
    }

    public List<Sesion> listarSesionesPorPaciente(int pacienteId) {
        return sesionRepository.findByPacienteId(pacienteId);
    }

    public void cancelarSesion(int id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        if (sesion.getEstado() == EstadoSesion.FINALIZADA) {
            throw new RuntimeException("No se puede cancelar una sesión finalizada");
        }

        sesion.setEstado(EstadoSesion.CANCELADA);
        sesionRepository.save(sesion);
    }

    public void finalizarSesion(int id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setEstado(EstadoSesion.FINALIZADA);
        sesionRepository.save(sesion);
    }
    
}
