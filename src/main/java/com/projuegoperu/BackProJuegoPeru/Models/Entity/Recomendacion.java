package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "recomendacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recomendacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate fecha;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    // Relación con Paciente
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Relación con Terapeuta (Empleado)
    @ManyToOne
    @JoinColumn(name = "terapeuta_id", nullable = false)
    private Empleado terapeuta;

    // Relación con Usuario que la creó (Admin)
    @ManyToOne
    @JoinColumn(name = "creado_por", nullable = false)
    private Empleado creadoPor;

}
