package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Livro;
import com.biblioteca.service.AutorService;
import com.biblioteca.service.CategoriaService;
import com.biblioteca.service.LivroService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {
    private final LivroService service;
    private final CategoriaService categoriaService;
    private final AutorService autorService;

    public LivroController(LivroService service, CategoriaService categoriaService, AutorService autorService) {
        this.service = service;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("livros", service.listar(busca));
        model.addAttribute("busca", busca);
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        prepararForm(model, new Livro(), new ArrayList<>());
        return "livros/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Livro livro = service.buscarPorId(id);
        List<Long> selecionados = livro.getAutores().stream().map(Autor::getId).toList();
        prepararForm(model, livro, selecionados);
        return "livros/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Livro livro,
                         @RequestParam(required = false) Long categoriaId,
                         @RequestParam(required = false) List<Long> autoresIds,
                         RedirectAttributes ra) {
        try {
            service.salvar(livro, categoriaId, autoresIds);
            ra.addFlashAttribute("sucesso", "Livro salvo com sucesso!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "ISBN já cadastrado.");
        }
        return "redirect:/livros";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("sucesso", "Livro excluído!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Não é possível excluir: o livro possui empréstimos.");
        }
        return "redirect:/livros";
    }

    private void prepararForm(Model model, Livro livro, List<Long> autoresSelecionados) {
        model.addAttribute("livro", livro);
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("autores", autorService.listar());
        model.addAttribute("autoresSelecionados", autoresSelecionados);
    }
}
