package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoEmpleado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "idPersona") // Herencia JOINED con Usuario
public class Empleado extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_empleado", nullable = false)
    private TipoEmpleado tipoEmpleado; // ADMIN, TERAPEUTA

    @Column(name = "especialidad")
    private String especialidad; // Opcional, si es TERAPEUTA

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_empleado", nullable = false)
    private EstadoEmpleado estadoEmpleado; // LIBRE, OCUPADO, VACACIONES

}
