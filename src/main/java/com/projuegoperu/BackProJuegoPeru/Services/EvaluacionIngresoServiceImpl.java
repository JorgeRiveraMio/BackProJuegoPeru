package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.EvaluacionIngresoDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.EvaluacionIngreso;
import com.projuegoperu.BackProJuegoPeru.Repository.EvaluacionIngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EvaluacionIngresoServiceImpl implements EvaluacionIngresoService {
    @Autowired
    private EvaluacionIngresoRepository evaluacionIngresoRepository;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoEvaluacionService tipoEvaluacionService;

    @Override
    public EvaluacionIngreso crearEvaluacion(EvaluacionIngresoDto dto) {
        EvaluacionIngreso evaluacion = new EvaluacionIngreso();

        evaluacion.setTipo(dto.getTipo());
        evaluacion.setTieneInformes(dto.getTieneInformes());
        evaluacion.setRequiereEvaluacion(dto.getRequiereEvaluacion());
        evaluacion.setFechaEvaluacion(dto.getFechaEvaluacion());
        evaluacion.setPlanALasSesiones(dto.getPlanALasSesiones());
        evaluacion.setObservaciones(dto.getObservaciones());
        evaluacion.setFechaCreacion(LocalDateTime.now());

        // Obtener paciente
        pacienteService.obtenerPorId(dto.getPacienteId()).ifPresent(evaluacion::setPaciente);

        // Obtener usuario que creó
        usuarioService.ObtenerPorId(dto.getCreadoPorId()).ifPresent(evaluacion::setCreadoPor);

        // Obtener tipo de evaluación
        tipoEvaluacionService.obtenerPorId(dto.getTipoEvaluacionId()).ifPresent(evaluacion::setTipoEvaluacion);

        return evaluacionIngresoRepository.save(evaluacion);
    }

    @Override
    public List<EvaluacionIngreso> obtenerPorPaciente(int pacienteId) {
        return evaluacionIngresoRepository.findByPacienteId(pacienteId);
    }

    @Override
    public Optional<EvaluacionIngreso> obtenerPorId(int id) {
        return evaluacionIngresoRepository.findById(id);
    }
}
