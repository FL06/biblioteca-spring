package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.ItemEmprestimo;
import com.biblioteca.model.Leitor;
import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusEmprestimo;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LeitorRepository;
import com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository repository;
    private final LeitorRepository leitorRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository repository,
                             LeitorRepository leitorRepository,
                             LivroRepository livroRepository) {
        this.repository = repository;
        this.leitorRepository = leitorRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional
    public List<Emprestimo> listar() {
        List<Emprestimo> lista = repository.findAllByOrderByIdDesc();
        LocalDate hoje = LocalDate.now();
        for (Emprestimo e : lista) {
            if (e.getStatus() == StatusEmprestimo.ATIVO
                    && e.getDataPrevistaDevolucao() != null
                    && e.getDataPrevistaDevolucao().isBefore(hoje)) {
                e.setStatus(StatusEmprestimo.ATRASADO);
            }
        }
        return lista;
    }

    @Transactional
    public Emprestimo registrar(Long leitorId, LocalDate dataPrevista, List<Long> livrosIds) {
        if (livrosIds == null || livrosIds.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um livro.");
        }
        Leitor leitor = leitorRepository.findById(leitorId)
                .orElseThrow(() -> new IllegalArgumentException("Leitor não encontrado."));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLeitor(leitor);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(dataPrevista);
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        for (Long livroId : livrosIds) {
            Livro livro = livroRepository.findById(livroId)
                    .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));
            if (livro.getQuantidadeDisponivel() == null || livro.getQuantidadeDisponivel() <= 0) {
                throw new IllegalArgumentException("Livro sem estoque: " + livro.getTitulo());
            }
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);

            ItemEmprestimo item = new ItemEmprestimo();
            item.setEmprestimo(emprestimo);
            item.setLivro(livro);
            item.setQuantidade(1);
            emprestimo.getItens().add(item);
        }
        return repository.save(emprestimo);
    }

    @Transactional
    public void devolver(Long id) {
        Emprestimo emprestimo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));
        if (emprestimo.getStatus() != StatusEmprestimo.DEVOLVIDO) {
            devolverEstoque(emprestimo);
            emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
            emprestimo.setDataDevolucao(LocalDate.now());
        }
    }

    @Transactional
    public void excluir(Long id) {
        Emprestimo emprestimo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));
        if (emprestimo.getStatus() != StatusEmprestimo.DEVOLVIDO) {
            devolverEstoque(emprestimo);
        }
        repository.delete(emprestimo);
    }

    public long contarAtivos() {
        return repository.countByStatusNot(StatusEmprestimo.DEVOLVIDO);
    }

    private void devolverEstoque(Emprestimo emprestimo) {
        for (ItemEmprestimo item : emprestimo.getItens()) {
            Livro livro = item.getLivro();
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + item.getQuantidade());
        }
    }
}
