package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
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
    public List<InformeEvaluacionInicial> listarInformeEvaluacionInicial(){return informeEvaluacionInicialRepository.findAll();}

//    public InformeEvaluacionInicial actualizarInforme(Integer id, InformeEvaluacionInicial informeActualizado) {
//        InformeEvaluacionInicial informe = informeEvaluacionInicialRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Informe no encontrado"));
//
//        // Actualizar campos
//        informe.setFechaUltimaTerapia(informeActualizado.getFechaUltimaTerapia());
//        informe.setArchivoUrl(informeActualizado.getArchivoUrl());
//        informe.setObservaciones(informeActualizado.getObservaciones());
//        informe.setEstadoInforme(informeActualizado.getEstadoInforme());
//
//        return informeEvaluacionInicialRepository.save(informe);
//    }

}
