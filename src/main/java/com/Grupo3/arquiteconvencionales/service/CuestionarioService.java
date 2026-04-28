package com.Grupo3.arquiteconvencionales.service;

import com.Grupo3.arquiteconvencionales.config.Constantes;
import com.Grupo3.arquiteconvencionales.model.Pregunta;
import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import com.Grupo3.arquiteconvencionales.repository.PreguntaRepository;
import com.Grupo3.arquiteconvencionales.repository.ResultadoRepository;
import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CuestionarioService {

    private final PreguntaRepository preguntaRepository;
    private final ResultadoRepository resultadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Pregunta> obtenerTodasLasPreguntas() {
        return preguntaRepository.findAllByOrderByIdAsc();
    }

    public List<Pregunta> obtenerPreguntasPorTema(final String tema) {
        return preguntaRepository.findByTema(tema);
    }

    @Transactional
    public Usuario registrarUsuario(final String username, final String password, final String email) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(Constantes.ERR_USUARIO_YA_EXISTE);
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(Constantes.ERR_EMAIL_YA_REGISTRADO);
        }

        final Usuario usuario = new Usuario(
            username,
            passwordEncoder.encode(password),
            email,
            Constantes.DEFAULT_ROLE
        );
        return usuarioRepository.save(usuario);
    }


    public Map<String, String> filtrarRespuestas(final Map<String, String> params) {
        return params.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(Constantes.PREFIJO_PREGUNTA))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Transactional
    public Resultado guardarResultado(final Long usuarioId, final Map<String, String> respuestas) {
        final Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.ERR_USUARIO_NO_ENCONTRADO));

        final List<Pregunta> preguntas = preguntaRepository.findAll();
        
        final long correctas = preguntas.stream()
                .filter(pregunta -> {
                    String respuestaUsuario = respuestas.get(Constantes.PREFIJO_PREGUNTA + pregunta.getId());
                    return respuestaUsuario != null 
                        && respuestaUsuario.equals(pregunta.getRespuestaCorrecta());
                })
                .count();

        final int puntaje = (int) ((double) correctas / preguntas.size() * 100);
        final Resultado resultado = new Resultado(
            usuario, 
            puntaje, 
            preguntas.size(), 
            (int) correctas, 
            Constantes.TEMA_DEFAULT
        );
        
        return resultadoRepository.save(resultado);
    }

    public List<Resultado> obtenerResultadosUsuario(final String username) {
        return resultadoRepository.findByUsuarioUsername(username);
    }

    public List<Resultado> obtenerTodosLosResultados() {
        return resultadoRepository.findAllByOrderByFechaRealizacionDesc();
    }
}