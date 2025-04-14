package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthCreateUserRequest(

        @NotBlank String username,
        @NotBlank String password,

        // Campos heredados de Persona
        @NotBlank(message = "El name no puede estar vacío") String name,
        @NotBlank(message = "El lastname no puede estar vacío") String lastname,
        @NotBlank(message = "El DNI no puede estar vacío")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos") String dni,

        @Valid AuthCreateRoleRequest roleRequest
) {
}