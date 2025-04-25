package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthValidateCodRequest(
        @NotBlank String username,
        @NotBlank String code
) {
}
