package com.biblioteca.controller;

import com.biblioteca.model.Categoria;
import com.biblioteca.service.CategoriaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listar());
        return "categorias/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.buscarPorId(id));
        return "categorias/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Categoria categoria, RedirectAttributes ra) {
        service.salvar(categoria);
        ra.addFlashAttribute("sucesso", "Categoria salva com sucesso!");
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("sucesso", "Categoria excluída!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Não é possível excluir: existem livros nesta categoria.");
        }
        return "redirect:/categorias";
    }
}
