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

-- Encomenda + tabela de junção para a lista de codEntradasStock.
-- ordem preserva a posição na List<Integer>; ON DELETE CASCADE limpa
-- as entradas se a encomenda for apagada.
CREATE TABLE IF NOT EXISTS Encomenda (
    id            INT      NOT NULL,
    codFornecedor INT      NOT NULL,
    data_rececao  DATETIME NULL,
    data_envio    DATETIME NULL,
    estado        ENUM('RASCUNHO', 'ENVIADA', 'RECEBIDA') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codFornecedor) REFERENCES Fornecedor(id)
);

CREATE TABLE IF NOT EXISTS Encomenda_EntradaStock (
    idEncomenda INT NOT NULL,
    ordem       INT NOT NULL,
    codStock    INT NOT NULL,
    PRIMARY KEY (idEncomenda, ordem),
    FOREIGN KEY (idEncomenda) REFERENCES Encomenda(id) ON DELETE CASCADE,
    FOREIGN KEY (codStock)    REFERENCES Stock(id)
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

-- =========================================================
-- SOrdensServico
-- A OS guarda os campos comuns. Cada coleção (acessorios, fotografias,
-- pecasOrcamento, pecasUsadas, cod_reparacoes do diagnostico/conserto)
-- vive em tabela própria com FK para a OS (ou para o Diagnostico/Conserto)
-- e ON DELETE CASCADE para que apagar a OS limpe tudo o que pendura.
-- Diagnostico e Conserto têm cardinalidade 0..1 com a OS, por isso
-- usam idOS como PK e FK simultaneamente.
-- =========================================================

CREATE TABLE IF NOT EXISTS OrdemServico (
    id             INT      NOT NULL,
    descricao      TEXT,
    data_criacao   DATETIME NOT NULL,
    codTrotinete   INT      NOT NULL,
    codCliente     INT      NOT NULL,
    codResponsavel INT      NOT NULL,
    estado         ENUM('PendenteReparacao', 'PendenteDiagnostico',
                        'PendenteAprovacaoOrcamento', 'PendentePagamento',
                        'Paga', 'OrcamentoNaoAprovado', 'AguardarPecas',
                        'Eliminada') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codTrotinete)   REFERENCES Trotinete(id),
    FOREIGN KEY (codCliente)     REFERENCES Cliente(id),
    FOREIGN KEY (codResponsavel) REFERENCES Funcionario(id)
);

CREATE TABLE IF NOT EXISTS OrdemServico_Acessorio (
    idOS  INT          NOT NULL,
    ordem INT          NOT NULL,
    valor VARCHAR(255) NOT NULL,
    PRIMARY KEY (idOS, ordem),
    FOREIGN KEY (idOS) REFERENCES OrdemServico(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Fotografia (
    id       INT          NOT NULL AUTO_INCREMENT,
    idOS     INT          NOT NULL,
    conteudo MEDIUMBLOB,
    formato  VARCHAR(20),
    tamanho  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (idOS) REFERENCES OrdemServico(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Diagnostico (
    idOS        INT   NOT NULL,
    descricao   TEXT,
    orcamento   FLOAT NOT NULL,
    codMecanico INT   NOT NULL,
    PRIMARY KEY (idOS),
    FOREIGN KEY (idOS)        REFERENCES OrdemServico(id) ON DELETE CASCADE,
    FOREIGN KEY (codMecanico) REFERENCES Funcionario(id)
);

CREATE TABLE IF NOT EXISTS Diagnostico_PecaOrcamento (
    idOS       INT NOT NULL,
    codPeca    INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (idOS, codPeca),
    FOREIGN KEY (idOS)    REFERENCES Diagnostico(idOS) ON DELETE CASCADE,
    FOREIGN KEY (codPeca) REFERENCES Peca(id)
);

CREATE TABLE IF NOT EXISTS Diagnostico_Reparacao (
    idOS         INT NOT NULL,
    codReparacao INT NOT NULL,
    PRIMARY KEY (idOS, codReparacao),
    FOREIGN KEY (idOS)         REFERENCES Diagnostico(idOS) ON DELETE CASCADE,
    FOREIGN KEY (codReparacao) REFERENCES Reparacao(id)
);

CREATE TABLE IF NOT EXISTS Conserto (
    idOS              INT     NOT NULL,
    preco_total       FLOAT   NOT NULL,
    codMecanico       INT     NOT NULL,
    chk_luzes         BOOLEAN NOT NULL DEFAULT FALSE,
    chk_pneus         BOOLEAN NOT NULL DEFAULT FALSE,
    chk_aceleracao    BOOLEAN NOT NULL DEFAULT FALSE,
    chk_travagem      BOOLEAN NOT NULL DEFAULT FALSE,
    chk_visor         BOOLEAN NOT NULL DEFAULT FALSE,
    chk_teste_pratico BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (idOS),
    FOREIGN KEY (idOS)        REFERENCES OrdemServico(id) ON DELETE CASCADE,
    FOREIGN KEY (codMecanico) REFERENCES Funcionario(id)
);

CREATE TABLE IF NOT EXISTS Conserto_PecaUsada (
    idOS       INT NOT NULL,
    codStock   INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (idOS, codStock),
    FOREIGN KEY (idOS)     REFERENCES Conserto(idOS) ON DELETE CASCADE,
    FOREIGN KEY (codStock) REFERENCES Stock(id)
);

CREATE TABLE IF NOT EXISTS Conserto_Reparacao (
    idOS         INT NOT NULL,
    codReparacao INT NOT NULL,
    PRIMARY KEY (idOS, codReparacao),
    FOREIGN KEY (idOS)         REFERENCES Conserto(idOS) ON DELETE CASCADE,
    FOREIGN KEY (codReparacao) REFERENCES Reparacao(id)
);

