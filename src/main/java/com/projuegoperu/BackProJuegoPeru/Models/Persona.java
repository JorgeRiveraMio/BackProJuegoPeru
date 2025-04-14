package com.projuegoperu.BackProJuegoPeru.Models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Inheritance(strategy = InheritanceType.JOINED)

@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPersona;

    @NotBlank(message = "El name no puede estar vacio")
    private String name;

    @NotBlank(message = "El lastname no puede estar vacio")
    private String lastname;

    @NotBlank(message = "El DNI no puede estar vacio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    public Persona(int idPersona, String name, String lastname, String dni) {
        this.idPersona = idPersona;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
    }

    public Persona() {

    }
}
