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
