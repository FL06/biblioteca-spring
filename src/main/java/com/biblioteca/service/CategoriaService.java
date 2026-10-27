package com.biblioteca.service;

import com.biblioteca.model.Categoria;
import com.biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
    }

    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
