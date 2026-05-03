-- EcoRide schema
-- Cada subsistema vai acrescentando as suas tabelas a este ficheiro
-- pela ordem em que forem implementados.

-- =========================================================
-- SAutenticacao
-- =========================================================

CREATE TABLE IF NOT EXISTS Utilizador (
    id            INT          NOT NULL,
    password      VARCHAR(255) NOT NULL,
    idFuncionario INT          NOT NULL,
    cargo         ENUM('Gerente', 'GestorStock', 'Secretaria', 'Mecanico') NOT NULL,
    PRIMARY KEY (id)
);

-- =========================================================
-- SFuncionarios
-- =========================================================

CREATE TABLE IF NOT EXISTS Funcionario (
    id                  INT           NOT NULL,
    nome                VARCHAR(100)  NOT NULL,
    telemovel           VARCHAR(20)   NOT NULL,
    email               VARCHAR(150)  NOT NULL,
    data_nascimento     VARCHAR(30)   NOT NULL,
    NISS                VARCHAR(20)   NOT NULL,
    NIF                 VARCHAR(20)   NOT NULL,
    NUS                 VARCHAR(20)   NOT NULL,
    IBAN                VARCHAR(34)   NOT NULL,
    salario_hora        FLOAT         NOT NULL,
    salario_liquido     FLOAT         NOT NULL,
    salario_bruto       FLOAT         NOT NULL,
    horas_extra         INT           NOT NULL DEFAULT 0,
    numero_porta        VARCHAR(20)   NOT NULL,
    rua                 VARCHAR(255)  NOT NULL,
    localidade          VARCHAR(100)  NOT NULL,
    codigo_postal       VARCHAR(20)   NOT NULL,
    PRIMARY KEY (id)
);

-- =========================================================
-- SClientes
-- =========================================================

CREATE TABLE IF NOT EXISTS Cliente (
    id        INT          NOT NULL,
    nome      VARCHAR(100) NOT NULL,
    email     VARCHAR(150),
    telemovel VARCHAR(20),
    NIF       VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Trotinete (
    id          INT          NOT NULL,
    modelo      VARCHAR(100),
    marca       VARCHAR(100),
    num_serie   INT,
    tipo_motor  VARCHAR(50),
    cod_cliente INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cod_cliente) REFERENCES Cliente(id)
);

-- =========================================================
-- SReparacoes
-- =========================================================

CREATE TABLE IF NOT EXISTS Reparacao (
    id           INT           NOT NULL,
    nomenclatura VARCHAR(150)  NOT NULL,
    descricao    TEXT,
    preco        FLOAT         NOT NULL,
    disponivel   BOOLEAN       NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

-- =========================================================
-- SNotificacoes
-- =========================================================

CREATE TABLE IF NOT EXISTS Notificacao (
    id                  INT      NOT NULL,
    descricao           TEXT,
    data_emissao        DATETIME NOT NULL,
    id_remetente        INT      NOT NULL,
    id_destinatario     INT      NOT NULL,
    notificacao_tratada BOOLEAN  NOT NULL DEFAULT FALSE,
    data_horaTratada    DATETIME NULL,
    PRIMARY KEY (id)
);

-- =========================================================
-- SStock
-- =========================================================

CREATE TABLE IF NOT EXISTS Fornecedor (
    id        INT          NOT NULL,
    nome      VARCHAR(100) NOT NULL,
    telemovel VARCHAR(20),
    email     VARCHAR(150),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Peca (
    id            INT          NOT NULL,
    referencia    VARCHAR(100) NOT NULL,
    stock_minimo  INT          NOT NULL DEFAULT 0,
    preco_venda   INT          NOT NULL,
    codFornecedor INT          NOT NULL,
    ativa         BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (codFornecedor) REFERENCES Fornecedor(id)
);

-- Stock + StockComGarantia partilham tabela: nr_serie e garantia ficam NULL
-- para itens sem garantia. O DAO devolve a subclasse certa em runtime.
CREATE TABLE IF NOT EXISTS Stock (
    id           INT          NOT NULL,
    preco_compra FLOAT        NOT NULL,
    codPeca      INT          NOT NULL,
    data_chegada DATETIME     NOT NULL,
    quantidade   INT          NOT NULL,
    nr_serie     VARCHAR(100) NULL,
    garantia     DATE         NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codPeca) REFERENCES Peca(id)
);

CREATE TABLE IF NOT EXISTS Devolucao (
    id       INT          NOT NULL,
    data     DATETIME     NOT NULL,
    motivo   VARCHAR(255),
    estado   ENUM('PendenteDevolucao', 'Devolvida', 'Invalida') NOT NULL,
    codStock INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codStock) REFERENCES Stock(id)
);

-- =========================================================
-- SFinanceiro
-- Class table inheritance (modelo normalizado): 1 tabela base com
-- os campos comuns + 1 tabela por subclasse com a sua FK específica.
-- A FK do `id` em cada filha aponta para a base com ON DELETE CASCADE,
-- por isso apagar na base remove automaticamente a linha filha.
-- O DAO instancia a subclasse certa em runtime baseado em `tipo`:
--   Salario          -> MovimentoFuncionario
--   GastoPecas       -> MovimentoPeca
--   LucroVendaPecas  -> MovimentoPeca
--   LucroMaoObra     -> MovimentoReparacao
-- =========================================================

CREATE TABLE IF NOT EXISTS MovimentoFinanceiro (
    id        INT          NOT NULL,
    valor     FLOAT        NOT NULL,
    data      DATETIME     NOT NULL,
    descricao VARCHAR(255),
    tipo      ENUM('Salario', 'GastoPecas', 'LucroMaoObra', 'LucroVendaPecas') NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS MovimentoFuncionario (
    id             INT NOT NULL,
    codFuncionario INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)             REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codFuncionario) REFERENCES Funcionario(id)
);

CREATE TABLE IF NOT EXISTS MovimentoReparacao (
    id           INT NOT NULL,
    codReparacao INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)           REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codReparacao) REFERENCES Reparacao(id)
);

CREATE TABLE IF NOT EXISTS MovimentoPeca (
    id      INT NOT NULL,
    codPeca INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)      REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codPeca) REFERENCES Peca(id)
);
