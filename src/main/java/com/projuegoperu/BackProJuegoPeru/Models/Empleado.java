package com.projuegoperu.BackProJuegoPeru.Models;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoEmpleado;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "idPersona")  // Correcto para herencia JOINED
public class Empleado extends Usuario {

    @Enumerated(EnumType.STRING)
    private EstadoEmpleado estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_empleado")
    private TipoEmpleado tipoEmpleado;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    public Empleado(int idPersona, String name, String lastname, String dni, String username, String password, LocalDateTime creationDate, String tipoUsuario, Set<Rol> roles, EstadoEmpleado estado, TipoEmpleado tipoEmpleado, LocalDate fechaContratacion) {
        super(idPersona, name, lastname, dni, username, password, creationDate, tipoUsuario, roles);
        this.estado = estado;
        this.tipoEmpleado = tipoEmpleado;
        this.fechaContratacion = fechaContratacion;
    }
}
