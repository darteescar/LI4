package app.ecoRideLN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideLN.sAutenticacao.ISAutenticacao;
import app.ecoRideLN.sAutenticacao.SAutenticacaoFacade;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.ISClientes;
import app.ecoRideLN.sClientes.SClientesFacade;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFinanceiro.ISFinanceiro;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.SFinanceiroFacade;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sFuncionarios.ISFuncionarios;
import app.ecoRideLN.sFuncionarios.SFuncionariosFacade;
import app.ecoRideLN.sNotificacoes.ISNotificacoes;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sNotificacoes.NotificacaoOS;
import app.ecoRideLN.sNotificacoes.NotificacaoStock;
import app.ecoRideLN.sNotificacoes.SNotificacoesFacade;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.ISOrdensServico;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sOrdensServico.PecasOrcamento;
import app.ecoRideLN.sOrdensServico.PecasUsadas;
import app.ecoRideLN.sOrdensServico.SOrdensServicoFacade;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.ItemEncomenda;
import app.ecoRideLN.sStock.ISStock;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.SStockFacade;
import app.ecoRideLN.sStock.Stock;

public class EcoRideLN implements IEcoRideLN {

    private final ISNotificacoes sNotificacoes;
    private final ISAutenticacao sAutenticacao;
    private final ISClientes sClientes;
    private final ISFinanceiro sFinanceiro;
    private final ISFuncionarios sFuncionarios;
    private final ISOrdensServico sOrdensServico;
    private final ISStock sStock;
    private final ISReparacoes sReparacoes;

    public EcoRideLN() {
        this.sNotificacoes = new SNotificacoesFacade();
        this.sAutenticacao = new SAutenticacaoFacade();
        this.sClientes = new SClientesFacade();
        this.sFinanceiro = new SFinanceiroFacade();
        this.sFuncionarios = new SFuncionariosFacade();
        this.sOrdensServico = new SOrdensServicoFacade();
        this.sStock = new SStockFacade();
        this.sReparacoes = new SReparacoesFacade();
    }

    // ------------------- Notificações -------------------

    @Override
    public NotificacaoOS registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os) {
        return sNotificacoes.registarNotificacaoOS(descricao, id_remetente, id_destinatario, id_os);
    }

    @Override
    public NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca) {
        return sNotificacoes.registarNotificacaoStock(descricao, id_remetente, id_destinatario, id_peca);
    }

    @Override
    public Notificacao obterNotificacao(int id) {
        return sNotificacoes.obterNotificacao(id);
    }

    @Override
    public boolean removerNotificacao(int id) {
        return sNotificacoes.removerNotificacao(id);
    }

    @Override
    public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario) {
        return sNotificacoes.obterNotificacoesPorDestinatario(id_destinatario);
    }

    @Override
    public boolean sinalizarNotificacao_comoTratada(int id) {
        return sNotificacoes.sinalizarNotificacao_comoTratada(id);
    }

    @Override
    public boolean sinalizarNotificacao_comoLida(int id) {
        return sNotificacoes.sinalizarNotificacao_comoLida(id);
    }

    // ------------------- Ordens de Serviço -------------------

    @Override
    public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador) {
        return sOrdensServico.registarOS(id_cliente, id_trotinete, descricao, acessorios, fotografias, codCriador);
    }

    @Override
    public OrdemServico obterOS(int id) {
        return sOrdensServico.obterOS(id);
    }

    @Override
    public boolean removerOS(int id) {
        return sOrdensServico.removerOS(id);
    }

    @Override
    public void cancelarOS(int id) {
        sOrdensServico.eliminarOS(id);
    }

    @Override
    public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Reparacao> reparacoes, String descricao) {
        List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).collect(java.util.stream.Collectors.toList());
        float orcamento = 0;
        for (Reparacao r : reparacoes) orcamento += r.getPreco();
        for (PecasOrcamento po : listPecas) {
            Peca p = sStock.obterPeca(po.getCodPeca());
            if (p != null) orcamento += po.getQuantidade() * p.getPreco_venda();
        }
        sOrdensServico.registarDiagnosticoOS(idOS, listPecas, codReps, orcamento, descricao);
    }

    @Override
    public void registarConsertoOS(int id_OS, List<Stock> pecas, List<Reparacao> reparacoes) {
        List<PecasUsadas> pecasUsadas = pecas.stream()
            .map(s -> new PecasUsadas(s.getQuantidade(), s.getId()))
            .collect(java.util.stream.Collectors.toList());
        List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).collect(java.util.stream.Collectors.toList());
        float orcamento = 0;
        for (Reparacao r : reparacoes) orcamento += r.getPreco();
        for (Stock s : pecas) {
            Peca p = sStock.obterPeca(s.getCodPeca());
            if (p != null) orcamento += s.getQuantidade() * p.getPreco_venda();
        }
        sOrdensServico.registarConsertoOS(id_OS, pecasUsadas, codReps, orcamento);
    }

    @Override
    public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        sOrdensServico.registarPagamentoOS(id_OS, metodo_pagamento);
    }

    @Override
    public boolean clienteTemApenasUmPagamentoPendente(int id) {
        long count = sOrdensServico.obterTodasOSs().stream()
            .filter(os -> os.getCodCliente() == id && os.getEstado() == app.ecoRideLN.sOrdensServico.EstadoOS.PendentePagamento)
            .count();
        return count <= 1;
    }

    @Override
    public List<OrdemServico> obterOSs_Cliente(int id) {
        return sOrdensServico.obterTodasOSs().stream()
            .filter(os -> os.getCodCliente() == id)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<OrdemServico> obterOS_Trotinete(int id_trotinete) {
        return sOrdensServico.obterTodasOSs().stream()
            .filter(os -> os.getCodTrotinete() == id_trotinete)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Conserto> obterConsertosAnteriores(int id_trotinete) {
        return sOrdensServico.obterTodasOSs().stream()
            .filter(os -> os.getCodTrotinete() == id_trotinete && os.getConserto() != null)
            .map(OrdemServico::getConserto)
            .collect(java.util.stream.Collectors.toList());
    }

    // ------------------- Clientes -------------------

    @Override
    public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
        return sClientes.registarCliente(nome, email, telemovel, nif);
    }

    @Override
    public Cliente obterCliente(int id) {
        return sClientes.obterCliente(id);
    }

    @Override
    public boolean existeCliente(int id) {
        return sClientes.existeCliente(id);
    }

    @Override
    public boolean removerCliente(int id) {
        return sClientes.removerCliente(id);
    }

    @Override
    public List<Cliente> obterClientes() {
        return sClientes.obterClientes();
    }

    // ------------------- Trotinetes -------------------

    @Override
    public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor) {
        return sClientes.registarTrotinete(id_cliente, modelo, marca, num_serie, tipo_motor);
    }

    @Override
    public Trotinete obterTrotinete(int id) {
        return sClientes.obterTrotinete(id);
    }

    @Override
    public boolean existeTrotinete(int id) {
        return sClientes.existeTrotinete(id);
    }

    @Override
    public boolean removerTrotinete(int id) {
        return sClientes.removerTrotinete(id);
    }

    // ------------------- Reparações -------------------

    @Override
    public Reparacao registarReparacao(String nomenclatura, String descricao, float preco) {
        return sReparacoes.registarReparacao(nomenclatura, descricao, preco);
    }

    @Override
    public Reparacao obterReparacao(int id) {
        return sReparacoes.obterReparacao(id);
    }

    @Override
    public boolean existeReparacao(int id) {
        return sReparacoes.existeReparacao(id);
    }

    @Override
    public boolean removerReparacao(int id) {
        return sReparacoes.removerReparacao(id);
    }

    @Override
    public List<Reparacao> obterReparacoes() {
        return sReparacoes.obterReparacoes();
    }

    // ------------------- Peças -------------------

    @Override
    public Peca registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor) {
        return sStock.registarPeca(ref, nome, descricao, stock_minimo, preco_venda, id_fornecedor);
    }

    @Override
    public Peca obterPeca(int id) {
        return sStock.obterPeca(id);
    }

    @Override
    public boolean existePeca_id(int id) {
        return sStock.existePeca_id(id);
    }

    @Override
    public boolean existePeca_ref(String ref) {
        return sStock.existePeca_ref(ref);
    }

    @Override
    public boolean removerPeca(int id) {
        return sStock.removerPeca(id);
    }

    // ------------------- Stock -------------------

    @Override
    public Stock registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data, LocalDate garantia, String nr_serie) {
        return sStock.registarStockComGarantia(id_peca, preco_compra, data, garantia, nr_serie);
    }

    @Override
    public Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade) {
        return sStock.registarStock_PecaNormal(id_peca, preco_compra, data, quantidade);
    }

    @Override
    public Stock obterStock(int id) {
        return sStock.obterStock(id);
    }

    @Override
    public boolean existeStock(int id) {
        return sStock.existeStock(id);
    }

    @Override
    public boolean removerStock(int id) {
        return sStock.removerStock(id);
    }

    // ------------------- Devoluções -------------------

    @Override
    public Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade) {
        return sStock.criarDevolucao(data_devolucao, motivo, id_stock, quantidade);
    }

    @Override
    public Devolucao obterDevolucao(int id) {
        return sStock.obterDevolucao(id);
    }

    @Override
    public boolean existeDevolucao(int id) {
        return sStock.existeDevolucao(id);
    }

    @Override
    public boolean removerDevolucao(int id) {
        return sStock.removerDevolucao(id);
    }

    @Override
    public void marcarDevolucaoComoEnviada(int id) {
        sStock.marcarDevolucaoComoEnviada(id);
    }

    @Override
    public void marcarDevolucaoComoDevolvida(int id) {
        sStock.marcarDevolucaoComoDevolvida(id);
    }

    @Override
    public void marcarDevolucaoComoInvalida(int id) {
        sStock.marcarDevolucaoComoInvalida(id);
    }

    // ------------------- Encomendas -------------------

    @Override
    public Encomenda criarEncomenda(List<ItemEncomenda> itens, int cod_fornecedor) {
        return sStock.criarEncomenda(itens, cod_fornecedor);
    }

    @Override
    public Encomenda obterEncomenda(int id) {
        return sStock.obterEncomenda(id);
    }

    @Override
    public boolean removerEncomenda(int id) {
        return sStock.removerEncomenda(id);
    }

    @Override
    public List<Encomenda> obterTodasEncomendas() {
        return sStock.obterTodasEncomendas();
    }

    @Override
    public void marcarEncomendaComoEnviada(int id) {
        sStock.marcarEncomendaComoEnviada(id);
    }

    @Override
    public void marcarEncomendaComoRecebida(int id) {
        sStock.marcarEncomendaComoRecebida(id);
    }

    // ------------------- Fornecedores -------------------

    @Override
    public Fornecedor registarFornecedor(String nome, String telemovel, String email) {
        return sStock.registarFornecedor(nome, telemovel, email);
    }

    @Override
    public Fornecedor obterFornecedor(int id) {
        return sStock.obterFornecedor(id);
    }

    @Override
    public boolean existeFornecedor(int id) {
        return sStock.existeFornecedor(id);
    }

    @Override
    public boolean removerFornecedor(int id) {
        return sStock.removerFornecedor(id);
    }

    // ------------------- Funcionários -------------------

    @Override
    public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal) {
        return sFuncionarios.registarFuncionario(nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal);
    }

    @Override
    public Funcionario obterFuncionario(int id) {
        return sFuncionarios.obterFuncionario(id);
    }

    @Override
    public boolean existeFuncionario(int id) {
        return sFuncionarios.existeFuncionario(id);
    }

    @Override
    public boolean removerFuncionario(int id) {
        return sFuncionarios.removerFuncionario(id);
    }

    @Override
    public void registarHorasExtraFuncionario(int id_funcionario, int horas_extra) {
        sFuncionarios.adicionarHorasExtra(id_funcionario, horas_extra);
    }

    @Override
    public void registarPagamentoFuncionario(int id_funcionario) {
        sFuncionarios.registarPagamentoFuncionario(id_funcionario);
    }

    // ------------------- Movimentos Financeiros -------------------

    @Override
    public MovimentoFinanceiro obterMovimentoFinanceiro(int id) {
        return sFinanceiro.obterMovimentoFinanceiro(id);
    }

    @Override
    public List<MovimentoFinanceiro> obterMovimentosFinanceiros() {
        return sFinanceiro.obterMovimentos();
    }

    @Override
    public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo) {
        return sFinanceiro.obterMovimentosFinanceirosFiltrados(desde, ate, tipo);
    }

    // ------------------- Cross-cutting -------------------

    private boolean pecasDiagnosticoDisponiveisReparacao(int id_OS) {
        List<PecasOrcamento> pecas = sOrdensServico.obterPecasDiagnosticoOS(id_OS);
        for (PecasOrcamento po : pecas) {
            if (sStock.obter_quantidade_Stock_Peca_id(po.getCodPeca()) < po.getQuantidade()) {
                return false;
            }
        }
        return true;
    }
}
