package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password) {
}
