package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoInforme;
import com.projuegoperu.BackProJuegoPeru.Repository.InformeEvaluacionInicialRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class InformeEvaluacionInicialService {
    @Autowired
    private InformeEvaluacionInicialRepository informeEvaluacionInicialRepository;

    @Autowired
    private PacienteRepository  pacienteRepository;

    public InformeEvaluacionInicial Guardar(InformeEvaluacionInicial informe) {

        return informeEvaluacionInicialRepository.save(informe);
    }


    public Optional<InformeEvaluacionInicial> ObtenerInformeEvaluacionInicial (int id){

        return informeEvaluacionInicialRepository.findById(id);
    }
    
    public List<InformeEvaluacionInicial> findByPacienteId(Integer pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + pacienteId));
        return informeEvaluacionInicialRepository.findByPaciente(paciente);
    }

    public List<InformeEvaluacionInicial> listarInformeEvaluacionInicial(){return informeEvaluacionInicialRepository.findAll();}

    public InformeEvaluacionInicial actualizarInforme(Integer id, InformeEvaluacionInicial informeActualizado) {
        InformeEvaluacionInicial informe = informeEvaluacionInicialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Informe no encontrado"));

        // Actualizar campos
        informe.setFechaUltimaTerapia(informeActualizado.getFechaUltimaTerapia());
        informe.setArchivoUrl(informeActualizado.getArchivoUrl());
        informe.setObservaciones(informeActualizado.getObservaciones());
        informe.setEstadoInforme(informeActualizado.getEstadoInforme());

        return informeEvaluacionInicialRepository.save(informe);
    }

    public InformeEvaluacionInicial actualizarInformeAprobado(Integer id) {
        InformeEvaluacionInicial informe = informeEvaluacionInicialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Informe no encontrado"));

        // Cambiar solo el estado a "Aprobado"
        informe.setEstadoInforme(EstadoInforme.APROBADO);

        return informeEvaluacionInicialRepository.save(informe);
    }
    public InformeEvaluacionInicial actualizarInformeDesAprobado(Integer id) {
        InformeEvaluacionInicial informe = informeEvaluacionInicialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Informe no encontrado"));

        // Cambiar solo el estado a "Aprobado"
        informe.setEstadoInforme(EstadoInforme.DESAPROBADO);

        return informeEvaluacionInicialRepository.save(informe);
    }

    public InformeEvaluacionInicial actualizarComentarioInforme(Integer id, String comentario) {
        InformeEvaluacionInicial informe = informeEvaluacionInicialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Informe no encontrado"));

        informe.setObservaciones(comentario);  // Solo se actualiza el comentario

        return informeEvaluacionInicialRepository.save(informe);
    }


}
