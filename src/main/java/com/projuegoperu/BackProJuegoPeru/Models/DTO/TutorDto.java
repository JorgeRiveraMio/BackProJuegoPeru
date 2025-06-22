package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoTutor;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class TutorDto extends UsuarioDto {
    
    private int idUsuario;
    
    private String direccion; 

    private String telefono; 

    @NotNull(message = "El estado del cliente es obligatorio")
    private EstadoTutor estado;
    
    public TutorDto() {

    }
}
