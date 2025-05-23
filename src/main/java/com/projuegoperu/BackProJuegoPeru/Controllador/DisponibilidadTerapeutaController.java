package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.EmpleadoDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.TerapeutaDisponibilidadDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TerapeutaDisponibilidad;
import com.projuegoperu.BackProJuegoPeru.Services.EmpleadoService;
import com.projuegoperu.BackProJuegoPeru.Services.TerapeutaDisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/disponibilidadTerapeuta")
public class DisponibilidadTerapeutaController {
    @Autowired
    private TerapeutaDisponibilidadService terapeutaDisponibilidadService;

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody TerapeutaDisponibilidadDto terapeutaDisponibilidadDto) {
        try {

            terapeutaDisponibilidadService.Guardar(terapeutaDisponibilidadDto);
            return ResponseEntity.ok("Se guardo correctamente  la disponibilidad");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/obtenerId/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        Optional<TerapeutaDisponibilidad> obtenido = terapeutaDisponibilidadService.ObtenerDisponibilidad(id);
        if (obtenido.isPresent()) {
            return ResponseEntity.ok(obtenido.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "No se encontro disponibilidad con es ID :" + id );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping("/todos")
    public ResponseEntity<List<TerapeutaDisponibilidad>> listarTodos() {
        List<TerapeutaDisponibilidad> disponibilidad = terapeutaDisponibilidadService.Listar();
        return ResponseEntity.ok(disponibilidad);
    }

    @GetMapping("/disponibilidad/{empleadoId}")
    public ResponseEntity<List<TerapeutaDisponibilidad>> obtenerDisponibilidad(@PathVariable Integer empleadoId) {
        List<TerapeutaDisponibilidad> disponibilidad = terapeutaDisponibilidadService.buscarDisponibilidadPorEmpleado(empleadoId);
        return ResponseEntity.ok(disponibilidad);
    }


    @PutMapping("/actualizarPorId/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable int id, @RequestBody TerapeutaDisponibilidadDto detalles) {
        Optional<TerapeutaDisponibilidad> disponibilidadExistente = terapeutaDisponibilidadService.ObtenerDisponibilidad(id);

        if (!disponibilidadExistente.isPresent()) {
            // Retornar un mensaje personalizado si no se encuentra
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Disponibilidad  con ID " + id + " no encontrado");
        }

        TerapeutaDisponibilidad disponibilidad = disponibilidadExistente.get();

        // Actualizar empleado
        Optional<Empleado> optionalEmpleado = empleadoService.ObtenerEmpleadoID(detalles.getEmpleadoId());
        if (optionalEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Empleado con ID " + detalles.getEmpleadoId() + " no encontrado");
        }
        disponibilidad.setEmpleado(optionalEmpleado.get());

        // Actualizar campos simples
        disponibilidad.setDiaSemana(detalles.getDiaSemana());
        disponibilidad.setHoraInicio(detalles.getHoraInicio());
        disponibilidad.setHoraFin(detalles.getHoraFin());

        TerapeutaDisponibilidad actualizado = terapeutaDisponibilidadService.ActualizarDisponibilidad(disponibilidad);

        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("eliminarDisponibilidadId/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable int id) {
        Optional<TerapeutaDisponibilidad> disponibilidad = terapeutaDisponibilidadService.ObtenerDisponibilidad(id);

        if (!disponibilidad.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Disponibilidad con ID " + id + " no encontrado.");
        }

        terapeutaDisponibilidadService.Eliminar(disponibilidad.get().getId());

        return ResponseEntity.ok("Disponibilidad eliminado correctamente.");
    }
}
