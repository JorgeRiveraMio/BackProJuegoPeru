package com.projuegoperu.BackProJuegoPeru.Models.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoUsuario {
    CLIENTE,
    EMPLEADO;

    @JsonCreator
    public static TipoUsuario fromString(String value) {
        if (value != null) {
            for (TipoUsuario tipo : TipoUsuario.values()) {
                if (tipo.name().equalsIgnoreCase(value)) {
                    return tipo;
                }
            }
        }
        return null; // Puedes lanzar una excepción aquí si prefieres manejar el error de forma distinta.
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
