package com.biblioteca.controller;

import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LeitorService;
import com.biblioteca.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final LivroService livroService;
    private final LeitorService leitorService;
    private final EmprestimoService emprestimoService;

    public HomeController(LivroService livroService, LeitorService leitorService, EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.leitorService = leitorService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalLivros", livroService.contar());
        model.addAttribute("totalLeitores", leitorService.contar());
        model.addAttribute("totalAtivos", emprestimoService.contarAtivos());
        return "index";
    }
}
