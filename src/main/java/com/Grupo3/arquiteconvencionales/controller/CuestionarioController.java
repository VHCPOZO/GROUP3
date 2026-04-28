package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.config.Constantes;
import com.Grupo3.arquiteconvencionales.model.Pregunta;
import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CuestionarioController {

    private final CuestionarioService cuestionarioService;
    private final UsuarioRepository usuarioRepository;


    @GetMapping(Constantes.VIEW_CUESTIONARIO)
    public String mostrarCuestionario(final Model model) {
        final List<Pregunta> preguntas = cuestionarioService.obtenerTodasLasPreguntas();
        model.addAttribute("preguntas", preguntas);
        return Constantes.VIEW_CUESTIONARIO;
    }

    @PostMapping(Constantes.VIEW_CUESTIONARIO)
    public String procesarCuestionario(
            @RequestParam final Map<String, String> params,
            final Authentication authentication,
            final Model model) {
        
        final String username = authentication.getName();
        final Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(Constantes.ERR_USUARIO_NO_ENCONTRADO));

        final Map<String, String> respuestas = cuestionarioService.filtrarRespuestas(params);

        final Resultado resultado = cuestionarioService.guardarResultado(usuario.getId(), respuestas);
        model.addAttribute("resultado", resultado);
        
        return Constantes.VIEW_RESULTADO;
    }
}