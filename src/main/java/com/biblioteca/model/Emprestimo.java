package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emprestimo")
@Getter @Setter @NoArgsConstructor
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "leitor_id")
    private Leitor leitor;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEmprestimo> itens = new ArrayList<>();
}
