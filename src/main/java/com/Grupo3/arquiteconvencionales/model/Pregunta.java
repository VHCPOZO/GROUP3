package com.Grupo3.arquiteconvencionales.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "preguntas")
@Getter
@Setter
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pregunta", columnDefinition = "TEXT", nullable = false)
    private String pregunta;

    @Column(name = "opcion_a", columnDefinition = "TEXT", nullable = false)
    private String opcionA;

    @Column(name = "opcion_b", columnDefinition = "TEXT", nullable = false)
    private String opcionB;

    @Column(name = "opcion_c", columnDefinition = "TEXT", nullable = false)
    private String opcionC;

    @Column(name = "opcion_d", columnDefinition = "TEXT", nullable = false)
    private String opcionD;

    @Column(name = "respuesta_correcta", nullable = false)
    private String respuestaCorrecta;

    @Column(name = "tema")
    private String tema;

    public Pregunta() {}

    public Pregunta(String pregunta, String opcionA, String opcionB, String opcionC,
                    String opcionD, String respuestaCorrecta, String tema) {
        this.pregunta = pregunta;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
        this.tema = tema;
    }
}