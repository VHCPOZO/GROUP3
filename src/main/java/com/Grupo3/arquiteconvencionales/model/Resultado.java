package com.Grupo3.arquiteconvencionales.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
@Getter
@Setter
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer puntaje;

    @Column(nullable = false)
    private Integer totalPreguntas;

    @Column(nullable = false)
    private Integer respuestasCorrectas;

    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDateTime fechaRealizacion;

    @Column
    private String tema;

    public Resultado() {}

    public Resultado(Usuario usuario, Integer puntaje, Integer totalPreguntas,
                    Integer respuestasCorrectas, String tema) {
        this.usuario = usuario;
        this.puntaje = puntaje;
        this.totalPreguntas = totalPreguntas;
        this.respuestasCorrectas = respuestasCorrectas;
        this.tema = tema;
        this.fechaRealizacion = LocalDateTime.now();
    }
}