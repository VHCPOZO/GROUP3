package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private CuestionarioService cuestionarioService;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   Model model) {
        try {
            cuestionarioService.registrarUsuario(username, password, email);
            return "redirect:/login?registroExitoso";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}