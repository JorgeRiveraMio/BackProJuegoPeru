package com.projuegoperu.BackProJuegoPeru.Controllador;

import java.util.Collections;
import java.util.List;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.SesionDto;
import com.projuegoperu.BackProJuegoPeru.Services.SesionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sesiones")
@RequiredArgsConstructor
public class SesionController {
    
    private final SesionService sesionService;



    @PostMapping("/reservar")
    public ResponseEntity<Sesion> reservarSesion(@RequestBody SesionDto request) {
        return ResponseEntity.ok(sesionService.crearSesion(request));
    }

    @GetMapping()
    public ResponseEntity<List<Sesion>> listarSesiones(@RequestParam(required = false) Integer pacienteId) {
        if (pacienteId != null) {
            return ResponseEntity.ok(sesionService.listarSesionesPorPaciente(pacienteId));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSesion(@PathVariable int id) {
        sesionService.cancelarSesion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarSesion(@PathVariable int id) {
        sesionService.finalizarSesion(id);
        return ResponseEntity.noContent().build();
    }
}
