package com.biblioteca.controller;

import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LeitorService;
import com.biblioteca.service.LivroService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {
    private final EmprestimoService service;
    private final LeitorService leitorService;
    private final LivroService livroService;

    public EmprestimoController(EmprestimoService service, LeitorService leitorService, LivroService livroService) {
        this.service = service;
        this.leitorService = leitorService;
        this.livroService = livroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", service.listar());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("leitores", leitorService.listar());
        model.addAttribute("livros", livroService.listarDisponiveis());
        model.addAttribute("sugestaoDevolucao", LocalDate.now().plusDays(14));
        return "emprestimos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam Long leitorId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPrevista,
                         @RequestParam(required = false) List<Long> livrosIds,
                         RedirectAttributes ra) {
        try {
            service.registrar(leitorId, dataPrevista, livrosIds);
            ra.addFlashAttribute("sucesso", "Empréstimo registrado!");
            return "redirect:/emprestimos";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/emprestimos/novo";
        }
    }

    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes ra) {
        service.devolver(id);
        ra.addFlashAttribute("sucesso", "Devolução registrada!");
        return "redirect:/emprestimos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        service.excluir(id);
        ra.addFlashAttribute("sucesso", "Empréstimo excluído!");
        return "redirect:/emprestimos";
    }
}
