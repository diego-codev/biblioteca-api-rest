CREATE TABLE IF NOT EXISTS pessoa (
    id_pessoa BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    logradouro VARCHAR(100),
    bairro VARCHAR(60),
    localidade VARCHAR(60),
    uf VARCHAR(2)
);
CREATE UNIQUE INDEX IF NOT EXISTS uq_pessoa_cpf ON pessoa(cpf);
CREATE UNIQUE INDEX IF NOT EXISTS uq_pessoa_email ON pessoa(email);

CREATE TABLE IF NOT EXISTS livro (
    id_livro BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    data_lancamento DATE,
    quantidade INTEGER
);


-- Tabela de emprestimo com PK composta (id_livro, id_pessoa)
CREATE TABLE IF NOT EXISTS emprestimo (
    id_livro BIGINT NOT NULL,
    id_pessoa BIGINT NOT NULL,
    data_emprestimo DATE,
    data_prevista_devolucao DATE,
    data_devolucao DATE,
    PRIMARY KEY (id_livro, id_pessoa),
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (id_livro) REFERENCES livro (id_livro) ON DELETE CASCADE,
    CONSTRAINT fk_emprestimo_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS solicitacao_emprestimo_externo (
    id BIGSERIAL PRIMARY KEY,
    nome_livro VARCHAR(255),
    id_pessoa BIGINT,
    data_solicitacao DATE,
    status VARCHAR(20),
    autor VARCHAR(255),
    data_lancamento DATE,
    CONSTRAINT fk_solicitacao_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa) ON DELETE SET NULL,
    CONSTRAINT chk_status_solicitacao CHECK (status IN ('PENDENTE','APROVADA','REJEITADA'))
);

-- Índices auxiliares
CREATE INDEX IF NOT EXISTS idx_emprestimo_livro ON emprestimo(id_livro);
CREATE INDEX IF NOT EXISTS idx_emprestimo_pessoa ON emprestimo(id_pessoa);
CREATE INDEX IF NOT EXISTS idx_solicitacao_pessoa ON solicitacao_emprestimo_externo(id_pessoa);
