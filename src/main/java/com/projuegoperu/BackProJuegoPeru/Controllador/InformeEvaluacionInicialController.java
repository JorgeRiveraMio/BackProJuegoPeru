package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.InformeEvaluacionDTO;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoInforme;
import com.projuegoperu.BackProJuegoPeru.Repository.InformeEvaluacionInicialRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Services.InformeEvaluacionInicialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/InformeEvaluacionInicial")
public class InformeEvaluacionInicialController {
    @Autowired
    private InformeEvaluacionInicialService informeEvaluacionInicialService;
    @Autowired
    private InformeEvaluacionInicialRepository informeRepo;

    @Autowired
    private PacienteRepository pacienteRepository;

//    @PostMapping("/guardar")
//    public ResponseEntity<InformeEvaluacionInicial> guardar(@RequestBody InformeEvaluacionInicial informe) {
//
//        InformeEvaluacionInicial guardado = informeEvaluacionInicialService.Guardar(informe);
//        if (guardado == null) {
//            // En caso de que no se haya guardado correctamente, puedes devolver un error 500 o un 400.
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(guardado);
//    }
    
    @PostMapping("/guardar")
    public ResponseEntity<InformeEvaluacionInicial> guardarInforme(
            @RequestPart("dto") InformeEvaluacionDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            InformeEvaluacionInicial informe = new InformeEvaluacionInicial();
            informe.setPaciente(paciente);
            informe.setFechaUltimaTerapia(dto.getFechaUltimaTerapia());
            informe.setObservaciones(dto.getObservaciones());
            informe.setArchivoUrl(dto.getUrlArchivo()); // Ya viene con la URL de Cloudinary
            informe.setEstadoInforme(EstadoInforme.PENDIENTE);

            InformeEvaluacionInicial guardado = informeEvaluacionInicialService.Guardar(informe);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    


//    @GetMapping("/obtenerId/{id}")
//    public ResponseEntity<InformeEvaluacionInicial> obtenerPorId(@PathVariable int id) {
//
//        Optional<InformeEvaluacionInicial> obtenido = informeEvaluacionInicialService.ObtenerInformeEvaluacionInicial(id);
//        if (obtenido.isPresent()) {
//            return ResponseEntity.ok(obtenido.get());
//        }else{
//            return ResponseEntity.notFound().build();
//        }
//    }
    @GetMapping("/todos")
    public ResponseEntity<List<InformeEvaluacionInicial>> obtenerTodos() {
        List<InformeEvaluacionInicial> informes = informeEvaluacionInicialService.listarInformeEvaluacionInicial();
        return ResponseEntity.ok(informes);
    }
    @PutMapping("/aprobarId/{id}")
    public ResponseEntity<InformeEvaluacionInicial> actualizarInformeAprobado(
            @PathVariable Integer id          ) {

        InformeEvaluacionInicial informe = informeEvaluacionInicialService.actualizarInformeAprobado(id);
        return ResponseEntity.ok(informe);
    }

    @PutMapping("/desaprobarId/{id}")
    public ResponseEntity<InformeEvaluacionInicial> actualizarInformeDesaprobar(
            @PathVariable Integer id          ) {

        InformeEvaluacionInicial informe = informeEvaluacionInicialService.actualizarInformeDesAprobado(id);
        return ResponseEntity.ok(informe);
    }

    @PutMapping("/actualizarId/{id}")
    public ResponseEntity<InformeEvaluacionInicial> actualizarInforme(
            @PathVariable Integer id,
            @RequestBody InformeEvaluacionDTO dto
    ) {
        Optional<InformeEvaluacionInicial> informeOpt = informeRepo.findById(id);
        if (informeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InformeEvaluacionInicial informeEncontrado = informeOpt.get();

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        informeEncontrado.setPaciente(paciente);
        informeEncontrado.setFechaUltimaTerapia(dto.getFechaUltimaTerapia());
        informeEncontrado.setObservaciones(dto.getObservaciones());

        // Solo actualiza si se envió una nueva URL
        if (dto.getUrlArchivo() != null && !dto.getUrlArchivo().isBlank()) {
            informeEncontrado.setArchivoUrl(dto.getUrlArchivo());
        }

        InformeEvaluacionInicial actualizado = informeEvaluacionInicialService.Guardar(informeEncontrado);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<String> obtenerUrlInformePdf(@PathVariable Integer id) {
        InformeEvaluacionInicial informe = informeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado"));

        return ResponseEntity.ok(informe.getArchivoUrl());
    }

    @GetMapping("/paciente/{id}")
    public List<InformeEvaluacionInicial> getInformesPorPaciente(@PathVariable Integer id) {
        return informeEvaluacionInicialService.findByPacienteId(id);
    }




}
