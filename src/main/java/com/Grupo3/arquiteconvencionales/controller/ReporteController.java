package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.config.Constantes;
import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReporteController {

    private final CuestionarioService cuestionarioService;

    
    @GetMapping(Constantes.VIEW_MIS_NOTAS)
    public String verMisNotas(final Authentication authentication, final Model model) {
        final String username = authentication.getName();
        final List<Resultado> misResultados = cuestionarioService.obtenerResultadosUsuario(username);
        model.addAttribute("resultados", misResultados);
        
        return Constantes.VIEW_MIS_NOTAS;
    }

    @GetMapping("/admin/reportes")
    public String verTodosLosReportes(final Model model) {
        final List<Resultado> todosLosResultados = cuestionarioService.obtenerTodosLosResultados();
        model.addAttribute("resultados", todosLosResultados);
        
        final double promedioGeneral = todosLosResultados.stream()
                .mapToInt(Resultado::getPuntaje)
                .average()
                .orElse(0.0);
                
        model.addAttribute("promedioGeneral", Math.round(promedioGeneral * 100.0) / 100.0);
        model.addAttribute("totalEvaluaciones", todosLosResultados.size());
        
        return Constantes.VIEW_ADMIN_REPORTES;
    }
}