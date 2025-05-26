package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "idUsuario") // Herencia JOINED con Usuario
public class Empleado extends Usuario {



    @Column(name = "especialidad")
    private String especialidad; // Opcional, si es TERAPEUTA

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_empleado", nullable = false)
    private EstadoEmpleado estadoEmpleado; // LIBRE, OCUPADO, VACACIONES

}
