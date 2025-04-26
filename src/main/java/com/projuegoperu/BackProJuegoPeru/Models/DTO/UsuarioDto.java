package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoUsuario")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClienteDto.class, name = "CLIENTE"),
    @JsonSubTypes.Type(value = EmpleadoDto.class, name = "EMPLEADO")
})
public class UsuarioDto {

    @NotBlank(message = "El name no puede estar vacío")
    private String name;

    @NotBlank(message = "El lastname no puede estar vacío")
    private String lastname;

    @NotBlank(message = "El dni no puede estar vacío")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "El username no puede estar vacío")
    private String username;
    @NotBlank(message = "El password no puede estar vacío")

    private String password;

    private LocalDateTime creationDate;

    private TipoUsuario tipoUsuario; 

    @NotEmpty(message = "Debe asignar al menos un rol")
    private RolDto rol;

    

}


