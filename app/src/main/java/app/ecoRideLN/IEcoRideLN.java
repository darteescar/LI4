package app.ecoRideLN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sNotificacoes.NotificacaoOS;
import app.ecoRideLN.sNotificacoes.NotificacaoStock;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sOrdensServico.PecasOrcamento;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;

public interface IEcoRideLN {

     // ------------------- Notificações -------------------

     public NotificacaoOS    registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os);
     public NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca);
     public Notificacao      obterNotificacao(int id);
     public boolean          removerNotificacao(int id);
     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);
     public boolean sinalizarNotificacao_comoTratada(int id);
     public boolean sinalizarNotificacao_comoLida(int id);

     // ------------------- Ordens de Serviço -------------------

     public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador);
     public OrdemServico obterOS(int id);
     public boolean removerOS(int id);
     public void cancelarOS(int id);
     public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Reparacao> reparacoes, String descricao);
     public void registarConsertoOS(int id_OS, List<Stock> pecas, List<Reparacao> reparacoes);
     public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento);
     public List<OrdemServico> obterOSs_Cliente(int id);
     public List<OrdemServico> obterOS_Trotinete(int id_trotinete);
     public List<Conserto> obterConsertosAnteriores(int id_trotinete);
     boolean clienteTemApenasUmPagamentoPendente(int id);


     // ------------------- Clientes -------------------

     public Cliente registarCliente(String nome, String email, String telemovel, String nif);
     public Cliente obterCliente(int id);
     public boolean existeCliente(int id);
     public boolean removerCliente(int id);
     public List<Cliente> obterClientes();

     // ------------------- Trotinetes -------------------

     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);
     public Trotinete obterTrotinete(int id);
     public boolean   existeTrotinete(int id);
     public boolean   removerTrotinete(int id);

     // ------------------- Reparações -------------------

     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco);
     public Reparacao obterReparacao(int id);
     public boolean   existeReparacao(int id);
     public boolean   removerReparacao(int id);
     public List<Reparacao> obterReparacoes();

     // ------------------- Peças -------------------

     public Peca    registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor);
     public Peca    obterPeca(int id);
     public boolean existePeca_id(int id);
     public boolean existePeca_ref(String ref);
     public boolean removerPeca(int id);

     // ------------------- Stock -------------------

     public Stock   registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data, LocalDate garantia, String nr_serie);
     public Stock   registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade);
     public Stock   obterStock(int id);
     public boolean existeStock(int id);
     public boolean removerStock(int id);

     // ------------------- Devoluções -------------------

     public Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock);
     public Devolucao obterDevolucao(int id);
     public boolean   existeDevolucao(int id);
     public boolean   removerDevolucao(int id);
     public void marcarDevolucaoComoEnviada(int id);
     public void marcarDevolucaoComoDevolvida(int id);
     public void marcarDevolucaoComoInvalida(int id);

     // ------------------- Encomendas -------------------

     public Encomenda criarEncomenda(List<Stock> pecas, int cod_fornecedor);
     public Encomenda obterEncomenda(int id);
     public boolean   removerEncomenda(int id);
     public List<Encomenda> obterTodasEncomendas();
     public void marcarEncomendaComoEnviada(int id);
     public void marcarEncomendaComoRecebida(int id);

     // ------------------- Fornecedores -------------------

     public Fornecedor registarFornecedor(String nome, String telemovel, String email);
     public Fornecedor obterFornecedor(int id);
     public boolean    existeFornecedor(int id);
     public boolean    removerFornecedor(int id);

     // ------------------- Funcionários -------------------

     public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);
     public Funcionario obterFuncionario(int id);
     public boolean     existeFuncionario(int id);
     public boolean     removerFuncionario(int id);
     public void        registarHorasExtraFuncionario(int id_funcionario, int horas_extra);
     public void        registarPagamentoFuncionario(int id_funcionario);

     // ------------------- Movimentos Financeiros -------------------

     public MovimentoFinanceiro obterMovimentoFinanceiro(int id);
     public List<MovimentoFinanceiro> obterMovimentosFinanceiros();
     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo);
}
