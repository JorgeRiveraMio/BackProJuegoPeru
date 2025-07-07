package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoSesion;
import com.projuegoperu.BackProJuegoPeru.Services.TipoSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tipos-sesion")
public class TipoSesionControllador {
    @Autowired
    private TipoSesionService tipoSesionService;

    @GetMapping
    public List<TipoSesion> listarTodos() {
        return tipoSesionService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<TipoSesion> obtenerPorId(@PathVariable Integer id) {
        return tipoSesionService.obtenerPorId(id);
    }

    @PostMapping
    public TipoSesion guardar(@RequestBody TipoSesion tipoSesion) {
        return tipoSesionService.guardar(tipoSesion);
    }

    @PutMapping
    public TipoSesion actualizar(@RequestBody TipoSesion tipoSesion) {
        return tipoSesionService.actualizar(tipoSesion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoSesionService.eliminar(id);
    }
}
