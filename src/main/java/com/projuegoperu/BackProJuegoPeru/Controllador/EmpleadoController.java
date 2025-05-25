package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.EmpleadoDto;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import com.projuegoperu.BackProJuegoPeru.Services.EmpleadoService;
import com.projuegoperu.BackProJuegoPeru.Services.UserDetailsServiceImpl;
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

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private RolRepository rolRepository;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody EmpleadoDto empleado) {
        try {

            userDetailService.createEmpleado(empleado);
            return ResponseEntity.ok("Se guardo correctamente el empleado");
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
    public ResponseEntity<?> actualizarPaciente(@PathVariable int id, @RequestBody EmpleadoDto detallesEmpleado) {
        Optional<Empleado> empleadoExistente = empleadoService.ObtenerEmpleadoID(id);

        if (!empleadoExistente.isPresent()) {
            // Retornar un mensaje personalizado si no se encuentra
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Empleado con ID " + id + " no encontrado");
        }

        Empleado empleado = empleadoExistente.get();

        // 🔁 Actualizar campos heredados de Usuario
        empleado.setName(detallesEmpleado.getName());
        empleado.setLastname(detallesEmpleado.getLastname());
        empleado.setDni(detallesEmpleado.getDni());
        empleado.setUsername(detallesEmpleado.getUsername());
        empleado.setPassword(detallesEmpleado.getPassword());
        // Si necesitas obtener el Rol a partir del ID (idRol), hazlo aquí
        Optional<Rol> rol = rolRepository.findById(detallesEmpleado.getIdRol()); // Asegúrate de tener este método en rolService
        if (rol.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Rol con ID " + id + " no encontrado");
        }
        empleado.setRol(rol.get());

        // 🔁 Actualizar campos específicos de Empleado
        empleado.setEspecialidad(detallesEmpleado.getEspecialidad());
        empleado.setEstadoEmpleado(detallesEmpleado.getEstadoEmpleado());

        Empleado actualizado = empleadoService.Guardar(empleado);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("eliminarEmpleadoId/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable int id) {
        Optional<Empleado> empleado = empleadoService.ObtenerEmpleadoID(id);

        if (!empleado.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Empleado con ID " + id + " no encontrado.");
        }

        empleadoService.Eliminar(empleado.get().getIdUsuario());

        return ResponseEntity.ok("Empleado eliminado correctamente.");
    }

}
