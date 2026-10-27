package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class LivroService {
    private final LivroRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final AutorRepository autorRepository;

    public LivroService(LivroRepository repository,
                        CategoriaRepository categoriaRepository,
                        AutorRepository autorRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.autorRepository = autorRepository;
    }

    public List<Livro> listar(String busca) {
        if (busca == null || busca.isBlank()) {
            return repository.findAll();
        }
        return repository.findByTituloContainingIgnoreCase(busca.trim());
    }

    public Livro buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));
    }

    public Livro salvar(Livro livro, Long categoriaId, List<Long> autoresIds) {
        if (categoriaId != null) {
            livro.setCategoria(categoriaRepository.findById(categoriaId).orElse(null));
        }
        if (autoresIds != null && !autoresIds.isEmpty()) {
            livro.setAutores(new HashSet<>(autorRepository.findAllById(autoresIds)));
        } else {
            livro.setAutores(new HashSet<>());
        }
        return repository.save(livro);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public List<Livro> listarDisponiveis() {
        return repository.findAll().stream()
                .filter(l -> l.getQuantidadeDisponivel() != null && l.getQuantidadeDisponivel() > 0)
                .toList();
    }

    public long contar() {
        return repository.count();
    }
}
