package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.config.Constantes;
import com.Grupo3.arquiteconvencionales.service.CuestionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AuthController {

    private final CuestionarioService cuestionarioService;

    @GetMapping(Constantes.VIEW_LOGIN)
    public String login() {
        return Constantes.VIEW_LOGIN;
    }

    @GetMapping(Constantes.VIEW_REGISTRO)
    public String registro() {
        return Constantes.VIEW_REGISTRO;
    }

    @PostMapping(Constantes.VIEW_REGISTRO)
    public String procesarRegistro(
            @RequestParam final String username,
            @RequestParam final String password,
            @RequestParam final String email,
            final Model model) {
        try {
            cuestionarioService.registrarUsuario(username, password, email);
            return "redirect:" + Constantes.VIEW_LOGIN + "?registroExitoso";
        } catch (final IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return Constantes.VIEW_REGISTRO;
        }
    }
}