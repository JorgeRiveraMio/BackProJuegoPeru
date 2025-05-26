package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
@Getter
@Setter
@Table(name = "evaluacion_ingreso")
public class EvaluacionIngreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private String tipo; // 'nuevo', 'terapia_previa'
    private Boolean tieneInformes;
    private Boolean requiereEvaluacion;
    private LocalDate fechaEvaluacion;
    private Integer planALasSesiones; // 4 (caso 2), 0 si inmediato, null si no aplica aún
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "tipo_evaluacion_id")
    private TipoEvaluacion tipoEvaluacion;
}
