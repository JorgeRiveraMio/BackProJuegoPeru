package com.projuegoperu.BackProJuegoPeru.Controllador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.SesionDto;
import com.projuegoperu.BackProJuegoPeru.Services.SesionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sesiones")
@RequiredArgsConstructor
public class SesionController {
    
    private final SesionService sesionService;

    @GetMapping("/sugeridas/{pacienteId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<List<SesionDto>> sugerirSesiones(@PathVariable Long pacienteId) {
        List<SesionDto> sugerencias = sesionService.generarSugerencias(pacienteId);
        return ResponseEntity.ok(sugerencias);
    }
}
