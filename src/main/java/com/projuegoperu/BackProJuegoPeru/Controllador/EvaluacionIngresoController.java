package com.projuegoperu.BackProJuegoPeru.Controllador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.EvaluacionIngresoDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.EvaluacionIngreso;
import com.projuegoperu.BackProJuegoPeru.Services.EvaluacionIngresoService;

@RestController
@RequestMapping("/evaluaciones")
public class EvaluacionIngresoController {
    @Autowired
    private EvaluacionIngresoService evaluacionIngresoService;

    @PostMapping("/nueva")
    public ResponseEntity<?> crearEvaluacion(@RequestBody EvaluacionIngresoDto dto) {
        return ResponseEntity.ok(evaluacionIngresoService.crearEvaluacion(dto));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<EvaluacionIngreso>> obtenerPorPaciente(@PathVariable int id) {
        return ResponseEntity.ok(evaluacionIngresoService.obtenerPorPaciente(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionIngreso> obtenerPorId(@PathVariable int id) {
        return evaluacionIngresoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
