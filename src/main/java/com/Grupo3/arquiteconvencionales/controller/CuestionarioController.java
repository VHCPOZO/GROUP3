package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.model.Pregunta;
import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CuestionarioController {

    @Autowired
    private CuestionarioService cuestionarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar cuestionario
    @GetMapping("/cuestionario")
    public String mostrarCuestionario(Model model) {
        List<Pregunta> preguntas = cuestionarioService.obtenerTodasLasPreguntas();
        model.addAttribute("preguntas", preguntas);
        return "cuestionario";
    }

    // Procesar respuestas del cuestionario
    @PostMapping("/cuestionario")
    public String procesarCuestionario(@RequestParam Map<String, String> params,
                                       Authentication authentication,
                                       Model model) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Filtrar solo las respuestas de preguntas
        Map<String, String> respuestas = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("pregunta_")) {
                respuestas.put(entry.getKey(), entry.getValue());
            }
        }

        Resultado resultado = cuestionarioService.guardarResultado(usuario.getId(), respuestas);
        model.addAttribute("resultado", resultado);
        
        return "resultado";
    }
}