package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoEvaluacion;
import com.projuegoperu.BackProJuegoPeru.Services.TipoEvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipo-evaluacion")
@CrossOrigin(origins = "*")
public class TipoEvaluacionController {

    @Autowired
    private TipoEvaluacionService tipoEvaluacionService;

    @GetMapping
    public List<TipoEvaluacion> listar() {
        return tipoEvaluacionService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<TipoEvaluacion> obtenerPorId(@PathVariable Integer id) {
        return tipoEvaluacionService.obtenerPorId(id);
    }

    @PostMapping
    public TipoEvaluacion crear(@RequestBody TipoEvaluacion tipoEvaluacion) {
        return tipoEvaluacionService.guardar(tipoEvaluacion);
    }

    @PutMapping("/{id}")
    public TipoEvaluacion actualizar(@PathVariable Integer id, @RequestBody TipoEvaluacion tipoEvaluacion) {
        tipoEvaluacion.setId(id);
        return tipoEvaluacionService.guardar(tipoEvaluacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoEvaluacionService.eliminar(id);
    }
}
