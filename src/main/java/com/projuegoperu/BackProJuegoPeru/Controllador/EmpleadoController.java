package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Services.EmpleadoService;
import com.projuegoperu.BackProJuegoPeru.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Empleado empleado) {
        try {
            Empleado guardado = empleadoService.Guardar(empleado);
            return ResponseEntity.ok(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/obtenerId/{id}")
    public ResponseEntity<?> obtenerPorDni(@PathVariable int id) {
        Optional<Empleado> obtenido = empleadoService.ObtenerEmpleadoID(id);
        if (obtenido.isPresent()) {
            return ResponseEntity.ok(obtenido.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Empleado con ID " + id + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/todosAdmin")
    public ResponseEntity<List<Empleado>> obtenerTodosAdmin() {
        List<Empleado> empleado = empleadoService.listarAdmins();
        return ResponseEntity.ok(empleado);
    }

    @GetMapping("/todosTerapeuta")
    public ResponseEntity<List<Empleado>> obtenerTodosTerapeuta() {
        List<Empleado> empleado = empleadoService.listarTerapeutas();
        return ResponseEntity.ok(empleado);
    }




    @PutMapping("/actualizarPorId/{id}")
    public ResponseEntity<Empleado> actualizarPaciente(@PathVariable int id, @RequestBody Empleado detallesEmpleado) {
        Optional<Empleado> empleadoExistente = empleadoService.ObtenerEmpleadoID(id);

        if (!empleadoExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Empleado empleado = empleadoExistente.get();

        // 🔁 Actualizar campos heredados de Usuario
        empleado.setName(detallesEmpleado.getName());
        empleado.setLastname(detallesEmpleado.getLastname());
        empleado.setDni(detallesEmpleado.getDni());
        empleado.setUsername(detallesEmpleado.getUsername());
        empleado.setPassword(detallesEmpleado.getPassword());
        empleado.setTipoUsuario(detallesEmpleado.getTipoUsuario());
        empleado.setRol(detallesEmpleado.getRol());

        // 🔁 Actualizar campos específicos de Empleado
        empleado.setEspecialidad(detallesEmpleado.getEspecialidad());
        empleado.setEstadoEmpleado(detallesEmpleado.getEstadoEmpleado());

        Empleado actualizado = empleadoService.Guardar(empleado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("eliminarEmpleadoId/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable int id) {
        Optional<Empleado> empleado = empleadoService.ObtenerEmpleadoID(id);

        if (!empleado.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        empleadoService.Eliminar(empleado.get().getIdUsuario());
        return ResponseEntity.noContent().build(); // 204 No Content si fue eliminado correctamente
    }
}
