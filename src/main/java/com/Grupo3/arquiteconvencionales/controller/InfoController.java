package com.Grupo3.arquiteconvencionales.controller;

import com.Grupo3.arquiteconvencionales.config.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InfoController {

    @GetMapping("/")
    public String index() {
        return Constantes.VIEW_INDEX;
    }

    @GetMapping(Constantes.VIEW_INFO_ARQUITECTURA)
    public String infoArquitectura(final Model model) {
        return Constantes.VIEW_INFO_ARQUITECTURA;
    }
}