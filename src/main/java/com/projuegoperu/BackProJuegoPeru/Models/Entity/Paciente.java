package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paciente")

public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacio")
    private String apellido;

    @NotNull
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(unique = true)
    @NotBlank(message = "El DNI no puede estar vacio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;
    @NotBlank(message = "El direccion no puede estar vacio")
    private String direccion;
    @NotBlank(message = "El telefono no puede estar vacio")
    private String telefono;
    @NotBlank(message = "El escuela no puede estar vacio")
    private String escuela;
    @NotBlank(message = "El gradoEscolar no puede estar vacio")
    private String gradoEscolar;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable=false)
    private Tutor tutor;

}
