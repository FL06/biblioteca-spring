package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_emprestimo")
@Getter @Setter @NoArgsConstructor
public class ItemEmprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade = 1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "emprestimo_id")
    private Emprestimo emprestimo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private Livro livro;
}
