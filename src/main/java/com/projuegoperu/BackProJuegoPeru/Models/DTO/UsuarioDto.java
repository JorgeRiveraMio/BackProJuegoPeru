package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.projuegoperu.BackProJuegoPeru.Models.DAO.RolDao;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;
import com.projuegoperu.BackProJuegoPeru.Models.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto  extends  PersonaDto{

    @NotBlank(message = "El username no puede estar vacío")
    private String username;
    @NotBlank(message = "El password no puede estar vacío")
    private String password;
    private LocalDateTime creationDate;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuario tipoUsuario;

    @NotEmpty(message = "Debe asignar al menos un rol")
    private List<String> roles;
}
