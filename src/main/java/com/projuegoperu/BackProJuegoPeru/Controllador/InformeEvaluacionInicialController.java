package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.InformeEvaluacionInicial;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Services.InformeEvaluacionInicialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/InformeEvaluacionInicial")
public class InformeEvaluacionInicialController {
    @Autowired
    private InformeEvaluacionInicialService informeEvaluacionInicialService;

    @PostMapping("/guardar")
    public ResponseEntity<InformeEvaluacionInicial> guardar(@RequestBody InformeEvaluacionInicial informe) {

        InformeEvaluacionInicial guardado = informeEvaluacionInicialService.Guardar(informe);
        if (guardado == null) {
            // En caso de que no se haya guardado correctamente, puedes devolver un error 500 o un 400.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(guardado);
    }


    @GetMapping("/obtenerId/{id}")
    public ResponseEntity<InformeEvaluacionInicial> obtenerPorId(@PathVariable int id) {

        Optional<InformeEvaluacionInicial> obtenido = informeEvaluacionInicialService.ObtenerInformeEvaluacionInicial(id);
        if (obtenido.isPresent()) {
            return ResponseEntity.ok(obtenido.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/todos")
    public ResponseEntity<List<InformeEvaluacionInicial>> obtenerTodos() {
        List<InformeEvaluacionInicial> informes = informeEvaluacionInicialService.listarInformeEvaluacionInicial();
        return ResponseEntity.ok(informes);
    }
    @PutMapping("actualizarId/{id}")
    public ResponseEntity<InformeEvaluacionInicial> actualizarInforme(
            @PathVariable Integer id,
            @RequestBody InformeEvaluacionInicial informeActualizado) {

        InformeEvaluacionInicial informe = informeEvaluacionInicialService.actualizarInforme(id, informeActualizado);
        return ResponseEntity.ok(informe);
    }
}
