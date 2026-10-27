package com.biblioteca.repository;

import com.biblioteca.model.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {
}
