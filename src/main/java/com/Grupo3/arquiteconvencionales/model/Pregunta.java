package com.Grupo3.arquiteconvencionales.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "preguntas")
@Data
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

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public String getOpcionA() { return opcionA; }
    public void setOpcionA(String opcionA) { this.opcionA = opcionA; }

    public String getOpcionB() { return opcionB; }
    public void setOpcionB(String opcionB) { this.opcionB = opcionB; }

    public String getOpcionC() { return opcionC; }
    public void setOpcionC(String opcionC) { this.opcionC = opcionC; }

    public String getOpcionD() { return opcionD; }
    public void setOpcionD(String opcionD) { this.opcionD = opcionD; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
}