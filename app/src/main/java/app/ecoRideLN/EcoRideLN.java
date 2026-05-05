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
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.ISOrdensServico;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sOrdensServico.PecasOrcamento;
import app.ecoRideLN.sOrdensServico.SOrdensServicoFacade;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
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
    public NotificacaoOS criarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os) {
        return sNotificacoes.criarNotificacaoOS(descricao, id_remetente, id_destinatario, id_os);
    }

    @Override
    public NotificacaoStock criarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca) {
        return sNotificacoes.criarNotificacaoStock(descricao, id_remetente, id_destinatario, id_peca);
    }

    @Override
    public Notificacao obterNotificacao(int id) {
        return sNotificacoes.obterNotificacao(id);
    }

    @Override
    public boolean removerNotificacao(int id) {
        return sNotificacoes.removerNotificacao(id);
    }

    // ------------------- Ordens de Serviço -------------------
    @Override
    public OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias) {
        return sOrdensServico.registarOS(codResponsavel, id_cliente, id_trotinete, descricao, acessorios, fotografias);
    }

    @Override
    public OrdemServico obterOS(int id) {
        return sOrdensServico.obterOS(id);
    }

    @Override
    public boolean removerOS(int id) {
        return sOrdensServico.removerOS(id);
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
    public Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock) {
        return sStock.criarDevolucao(data_devolucao, motivo, id_stock);
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

    // ------------------- Encomendas -------------------
    @Override
    public Encomenda criarEncomenda(List<Stock> pecas, int cod_fornecedor) {
        return sStock.criarEncomenda(pecas, cod_fornecedor);
    }

    @Override
    public Encomenda obterEncomenda(int id) {
        return sStock.obterEncomenda(id);
    }

    @Override
    public boolean removerEncomenda(int id) {
        return sStock.removerEncomenda(id);
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

    // ------------------- Movimentos Financeiros -------------------
    @Override
    public MovimentoFinanceiro criarMovimentoFuncionario(float valor, String descricao, int codFuncionario) {
        return sFinanceiro.criarMovimentoFuncionario(valor, descricao, codFuncionario);
    }

    @Override
    public MovimentoFinanceiro criarMovimentoReparacao(float valor, String descricao, int codReparacao) {
        return sFinanceiro.criarMovimentoReparacao(valor, descricao, codReparacao);
    }

    @Override
    public MovimentoFinanceiro criarMovimentoPeca(float valor, String descricao, TipoMovimento tipo, int codPeca) {
        return sFinanceiro.criarMovimentoPeca(valor, descricao, tipo, codPeca);
    }

    @Override
    public MovimentoFinanceiro obterMovimentoFinanceiro(int id) {
        return sFinanceiro.obterMovimentoFinanceiro(id);
    }

    @Override
    public boolean existeMovimentoFinanceiro(int id) {
        return sFinanceiro.existeMovimentoFinanceiro(id);
    }

    @Override
    public boolean removerMovimentoFinanceiro(int id) {
        return sFinanceiro.removerMovimentoFinanceiro(id);
    }

    // ------------------- Cross-cutting -------------------
    @Override
    public boolean pecasDiagnosticoDisponiveisReparacao(int id_OS) {
        List<PecasOrcamento> pecas = sOrdensServico.obterPecasQuantidadeDiagnosticoOS(id_OS);
        for (PecasOrcamento po : pecas) {
            if (sStock.obter_quantidade_Stock_Peca_id(po.getCodPeca()) < po.getQuantidade()) {
                return false;
            }
        }
        return true;
    }

}
