package app.ecoRideLN;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFinanceiro.AnaliseFinanceira;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Diagnostico;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Defeito;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;

public interface IEcoRideLN {

     // ------------------- Autenticação -------------------
     // feito

     public Utilizador        registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador);
     public Utilizador        atualizarUtilizador(int id, int idFuncionario, Cargo cargo, String identificador);
     public List<Utilizador>  obterUtilizadores();
     public boolean           removerUtilizador(int idUtilizador);
     public boolean           autenticar(int idUtilizador, String password);
     public boolean           atualizarPalavraPasseUtilizador(int idUtilizador, String passwordvelha, String novaPassword);
     public Cargo             obterCargoUtilizador(int idUtilizador);
     public int               obterIdFuncionario_Utilizador(int idUtilizador);

     // ------------------- Notificações -------------------
     // feito

     public boolean           removerNotificacao(int id);
     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);
     public boolean           sinalizarNotificacao_comoTratada(int id);
     public boolean           sinalizarNotificacao_comoLida(int id);

     // ------------------- Ordens de Serviço -------------------

     public OrdemServico            registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador);
     public OrdemServico            obterOS(int id);
     public boolean                 removerOS(int id);
     public List<OrdemServico>      obterOSs();
     public void                    cancelarOS(int id);
     public Diagnostico             registarDiagnosticoOS(int idOS, Map<Integer, Integer> pecasQuantidades, List<Reparacao> reparacoes, String descricao, int id_funcionario);
     public Conserto                registarConsertoOS(int id_OS, Map<Integer, Integer> pecaQuantidades, List<Reparacao> reparacoes, int id_funcionario, CheckList checklist);

     public List<Defeito>      reportarDefeitoFungivelConsertoOS(int idOS, int codPeca, String motivo, int idFuncionario);
     public List<Defeito>      reportarDefeitoSerializadoConsertoOS(int idOS, List<Integer> codStocks, String motivo, int idFuncionario);
     public boolean            registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento);
     public List<OrdemServico> obterOSs_Cliente(int id);
     public List<OrdemServico> obterOSs_Trotinete(int id_trotinete);
     public boolean            aprovarOrcamentoOS(int id);
     public boolean            rejeitarOrcamentoOS(int id);
     public boolean            atribuirOS(int id, int id_funcionario);
     public boolean            registarNotificacaoPagamentoOS(int id_OS);

     // ------------------- Clientes -------------------
     // feito
     public Cliente       registarCliente(String nome, String email, String telemovel, String nif);
     public Cliente       atualizarCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);
     public Cliente       obterCliente(int id);
     public boolean       removerCliente(int id);
     public List<Cliente> obterClientes();

     // ------------------- Trotinetes -------------------
     // feito
     public Trotinete       registarTrotinete(int id_cliente, String modelo, String marca, String num_serie, String tipo_motor);
     public Trotinete       atualizarTrotinete(int id, int id_cliente, String modelo, String marca, String num_serie, String tipo_motor);
     public Trotinete       obterTrotinete(int id);
     public boolean         removerTrotinete(int id);
     public List<Trotinete> obterTrotinetes();

     // ------------------- Reparações -------------------
     // feito
     public Reparacao       registarReparacao(String nomenclatura, String descricao, float preco, boolean disponivel);
     public Reparacao       atualizarReparacao(int id, String novaNomenclatura, String novaDescricao, float novoPreco, boolean novaDisponibilidade);
     public Reparacao       obterReparacao(int id);
     public boolean         existeReparacao(int id);
     public boolean         removerReparacao(int id);
     public List<Reparacao> obterReparacoes();
     public List<Reparacao> obterReparacoesDisponiveis();

     // ------------------- Peças -------------------
     // feito
     public Peca       registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor);
     public Peca       atualizarPeca(int id, String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa);
     public Peca       obterPeca(int id);
     public boolean    existePeca_id(int id);
     public boolean    existePeca_ref(String ref);
     public boolean    removerPeca(int id);
     public List<Peca> obterPecas();

     // ------------------- Stock -------------------

     public Stock        registarStockComGarantia(int id_peca, float preco_compra, LocalDate data, int garantia, String nr_serie);
     public Stock        registarStock_PecaNormal(int id_peca, float preco_compra, LocalDate data, int quantidade);
     public Stock        obterStock(int id);
     public boolean      existeStock(int id);
     public boolean      removerStock(int id);
     public List<Stock>  obterStocks();
     public Stock        atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade);
     public Stock        atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade, int garantia, String nr_serie);

     // ------------------- Defeitos -------------------

     public List<Defeito> obterDefeitos();
     public boolean       removerDefeito(int id);
     public Devolucao     confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data);
     public void          descartarDefeito(int idDefeito);

     // ------------------- Devoluções -------------------

     public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data);
     public List<Devolucao> obterDevolucoes();
     public boolean         removerDevolucao(int id);
     public void            marcarDevolucaoComoEnviada(int id);
     public void            marcarDevolucaoComoDevolvida(int id);
     public void            marcarDevolucaoComoInvalida(int id);

     // ------------------- Encomendas -------------------
     // feito

     public Encomenda                   registarEncomenda(List<Integer> id_peca, List<Float> preco_compra, List<Integer> quantidade, int cod_fornecedor);
     public boolean                     removerEncomenda(int id);
     public List<Encomenda>             obterEncomendas();
     public Encomenda                   marcarEncomendaComoEnviada(int id);
     public Encomenda                   marcarEncomendaComoRecebida(int id, List<String> numeros_serie, List<Integer> garantias);
     public Map<Integer, Encomenda>     gerarListaAutomatica();

     // ------------------- Fornecedores -------------------
     // feito
     public Fornecedor       registarFornecedor(String nome, String telemovel, String email);
     public Fornecedor       atualizarFornecedor(int id, String nome, String telemovel, String email);
     public Fornecedor       obterFornecedor(int id);
     public boolean          existeFornecedor(int id);
     public boolean          removerFornecedor(int id);
     public List<Fornecedor> obterFornecedores();

     // ------------------- Funcionários -------------------
     // feito
     public Funcionario       registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);
     public Funcionario       atualizarFuncionario(int id, String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);
     public Funcionario       obterFuncionario(int id);
     public boolean           existeFuncionario(int id);
     public boolean           removerFuncionario(int id);
     public List<Funcionario> obterFuncionarios();
     public void              registarHorasExtraFuncionario(int id_funcionario, int horas_extra);
     public boolean           registarPagamentoFuncionario(int id_funcionario);

     // ------------------- Movimentos Financeiros -------------------
     // feito
     public List<MovimentoFinanceiro> obterMovimentosFinanceiros();
     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo);
     public AnaliseFinanceira         calcularAnaliseFinanceira(List<MovimentoFinanceiro> movimentos);

}
