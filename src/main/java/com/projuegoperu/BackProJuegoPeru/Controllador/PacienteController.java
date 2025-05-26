package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/guardar")
    public ResponseEntity<Paciente> guardar(@RequestBody  Paciente paciente) {

        Paciente guardado = pacienteService.Guardar(paciente);
        return ResponseEntity.ok(guardado);
    }


    @GetMapping("/obtenerDni/{dni}")
    public ResponseEntity<Paciente> obtenerPorDni(@PathVariable String dni) {

        Optional<Paciente> obtenido = pacienteService.ObtenerPaciente(dni);
        if (obtenido.isPresent()) {
            return ResponseEntity.ok(obtenido.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/todos")
    public ResponseEntity<List<Paciente>> obtenerTodos() {
        List<Paciente> pacientes = pacienteService.Listar();
        return ResponseEntity.ok(pacientes);
    }


    @PutMapping("/actualizar/{dni}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable String dni, @RequestBody Paciente pacienteDetalles) {
        Optional<Paciente> pacienteExistente = pacienteService.ObtenerPaciente(dni);

        if (!pacienteExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Paciente paciente = pacienteExistente.get();

        // 🔁 Actualiza campos (puedes validar o usar un DTO si prefieres)
        paciente.setNombre(pacienteDetalles.getNombre());
        paciente.setApellido(pacienteDetalles.getApellido());
        paciente.setFechaNacimiento(pacienteDetalles.getFechaNacimiento());
        paciente.setSexo(pacienteDetalles.getSexo());
        paciente.setDni(pacienteDetalles.getDni());
        paciente.setDireccion(pacienteDetalles.getDireccion());
        paciente.setTelefono(pacienteDetalles.getTelefono());
        paciente.setEscuela(pacienteDetalles.getEscuela());
        paciente.setGradoEscolar(pacienteDetalles.getGradoEscolar());
        paciente.setTutor(pacienteDetalles.getTutor()); // solo si ya está registrado

        Paciente actualizado = pacienteService.ActualizarUsuario(paciente);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("eliminarPaciente/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable String id) {
        Optional<Paciente> paciente = pacienteService.ObtenerPaciente(id);

        if (!paciente.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        pacienteService.Eliminar(paciente.get().getId());
        return ResponseEntity.noContent().build(); // 204 No Content si fue eliminado correctamente
    }





}
