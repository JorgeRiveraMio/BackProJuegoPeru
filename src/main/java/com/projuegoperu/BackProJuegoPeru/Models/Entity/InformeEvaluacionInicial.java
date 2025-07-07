package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoInforme;
import jakarta.persistence.*;
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
@Table(name = "informe_evaluacion_inicial")
public class InformeEvaluacionInicial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "fecha_ultima_terapia")
    private LocalDate fechaUltimaTerapia;

//    @Column(name = "archivo_url", length = 255)
//    private String archivoUrl;
    @Lob
    @Column(name = "archivo_pdf", nullable = false)
    private byte[] archivoPdf;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING) // o EnumType.ORDINAL si prefieres guardar el índice, no recomendado
    @Column(name = "estado_informe")
    private EstadoInforme estadoInforme;
}
