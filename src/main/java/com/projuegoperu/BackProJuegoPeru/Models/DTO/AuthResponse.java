package com.projuegoperu.BackProJuegoPeru.Models.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;


@JsonPropertyOrder({"username", "message", "rol","status", "jwt"})
public record AuthResponse(
        String username,
        String message,
        List<String> rol,
        String jwt,
        Boolean status) {
}