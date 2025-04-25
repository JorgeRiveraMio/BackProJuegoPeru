package com.projuegoperu.BackProJuegoPeru.Models.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDto {
    @NotBlank(message = "El name no puede estar vacío")
    private String name;

    @NotBlank(message = "El lastname no puede estar vacío")
    private String lastname;

    @NotBlank(message = "El dni no puede estar vacío")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

}
