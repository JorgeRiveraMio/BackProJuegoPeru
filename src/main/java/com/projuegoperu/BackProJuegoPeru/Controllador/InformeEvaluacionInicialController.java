package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoInforme;
import com.projuegoperu.BackProJuegoPeru.Repository.InformeEvaluacionInicialRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Services.InformeEvaluacionInicialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    public ResponseEntity<InformeEvaluacionInicial> guardarInformeConArchivo(
            @RequestParam("pacienteId") Integer pacienteId,
            @RequestParam("fechaUltimaTerapia") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaUltimaTerapia,
            @RequestParam("observaciones") String observaciones,
            @RequestParam("archivo") MultipartFile archivoPdf
    ) {
        try {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            InformeEvaluacionInicial informe = new InformeEvaluacionInicial();
            informe.setPaciente(paciente);
            informe.setFechaUltimaTerapia(fechaUltimaTerapia);
            informe.setObservaciones(observaciones);
            informe.setArchivoPdf(archivoPdf.getBytes()); // ← PDF aquí
            informe.setEstadoInforme(EstadoInforme.PENDIENTE);

            InformeEvaluacionInicial guardado = informeEvaluacionInicialService.Guardar(informe);

            return ResponseEntity.ok(guardado);
        } catch (IOException e) {
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
//    @PutMapping("actualizarId/{id}")
//    public ResponseEntity<InformeEvaluacionInicial> actualizarInforme(
//            @PathVariable Integer id,
//            @RequestBody InformeEvaluacionInicial informeActualizado) {
//
//        InformeEvaluacionInicial informe = informeEvaluacionInicialService.actualizarInforme(id, informeActualizado);
//        return ResponseEntity.ok(informe);
//    }
@PutMapping("/actualizarId/{id}")
public ResponseEntity<InformeEvaluacionInicial> actualizarInforme(
        @PathVariable Integer id,
        @RequestParam("pacienteId") Integer pacienteId,
        @RequestParam("fechaUltimaTerapia") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaUltimaTerapia,
        @RequestParam("observaciones") String observaciones,
        @RequestParam(value = "archivo", required = false) MultipartFile archivoPdf
) {
        Optional<InformeEvaluacionInicial> informe = informeRepo.findById(id);
        if (informe.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        InformeEvaluacionInicial informeEncontrado = informe.get();
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        informeEncontrado.setPaciente(paciente);
        informeEncontrado.setFechaUltimaTerapia(fechaUltimaTerapia);
        informeEncontrado.setObservaciones(observaciones);
        if (archivoPdf != null && !archivoPdf.isEmpty()) {
            try {
                informeEncontrado.setArchivoPdf(archivoPdf.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        InformeEvaluacionInicial actualizado = informeEvaluacionInicialService.Guardar(informeEncontrado);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<byte[]> verInformePdf(@PathVariable Integer id) {
        InformeEvaluacionInicial informe = informeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado"));

        byte[] archivo = informe.getArchivoPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("informe_" + id + ".pdf").build());

        return new ResponseEntity<>(archivo, headers, HttpStatus.OK);
    }

}
