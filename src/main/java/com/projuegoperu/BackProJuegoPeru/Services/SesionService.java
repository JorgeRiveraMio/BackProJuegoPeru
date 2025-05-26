package com.projuegoperu.BackProJuegoPeru.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.SesionDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.EvaluacionIngreso;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import com.projuegoperu.BackProJuegoPeru.Repository.EvaluacionIngresoRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.SesionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final TerapeutaDisponibilidadService terapeutaDisponibilidadService;
    private final PacienteRepository pacienteRepository;
    private final SesionRepository sesionRepository;
    private final EvaluacionIngresoRepository evaluacionIngresoRepository;

    public List<SesionDto> generarSugerencias(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId.intValue())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Buscar la última evaluación
        List<EvaluacionIngreso> evaluaciones = evaluacionIngresoRepository.findByPacienteId(paciente.getId());
        EvaluacionIngreso ultimaEvaluacion = evaluaciones.stream()
            .sorted((a, b) -> b.getFechaCreacion().compareTo(a.getFechaCreacion()))
            .findFirst()
            .orElse(null);

        if (ultimaEvaluacion == null) {
            throw new RuntimeException("No hay evaluación de ingreso registrada para el paciente");
        }

        if (ultimaEvaluacion.getRequiereEvaluacion() && ultimaEvaluacion.getFechaEvaluacion() == null) {
            throw new RuntimeException("El paciente requiere una evaluación previa antes de agendar sesiones");
        }

        int sesionesPlaneadas = (ultimaEvaluacion.getPlanALasSesiones() != null) ? ultimaEvaluacion.getPlanALasSesiones() : Integer.MAX_VALUE;

        List<TerapeutaDisponibilidad> disponibilidades = terapeutaDisponibilidadService.Listar();
        List<Sesion> sesionesExistentes = sesionRepository.findByPacienteId(paciente.getId());

        List<SesionDto> sugerencias = new ArrayList<>();
        int sesionesSugeridas = 0;

        for (TerapeutaDisponibilidad disp : disponibilidades) {
            for (int i = 0; i < 14 && sesionesSugeridas < sesionesPlaneadas; i++) {
                LocalDate fecha = LocalDate.now().plusDays(i);
                String diaSemana = fecha.getDayOfWeek().name();

                if (diaSemana.equalsIgnoreCase(disp.getDiaSemana())) {
                    boolean hayConflicto = sesionesExistentes.stream().anyMatch(sesion ->
                        sesion.getFechaSesion().equals(fecha) &&
                        solapaHorario(sesion.getHora(), // Ajusta esto si cambias a inicio/fin
                                      sesion.getHora().plusMinutes(45),
                                      disp.getHoraInicio(),
                                      disp.getHoraFin())
                    );

                    if (!hayConflicto) {
                        SesionDto dto = new SesionDto();
                        dto.setPacienteId(pacienteId);
                        dto.setEmpleadoTerapeutaId((long) disp.getEmpleado().getIdUsuario());
                        dto.setFechaSesion(fecha);
                        dto.setHoraInicio(disp.getHoraInicio());
                        dto.setHoraFin(disp.getHoraFin());
                        dto.setEstado("sugerida");
                        dto.setTipoSesionId(1L); // temporal
                        sugerencias.add(dto);
                        sesionesSugeridas++;
                    }
                }
            }
        }

        return sugerencias;
    }

    private boolean solapaHorario(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
    }
    
}
