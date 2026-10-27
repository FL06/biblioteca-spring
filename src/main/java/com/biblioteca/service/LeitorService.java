package com.biblioteca.service;

import com.biblioteca.model.Leitor;
import com.biblioteca.repository.LeitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeitorService {
    private final LeitorRepository repository;

    public LeitorService(LeitorRepository repository) {
        this.repository = repository;
    }

    public List<Leitor> listar() {
        return repository.findAll();
    }

    public Leitor buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Leitor não encontrado."));
    }

    public Leitor salvar(Leitor leitor) {
        return repository.save(leitor);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public long contar() {
        return repository.count();
    }
}
