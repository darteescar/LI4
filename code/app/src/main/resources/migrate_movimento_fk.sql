-- Migração: ajusta FK de MovimentoFuncionario e MovimentoReparacao
-- Executa uma vez na BD existente:
--   docker exec -i $(docker ps -qf name=mysql) mysql -uecoride -pEcoRide123! EcoRide < migrate_movimento_fk.sql

-- MovimentoFuncionario: coluna passa a NULL, FK passa a SET NULL
ALTER TABLE MovimentoFuncionario
    MODIFY COLUMN codFuncionario INT NULL;

ALTER TABLE MovimentoFuncionario
    DROP FOREIGN KEY IF EXISTS movimentofuncionario_ibfk_2;

ALTER TABLE MovimentoFuncionario
    ADD CONSTRAINT mf_funcionario_fk
    FOREIGN KEY (codFuncionario) REFERENCES Funcionario(id) ON DELETE SET NULL;

-- MovimentoReparacao: coluna passa a NULL, FK passa a SET NULL
ALTER TABLE MovimentoReparacao
    MODIFY COLUMN codReparacao INT NULL;

ALTER TABLE MovimentoReparacao
    DROP FOREIGN KEY IF EXISTS movimentoreparacao_ibfk_2;

ALTER TABLE MovimentoReparacao
    ADD CONSTRAINT mr_reparacao_fk
    FOREIGN KEY (codReparacao) REFERENCES Reparacao(id) ON DELETE SET NULL;
