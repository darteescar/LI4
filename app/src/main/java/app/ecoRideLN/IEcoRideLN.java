package app.ecoRideLN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sNotificacoes.NotificacaoOS;
import app.ecoRideLN.sNotificacoes.NotificacaoStock;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;
import app.ecoRideLN.sFuncionarios.Funcionario;

public interface IEcoRideLN {

     // ------------------- Notificações -------------------

     NotificacaoOS    criarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os);
     NotificacaoStock criarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca);
     Notificacao      obterNotificacao(int id);
     boolean          removerNotificacao(int id);

     // ------------------- Ordens de Serviço -------------------

     OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao);
     OrdemServico registarOS_Extras(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias);
     OrdemServico obterOS(int id);
     boolean      removerOS(int id);

     // ------------------- Clientes -------------------

     Cliente registarCliente(String nome, String email, String telemovel, String nif);
     Cliente obterCliente(int id);
     boolean existeCliente(int id);
     boolean removerCliente(int id);

     // ------------------- Trotinetes -------------------

     Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);
     Trotinete obterTrotinete(int id);
     boolean   existeTrotinete(int id);
     boolean   removerTrotinete(int id);

     // ------------------- Reparações -------------------

     Reparacao registarReparacao(String nomenclatura, String descricao, float preco);
     Reparacao obterReparacao(int id);
     boolean   existeReparacao(int id);
     boolean   removerReparacao(int id);

     // ------------------- Peças -------------------

     Peca    registarPeca(String ref, int stock_minimo, float preco_venda, int id_fornecedor);
     Peca    obterPeca(int id);
     boolean existePeca_id(int id);
     boolean existePeca_ref(String ref);
     boolean removerPeca(int id);

     // ------------------- Stock -------------------

     Stock   registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data, LocalDate garantia, String nr_serie);
     Stock   registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade);
     Stock   obterStock(int id);
     boolean existeStock(int id);
     boolean removerStock(int id);

     // ------------------- Devoluções -------------------

     Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock);
     Devolucao obterDevolucao(int id);
     boolean   existeDevolucao(int id);
     boolean   removerDevolucao(int id);

     // ------------------- Encomendas -------------------

     Encomenda criarEncomenda(List<Stock> pecas, int cod_fornecedor);
     Encomenda obterEncomenda(int id);
     boolean   removerEncomenda(int id);

     // ------------------- Fornecedores -------------------

     Fornecedor registarFornecedor(String nome, String telemovel, String email);
     Fornecedor obterFornecedor(int id);
     boolean    existeFornecedor(int id);
     boolean    removerFornecedor(int id);

     // ------------------- Funcionários -------------------

     Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);
     Funcionario obterFuncionario(int id);
     boolean     existeFuncionario(int id);
     boolean     removerFuncionario(int id);

     // ------------------- Movimentos Financeiros -------------------

     MovimentoFinanceiro criarMovimentoFuncionario(float valor, String descricao, int codFuncionario);
     MovimentoFinanceiro criarMovimentoReparacao(float valor, String descricao, int codReparacao);
     MovimentoFinanceiro criarMovimentoPeca(float valor, String descricao, TipoMovimento tipo, int codPeca);
     MovimentoFinanceiro obterMovimentoFinanceiro(int id);
     boolean             existeMovimentoFinanceiro(int id);
     boolean             removerMovimentoFinanceiro(int id);

     // ------------------- Cross-cutting -------------------

     boolean pecasDiagnosticoDisponiveisReparacao(int id_OS);
}
