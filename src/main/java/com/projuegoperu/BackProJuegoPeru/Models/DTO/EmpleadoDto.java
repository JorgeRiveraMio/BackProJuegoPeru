package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class EmpleadoDto extends UsuarioDto {

    private String especialidad; 

    @NotNull(message = "El estado del empleado es obligatorio")
    private EstadoEmpleado estadoEmpleado;

    public EmpleadoDto() {
        this.setTipoUsuario(TipoUsuario.EMPLEADO);
    }

}

