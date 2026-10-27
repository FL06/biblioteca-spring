package com.biblioteca.controller;

import com.biblioteca.model.Leitor;
import com.biblioteca.service.LeitorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/leitores")
public class LeitorController {
    private final LeitorService service;

    public LeitorController(LeitorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("leitores", service.listar());
        return "leitores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("leitor", new Leitor());
        return "leitores/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("leitor", service.buscarPorId(id));
        return "leitores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Leitor leitor, RedirectAttributes ra) {
        try {
            service.salvar(leitor);
            ra.addFlashAttribute("sucesso", "Leitor salvo com sucesso!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "E-mail ou CPF já cadastrado.");
        }
        return "redirect:/leitores";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("sucesso", "Leitor excluído!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Não é possível excluir: o leitor possui empréstimos.");
        }
        return "redirect:/leitores";
    }
}
