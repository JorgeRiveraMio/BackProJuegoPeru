package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Services.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.projuegoperu.BackProJuegoPeru.Services.UsuarioService;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/guardar")
    public ResponseEntity<Paciente> guardar(@RequestBody Paciente paciente) {
        System.out.println("Tutor recibido: " + paciente.getTutor());
        System.out.println("ID del tutor: " + paciente.getTutor().getIdUsuario());
        System.out.println("Nombre del tutor: " + paciente.getTutor().getName());

        // Guardar el paciente utilizando el servicio
        Paciente guardado = pacienteService.Guardar(paciente);

        // Retornar la respuesta con el paciente guardado
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

    @GetMapping("/tutor/{tutorId}")
    public List<Paciente> obtenerPacientesPorTutor(@PathVariable Integer tutorId) {
        return pacienteService.obtenerPacientesPorTutorId(tutorId);
    }
    
    @GetMapping("/admin/pacientes")
    public ResponseEntity<List<Paciente>> obtenerTodosLosPacientes(Principal principal) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(principal.getName());
        if (usuario.isPresent() && usuario.get().getRol().getName().equals("ROLE_ADMIN")) {
            List<Paciente> pacientes = pacienteService.obtenerPacientesConTutor();
            return ResponseEntity.ok(pacientes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
    }
}
