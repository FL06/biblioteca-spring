package com.biblioteca.service;

import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    private final AutorRepository repository;

    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    public List<Autor> listar() {
        return repository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Autor não encontrado."));
    }

    public Autor salvar(Autor autor) {
        return repository.save(autor);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
