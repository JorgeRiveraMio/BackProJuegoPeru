package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_terapia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteTerapia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con Paciente
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(length = 50, nullable = false)
    private String tipo; // pdf, excel

    @Column(name = "ruta_archivo", length = 255, nullable = false)
    private String rutaArchivo;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    // Usuario que genera el reporte
    @ManyToOne
    @JoinColumn(name = "generado_por", nullable = false)
    private Usuario generadoPor;

}
