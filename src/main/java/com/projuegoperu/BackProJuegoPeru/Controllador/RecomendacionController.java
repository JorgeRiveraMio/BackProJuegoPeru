package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.RecomendacionDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Recomendacion;
import com.projuegoperu.BackProJuegoPeru.Services.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recomendaciones")
public class RecomendacionController {
    @Autowired
    private RecomendacionService recomendacionService;

    // Registrar recomendación
    @PostMapping("/guardar")
    public ResponseEntity<Recomendacion> registrar(@RequestBody RecomendacionDto dto) {
        Recomendacion nueva = recomendacionService.registrar(dto);
        return ResponseEntity.ok(nueva);
    }

    // Editar recomendación
    @PutMapping("actualizar/{id}")
    public ResponseEntity<Recomendacion> editar(@PathVariable int id, @RequestBody RecomendacionDto dto) {
        Recomendacion actualizada = recomendacionService.editar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar recomendación
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        recomendacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar recomendaciones por ID de paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Recomendacion>> listarPorPaciente(@PathVariable int pacienteId) {
        List<Recomendacion> recomendaciones = recomendacionService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(recomendaciones);
    }

    // Buscar una recomendación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recomendacion> buscarPorId(@PathVariable int id) {
        return recomendacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
