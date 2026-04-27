package com.Grupo3.arquiteconvencionales.service;

import com.Grupo3.arquiteconvencionales.model.Pregunta;
import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import com.Grupo3.arquiteconvencionales.repository.PreguntaRepository;
import com.Grupo3.arquiteconvencionales.repository.ResultadoRepository;
import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class CuestionarioService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todas las preguntas
    public List<Pregunta> obtenerTodasLasPreguntas() {
        return preguntaRepository.findAllByOrderByIdAsc();
    }

    // Obtener preguntas por tema
    public List<Pregunta> obtenerPreguntasPorTema(String tema) {
        return preguntaRepository.findByTema(tema);
    }

    // Registrar usuario
    @Transactional
    public Usuario registrarUsuario(String username, String password, String email) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        Usuario usuario = new Usuario(
            username,
            passwordEncoder.encode(password),
            email,
            "ROLE_USER"
        );
        return usuarioRepository.save(usuario);
    }

    // Guardar resultado del cuestionario
    @Transactional
    public Resultado guardarResultado(Long usuarioId, Map<String, String> respuestas) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Pregunta> preguntas = preguntaRepository.findAll();
        int correctas = 0;

        for (Pregunta pregunta : preguntas) {
            String respuestaUsuario = respuestas.get("pregunta_" + pregunta.getId());
            if (respuestaUsuario != null && respuestaUsuario.equals(pregunta.getRespuestaCorrecta())) {
                correctas++;
            }
        }

        int puntaje = (int) ((double) correctas / preguntas.size() * 100);
        Resultado resultado = new Resultado(usuario, puntaje, preguntas.size(), correctas, "General");
        
        return resultadoRepository.save(resultado);
    }

    // Obtener resultados del usuario
    public List<Resultado> obtenerResultadosUsuario(String username) {
        return resultadoRepository.findByUsuarioUsername(username);
    }

    // Obtener todas las notas del sistema (Para el Administrador)
    public List<Resultado> obtenerTodosLosResultados() {
        return resultadoRepository.findAllByOrderByFechaRealizacionDesc();
    }
}