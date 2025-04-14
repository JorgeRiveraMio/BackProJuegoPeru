package com.projuegoperu.BackProJuegoPeru.Models.DAO;

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
@PrimaryKeyJoinColumn(name = "idPersona")
public class EmpleadoDao  extends  UsuarioDao{

    @Enumerated(EnumType.STRING)
    private EstadoEmpleado estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_empleado")
    private TipoEmpleado tipoEmpleado;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;
}
