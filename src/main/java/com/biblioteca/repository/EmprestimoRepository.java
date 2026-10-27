package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findAllByOrderByIdDesc();

    long countByStatusNot(StatusEmprestimo status);
}
