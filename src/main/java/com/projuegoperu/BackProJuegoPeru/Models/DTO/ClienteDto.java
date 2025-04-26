package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoCliente;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ClienteDto extends UsuarioDto {
    
    private String direccion; 

    private String telefono; 

    @NotNull(message = "El estado del cliente es obligatorio")
    private EstadoCliente estado;
    
    public ClienteDto() {
        this.setTipoUsuario(TipoUsuario.CLIENTE);
    }
}
