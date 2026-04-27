package com.Grupo3.arquiteconvencionales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    // Página principal / inicio - redirige a login si no está autenticado
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Información sobre arquitectura convencional
    @GetMapping("/info-arquitectura")
    public String infoArquitectura(Model model) {
        return "info-arquitectura";
    }
}