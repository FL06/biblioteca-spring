package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.service.AutorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autores")
public class AutorController {
    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", service.listar());
        return "autores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("autor", new Autor());
        return "autores/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("autor", service.buscarPorId(id));
        return "autores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Autor autor, RedirectAttributes ra) {
        service.salvar(autor);
        ra.addFlashAttribute("sucesso", "Autor salvo com sucesso!");
        return "redirect:/autores";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("sucesso", "Autor excluído!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Não é possível excluir: o autor está vinculado a livros.");
        }
        return "redirect:/autores";
    }
}
