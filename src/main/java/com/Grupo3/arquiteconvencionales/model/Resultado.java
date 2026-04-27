package com.Grupo3.arquiteconvencionales.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
@Data
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

    public Resultado(Usuario usuario, Integer puntaje, Integer totalPreguntas, Integer respuestasCorrectas, String tema) {
        this.usuario = usuario;
        this.puntaje = puntaje;
        this.totalPreguntas = totalPreguntas;
        this.respuestasCorrectas = respuestasCorrectas;
        this.tema = tema;
        this.fechaRealizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }

    public Integer getTotalPreguntas() { return totalPreguntas; }
    public void setTotalPreguntas(Integer totalPreguntas) { this.totalPreguntas = totalPreguntas; }

    public Integer getRespuestasCorrectas() { return respuestasCorrectas; }
    public void setRespuestasCorrectas(Integer respuestasCorrectas) { this.respuestasCorrectas = respuestasCorrectas; }

    public LocalDateTime getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
}