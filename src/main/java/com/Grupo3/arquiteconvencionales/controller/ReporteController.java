package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReporteController {

    @Autowired
    private CuestionarioService cuestionarioService;

    // 1. Vista para el Usuario Normal (Sus propias notas)
    @GetMapping("/mis-notas")
    public String verMisNotas(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Resultado> misResultados = cuestionarioService.obtenerResultadosUsuario(username);
        model.addAttribute("resultados", misResultados);
        
        return "mis-notas"; // Devolverá la plantilla mis-notas.html
    }

    // 2. Vista para el Administrador (Todas las notas)
    @GetMapping("/admin/reportes")
    public String verTodosLosReportes(Model model) {
        List<Resultado> todosLosResultados = cuestionarioService.obtenerTodosLosResultados();
        model.addAttribute("resultados", todosLosResultados);
        
        // Podemos añadir estadísticas simples
        double promedioGeneral = todosLosResultados.stream()
                .mapToInt(Resultado::getPuntaje)
                .average()
                .orElse(0.0);
                
        model.addAttribute("promedioGeneral", Math.round(promedioGeneral * 100.0) / 100.0);
        model.addAttribute("totalEvaluaciones", todosLosResultados.size());
        
        return "admin-reportes"; // Devolverá la plantilla admin-reportes.html
    }
}