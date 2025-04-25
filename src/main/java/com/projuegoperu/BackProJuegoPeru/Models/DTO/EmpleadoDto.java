package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoEmpleado;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDto extends UsuarioDto {
    @NotNull(message = "El tipo de empleado es obligatorio")
    private TipoEmpleado tipoEmpleado;

    @NotNull(message = "El estado del empleado es obligatorio")
    private EstadoEmpleado estadoEmpleado;

    private String especialidad; // solo si es TERAPEUTA
}

