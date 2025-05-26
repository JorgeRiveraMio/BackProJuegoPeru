package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Paciente;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Repository.EmpleadoRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private RolRepository rolRepository;

    public List<Empleado> listarAdmins() {
        return empleadoRepository.findByRolNombre("ROLE_ADMIN");
    }

    public List<Empleado> listarTerapeutas() {
        return empleadoRepository.findByRolNombre("ROLE_TERAPEUTA");
    }


    public Empleado Guardar(Empleado u) {
        Rol rol = rolRepository.findById(u.getRol().getId()).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        if ("ROLE_TUTOR".equalsIgnoreCase(rol.getName())) {
            throw new RuntimeException("No se puede asignar el rol ROLE_CLIENTE a un empleado.");
        }
        u.setRol(rol);

        // Guardar el paciente
        return empleadoRepository.save(u);
    }

    public Optional<Empleado> ObtenerEmpleadoID(int id) {
        return empleadoRepository.findById(id);
    }

    public Empleado ActualizarEmpleado(Empleado usu) {
        Empleado usuarioExistente = empleadoRepository.findById(usu.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con su DNI: " + usu.getDni()));


        return empleadoRepository.save(usuarioExistente);
    }
    public void Eliminar(int id) {
        empleadoRepository.deleteById(id);
    }
}
