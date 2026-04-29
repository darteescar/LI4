package app.ecoRideCD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOconfig {

    public static final String USERNAME = "me";
    public static final String PASSWORD = "Mypass12345678!";
    private static final String DATABASE = "EcoRide";
    private static final String DRIVER = "jdbc:mysql";
    public static final String URL = DRIVER + "://localhost:3306/" + DATABASE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void CreateBD() {
        String[] sqlCreates = {
            "CREATE TABLE IF NOT EXISTS Funcionario ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(150),"
                + "telemovel VARCHAR(15),"
                + "email VARCHAR(150),"
                + "data_nascimento VARCHAR(20),"
                + "NISS VARCHAR(20),"
                + "NIF VARCHAR(20),"
                + "NUS VARCHAR(50),"
                + "IBAN VARCHAR(40),"
                + "salario_hora DECIMAL(10,2),"
                + "salario_bruto DECIMAL(10,2),"
                + "salario_liquido DECIMAL(10,2),"
                + "horas_extra INT DEFAULT 0,"
                + "numero_porta VARCHAR(20),"
                + "rua VARCHAR(200),"
                + "localidade VARCHAR(100),"
                + "codigo_postal VARCHAR(10)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Utilizador ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "password VARCHAR(255),"
                + "idFuncionario_FK INT,"
                + "cargo VARCHAR(20),"
                + "FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Notificacao ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "descricao TEXT,"
                + "data_emissao DATETIME,"
                + "id_remetente INT,"
                + "id_destinatario INT,"
                + "tratada BOOLEAN DEFAULT FALSE,"
                + "data_horaTratada DATETIME"
                + ")",

            "CREATE TABLE IF NOT EXISTS Cliente ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(150),"
                + "email VARCHAR(150),"
                + "telemovel VARCHAR(15),"
                + "NIF VARCHAR(20)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Trotinete ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "modelo VARCHAR(100),"
                + "marca VARCHAR(100),"
                + "num_serie VARCHAR(100),"
                + "tipo_motor VARCHAR(100),"
                + "codCliente_FK INT,"
                + "FOREIGN KEY (codCliente_FK) REFERENCES Cliente(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Reparacao ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nomenclatura VARCHAR(100),"
                + "descricao TEXT,"
                + "preco DECIMAL(10,2),"
                + "disponivel BOOLEAN DEFAULT TRUE"
                + ")",

            "CREATE TABLE IF NOT EXISTS Fornecedor ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(150),"
                + "email VARCHAR(150),"
                + "telemovel VARCHAR(15)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Peca ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "referencia VARCHAR(100),"
                + "stock_minimo INT DEFAULT 0,"
                + "preco_venda DECIMAL(10,2),"
                + "codFornecedor_FK INT,"
                + "disponivel BOOLEAN DEFAULT TRUE,"
                + "ativa BOOLEAN DEFAULT TRUE,"
                + "quantidade INT DEFAULT 0,"
                + "FOREIGN KEY (codFornecedor_FK) REFERENCES Fornecedor(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Stock ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "preco_compra DECIMAL(10,2),"
                + "codPeca_FK INT,"
                + "data_chegada DATETIME,"
                + "estado VARCHAR(40),"
                + "FOREIGN KEY (codPeca_FK) REFERENCES Peca(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS StockComGarantia ("
                + "idStock_FK INT PRIMARY KEY,"
                + "nr_serie VARCHAR(100),"
                + "garantia INT,"
                + "FOREIGN KEY (idStock_FK) REFERENCES Stock(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Encomenda ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "codFornecedor_FK INT,"
                + "data_envio DATETIME,"
                + "data_rececao DATETIME,"
                + "estado VARCHAR(20),"
                + "FOREIGN KEY (codFornecedor_FK) REFERENCES Fornecedor(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS EncomendaStock ("
                + "idEncomenda_FK INT,"
                + "idStock_FK INT,"
                + "PRIMARY KEY (idEncomenda_FK, idStock_FK),"
                + "FOREIGN KEY (idEncomenda_FK) REFERENCES Encomenda(id),"
                + "FOREIGN KEY (idStock_FK) REFERENCES Stock(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Devolucao ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "data DATETIME,"
                + "motivo TEXT,"
                + "codStock_FK INT,"
                + "estado VARCHAR(30),"
                + "FOREIGN KEY (codStock_FK) REFERENCES Stock(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS MovimentoFinanceiro ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "valor DECIMAL(10,2),"
                + "data DATETIME,"
                + "descricao TEXT,"
                + "codEntidade INT,"
                + "tipo VARCHAR(30)"
                + ")",

            "CREATE TABLE IF NOT EXISTS OrdemServico ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "descricao TEXT,"
                + "codCliente_FK INT,"
                + "codTrotinete_FK INT,"
                + "data_criacao DATETIME,"
                + "codResponsavel_FK INT,"
                + "estado VARCHAR(40),"
                + "FOREIGN KEY (codCliente_FK) REFERENCES Cliente(id),"
                + "FOREIGN KEY (codTrotinete_FK) REFERENCES Trotinete(id),"
                + "FOREIGN KEY (codResponsavel_FK) REFERENCES Funcionario(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS AcessorioOS ("
                + "idOS_FK INT,"
                + "acessorio VARCHAR(255),"
                + "PRIMARY KEY (idOS_FK, acessorio),"
                + "FOREIGN KEY (idOS_FK) REFERENCES OrdemServico(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Diagnostico ("
                + "idOS_FK INT PRIMARY KEY,"
                + "descricao TEXT,"
                + "orcamento DECIMAL(10,2),"
                + "codMecanico_FK INT,"
                + "FOREIGN KEY (idOS_FK) REFERENCES OrdemServico(id),"
                + "FOREIGN KEY (codMecanico_FK) REFERENCES Funcionario(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS DiagnosticoReparacao ("
                + "idOS_FK INT,"
                + "idReparacao_FK INT,"
                + "PRIMARY KEY (idOS_FK, idReparacao_FK),"
                + "FOREIGN KEY (idOS_FK) REFERENCES Diagnostico(idOS_FK),"
                + "FOREIGN KEY (idReparacao_FK) REFERENCES Reparacao(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS DiagnosticoPeca ("
                + "idOS_FK INT,"
                + "idPeca_FK INT,"
                + "quantidade INT,"
                + "PRIMARY KEY (idOS_FK, idPeca_FK),"
                + "FOREIGN KEY (idOS_FK) REFERENCES Diagnostico(idOS_FK),"
                + "FOREIGN KEY (idPeca_FK) REFERENCES Peca(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Conserto ("
                + "idOS_FK INT PRIMARY KEY,"
                + "preco_total DECIMAL(10,2),"
                + "codMecanico_FK INT,"
                + "FOREIGN KEY (idOS_FK) REFERENCES OrdemServico(id),"
                + "FOREIGN KEY (codMecanico_FK) REFERENCES Funcionario(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS ConsertoReparacao ("
                + "idOS_FK INT,"
                + "idReparacao_FK INT,"
                + "PRIMARY KEY (idOS_FK, idReparacao_FK),"
                + "FOREIGN KEY (idOS_FK) REFERENCES Conserto(idOS_FK),"
                + "FOREIGN KEY (idReparacao_FK) REFERENCES Reparacao(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS ConsertoStock ("
                + "idOS_FK INT,"
                + "idStock_FK INT,"
                + "PRIMARY KEY (idOS_FK, idStock_FK),"
                + "FOREIGN KEY (idOS_FK) REFERENCES Conserto(idOS_FK),"
                + "FOREIGN KEY (idStock_FK) REFERENCES Stock(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Checklist ("
                + "idOS_FK INT PRIMARY KEY,"
                + "travoes BOOLEAN,"
                + "luzes BOOLEAN,"
                + "pneus BOOLEAN,"
                + "aceleracao BOOLEAN,"
                + "travagem BOOLEAN,"
                + "visor BOOLEAN,"
                + "teste_pratico BOOLEAN,"
                + "FOREIGN KEY (idOS_FK) REFERENCES OrdemServico(id)"
                + ")",

            "CREATE TABLE IF NOT EXISTS Fotografia ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "idOS_FK INT,"
                + "conteudo LONGBLOB,"
                + "formato VARCHAR(20),"
                + "tamanho BIGINT,"
                + "FOREIGN KEY (idOS_FK) REFERENCES OrdemServico(id)"
                + ")"
        };

        try (Connection conn = getConnection();
             Statement stm = conn.createStatement()) {
            for (String sql : sqlCreates) {
                stm.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
