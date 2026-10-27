# 📚 Sistema de Biblioteca Comunitária

Sistema web desenvolvido com Spring Boot, Spring MVC, Thymeleaf e Spring Data JPA.

## Tema
Gerenciamento de acervo, leitores e empréstimos de uma biblioteca.

## Tecnologias
- Java 17+, Spring Boot, Spring MVC, Spring Data JPA
- Thymeleaf, HTML/CSS/JS
- Banco de dados: MySQL / PostgreSQL

## Arquitetura MVC
- **Model:** Entity, Repository, Service
- **Controller:** Controllers (acessam apenas Services)
- **View:** Templates Thymeleaf

## Protótipos de tela

### Home
```
+--------------------------------------------------+
|  📚 Biblioteca Comunitária                       |
|  [Livros] [Autores] [Categorias] [Leitores]      |
|  [Empréstimos]                                   |
+--------------------------------------------------+
|   Bem-vindo!                                     |
|   +---------+ +---------+ +---------+            |
|   | 120     | | 45      | | 8       |            |
|   | Livros  | | Leitores| | Ativos  |            |
|   +---------+ +---------+ +---------+            |
+--------------------------------------------------+
```

### Listagem de livros
```
+--------------------------------------------------+
|  Livros                          [+ Novo Livro]  |
|  Buscar: [____________] [🔍]                     |
+--------------------------------------------------+
| ID | Título        | Categoria  | Autor     | Ações |
|----|---------------|------------|-----------|-------|
| 1  | Dom Casmurro  | Romance    | Machado   | ✏️ 🗑️ |
| 2  | Clean Code    | Tecnologia | R. Martin | ✏️ 🗑️ |
+--------------------------------------------------+
```

### Formulário de livro
```
+--------------------------------------------------+
|  Cadastrar Livro                                 |
|  Título:     [______________________]            |
|  ISBN:       [______________________]            |
|  Ano:        [______]  Qtd: [____]               |
|  Categoria:  [ Selecione ▼ ]                     |
|  Autores:    [☐ Machado de Assis] [☐ R. Martin]  |
|                                                  |
|            [Salvar]  [Cancelar]                  |
+--------------------------------------------------+
```

### Novo empréstimo
```
+--------------------------------------------------+
|  Novo Empréstimo                                 |
|  Leitor:      [ Selecione ▼ ]                    |
|  Devolução:   [__/__/____]                       |
|  Livros:      [ Selecione ▼ ] Qtd [_] [Adicionar]|
|  +--------------------------------------------+  |
|  | Dom Casmurro        | 1 | 🗑️               |  |
|  +--------------------------------------------+  |
|            [Registrar]  [Cancelar]               |
+--------------------------------------------------+
```

> As demais entidades (Autores, Categorias e Leitores) seguem o mesmo padrão: listagem, formulário de cadastro/edição e exclusão com confirmação.

## Diagrama de classes

```mermaid
classDiagram
    class Categoria {
        -Long id
        -String nome
        -String descricao
    }
    class Autor {
        -Long id
        -String nome
        -String nacionalidade
        -LocalDate dataNascimento
    }
    class Livro {
        -Long id
        -String titulo
        -String isbn
        -Integer anoPublicacao
        -Integer quantidadeDisponivel
    }
    class Leitor {
        -Long id
        -String nome
        -String email
        -String telefone
        -String cpf
    }
    class Emprestimo {
        -Long id
        -LocalDate dataEmprestimo
        -LocalDate dataPrevistaDevolucao
        -LocalDate dataDevolucao
        -StatusEmprestimo status
    }
    class ItemEmprestimo {
        -Long id
        -Integer quantidade
    }
    class StatusEmprestimo {
        <<enumeration>>
        ATIVO
        DEVOLVIDO
        ATRASADO
    }

    Categoria "1" --> "0..*" Livro : possui
    Livro "0..*" --> "1..*" Autor : escrito por
    Leitor "1" --> "0..*" Emprestimo : realiza
    Emprestimo "1" *-- "1..*" ItemEmprestimo : contém
    Livro "1" --> "0..*" ItemEmprestimo : referenciado em
    Emprestimo --> StatusEmprestimo
```

## Diagrama MER

```mermaid
erDiagram
    CATEGORIA ||--o{ LIVRO : "classifica"
    LIVRO ||--o{ LIVRO_AUTOR : "tem"
    AUTOR ||--o{ LIVRO_AUTOR : "escreve"
    LEITOR ||--o{ EMPRESTIMO : "realiza"
    EMPRESTIMO ||--|{ ITEM_EMPRESTIMO : "contém"
    LIVRO ||--o{ ITEM_EMPRESTIMO : "é emprestado em"

    CATEGORIA {
        bigint id PK
        varchar nome
        varchar descricao
    }
    AUTOR {
        bigint id PK
        varchar nome
        varchar nacionalidade
        date data_nascimento
    }
    LIVRO {
        bigint id PK
        varchar titulo
        varchar isbn UK
        int ano_publicacao
        int quantidade_disponivel
        bigint categoria_id FK
    }
    LIVRO_AUTOR {
        bigint livro_id PK, FK
        bigint autor_id PK, FK
    }
    LEITOR {
        bigint id PK
        varchar nome
        varchar email UK
        varchar telefone
        varchar cpf UK
    }
    EMPRESTIMO {
        bigint id PK
        date data_emprestimo
        date data_prevista_devolucao
        date data_devolucao
        varchar status
        bigint leitor_id FK
    }
    ITEM_EMPRESTIMO {
        bigint id PK
        int quantidade
        bigint emprestimo_id FK
        bigint livro_id FK
    }
```
## Como executar
1. Java 17+ e Maven (ou uma IDE com suporte a Maven)
2. Execute a classe `BibliotecaApplication`
3. Acesse http://localhost:8080
