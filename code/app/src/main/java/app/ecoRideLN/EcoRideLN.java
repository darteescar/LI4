package app.ecoRideLN;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.ISAutenticacao;
import app.ecoRideLN.sAutenticacao.SAutenticacaoFacade;
import app.ecoRideLN.sAutenticacao.Utilizador;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.ISClientes;
import app.ecoRideLN.sClientes.SClientesFacade;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFinanceiro.AnaliseFinanceira;
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
import app.ecoRideLN.sOrdensServico.SOrdensServicoFacade;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import app.ecoRideLN.sStock.Defeito;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.EstadoStock;
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

    // ------------------- Autenticação -------------------

    @Override
    public Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador) {
        return sAutenticacao.registarUtilizador(password, idFuncionario, cargo, identificador);
    }

    @Override
    public boolean autenticar(int idUtilizador, String password) {
        return sAutenticacao.autenticar(idUtilizador, password);
    }

    @Override
    public Cargo obterCargoUtilizador(int idUtilizador) {
        return sAutenticacao.obterCargoUtilizador(idUtilizador);
    }

    @Override
    public int obterIdFuncionario_Utilizador(int idUtilizador) {
        return sAutenticacao.obterUtilizador(idUtilizador).getIdFuncionario();
    }

    @Override
    public boolean atualizarPalavraPasseUtilizador(int idUtilizador, String passwordvelha, String novaPassword) {
        return sAutenticacao.atualizarPalavraPasseUtilizador(idUtilizador, passwordvelha, novaPassword);
    }

    // ------------------- Notificações -------------------
    // feito

    @Override
    public NotificacaoOS registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os) {
        return sNotificacoes.registarNotificacaoOS(descricao, id_remetente, id_destinatario, id_os);
    }

    @Override
    public NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca) {
        return sNotificacoes.registarNotificacaoStock(descricao, id_remetente, id_destinatario, id_peca);
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
    public List<OrdemServico> obterOSs() {
        return sOrdensServico.obterOSs();
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
    public void registarConsertoOS(int id_OS, List<Integer> stockIds, List<Reparacao> reparacoes) {
        float orcamento = 0;
        for (int stockId : stockIds) {
            Stock s = sStock.obterStock(stockId);
            if (s != null) {
                Peca p = sStock.obterPeca(s.getCodPeca());
                if (p != null) orcamento += s.getQuantidade() * p.getPreco_venda();
                sStock.atualizaEstadoStock(stockId, EstadoStock.StockUsadoConserto);
            }
        }
        List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).collect(java.util.stream.Collectors.toList());
        for (Reparacao r : reparacoes) orcamento += r.getPreco();
        sOrdensServico.registarConsertoOS(id_OS, stockIds, codReps, orcamento);
    }

    @Override
    public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        sOrdensServico.registarPagamentoOS(id_OS, metodo_pagamento);
    }

    @Override
    public boolean clienteTemApenasUmPagamentoPendente(int id) {
        long count = sOrdensServico.obterOSs().stream()
            .filter(os -> os.getCodCliente() == id && os.getEstado() == app.ecoRideLN.sOrdensServico.EstadoOS.PendentePagamento)
            .count();
        return count <= 1;
    }

    @Override
    public List<OrdemServico> obterOSs_Cliente(int id) {
        return sOrdensServico.obterOSs().stream()
            .filter(os -> os.getCodCliente() == id)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<OrdemServico> obterOS_Trotinete(int id_trotinete) {
        return sOrdensServico.obterOSs().stream()
            .filter(os -> os.getCodTrotinete() == id_trotinete)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Conserto> obterConsertosAnteriores(int id_trotinete) {
        return sOrdensServico.obterOSs().stream()
            .filter(os -> os.getCodTrotinete() == id_trotinete && os.getConserto() != null)
            .map(OrdemServico::getConserto)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void aprovarOrcamentoOS(int id){
        sOrdensServico.aprovarOrcamentoOS(id);
    }

    @Override
    public void rejeitarOrcamentoOS(int id){
        sOrdensServico.rejeitarOrcamentoOS(id);
    }

    @Override
    public void atribuirOS(int id, int id_funcionario){
        sOrdensServico.atribuirOS(id, id_funcionario);
    }

    @Override
    public void registarNotificacaoPagamentoOS(int id_OS){
        sOrdensServico.registarNotificacaoPagamentoOS(id_OS);
    }

    // ------------------- Clientes -------------------

    @Override
    public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
        return sClientes.registarCliente(nome, email, telemovel, nif);
    }

    @Override
    public Cliente atualizarCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif){
        return sClientes.atualizarCliente(id_cliente, novo_nome, novo_email, novo_telemovel, novo_nif);
    }

    @Override
    public Cliente obterCliente(int id) {
        return sClientes.obterCliente(id);
    }

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
    public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, String num_serie, String tipo_motor) {
        return sClientes.registarTrotinete(id_cliente, modelo, marca, num_serie, tipo_motor);
    }

    @Override
    public Trotinete atualizarTrotinete(int id, int id_cliente, String modelo, String marca, String num_serie, String tipo_motor) {
        return sClientes.atualizarTrotinete(id, id_cliente, modelo, marca, num_serie, tipo_motor);
    }

    @Override
    public Trotinete obterTrotinete(int id) {
        return sClientes.obterTrotinete(id);
    }

    public boolean existeTrotinete(int id) {
        return sClientes.existeTrotinete(id);
    }

    @Override
    public boolean removerTrotinete(int id) {
        return sClientes.removerTrotinete(id);
    }

    @Override
    public List<Trotinete> obterTrotinetes() {
        return sClientes.obterTrotinetes();
    }

    // ------------------- Reparações -------------------

    @Override
    public Reparacao registarReparacao(String nomenclatura, String descricao, float preco, boolean disponivel) {
        return sReparacoes.registarReparacao(nomenclatura, descricao, preco, disponivel);
    }

    @Override
    public Reparacao atualizarReparacao(int id, String novaNomenclatura, String novaDescricao, float novoPreco, boolean novaDisponibilidade) {
        return sReparacoes.atualizarReparacao(id, novaNomenclatura, novaDescricao, novoPreco, novaDisponibilidade);
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

    @Override
    public List<Reparacao> obterReparacoesDisponiveis() {
        return sReparacoes.obterReparacoesDisponiveis();
    }

    // ------------------- Peças -------------------

    @Override
    public Peca registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor) {
        return sStock.registarPeca(ref, nome, descricao, stock_minimo, preco_venda, id_fornecedor);
    }

    @Override
    public Peca atualizarPeca(int id, String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa){
        return sStock.atualizarPeca(id, ref, nome, descricao, stock_minimo, preco_venda, id_fornecedor, ativa);
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

    @Override
    public List<Peca> obterPecas() {
        return sStock.obterPecas();
    }

    // ------------------- Stock -------------------

    @Override
    public Stock registarStockComGarantia(int id_peca, float preco_compra, LocalDate data, int garantia, String nr_serie) {
        sFinanceiro.registarMovimentoCompraStock(id_peca, preco_compra, "Compra "+sStock.obterPeca(id_peca).getNome() +"x1");
        return sStock.registarStockComGarantia(id_peca, preco_compra, data, garantia, nr_serie);
    }

    @Override
    public Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDate data, int quantidade) {
        sFinanceiro.registarMovimentoCompraStock(id_peca, preco_compra * quantidade, "Compra "+sStock.obterPeca(id_peca).getNome() +"x"+ quantidade);
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

    @Override
    public List<Stock> obterStocks() {
        return sStock.obterStocks();
    }

    @Override
    public Stock atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade) {
        return sStock.atualizarStock(id_stock, preco_compra, cod_Peca, data_rececao, quantidade);
    }

    @Override
    public Stock atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade, int garantia, String nr_serie) {
        return sStock.atualizarStockComGarantia(id_stock, preco_compra, cod_Peca, data_rececao, quantidade, garantia, nr_serie);
    }

    // ------------------- Defeitos -------------------

    @Override
    public List<Defeito> registarDefeito(List<Integer> stockIds, String motivo, int idFuncionario) {
        return sStock.registarDefeito(stockIds, motivo, idFuncionario);
    }

    @Override
    public Defeito obterDefeito(int id) { return sStock.obterDefeito(id); }

    @Override
    public List<Defeito> obterDefeitos() { return sStock.obterDefeitos(); }

    @Override
    public boolean removerDefeito(int id) { return sStock.removerDefeito(id); }

    @Override
    public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) {
        return sStock.confirmarDefeitoComDevolucao(idDefeito, motivo, data);
    }

    @Override
    public void descartarDefeito(int idDefeito) { sStock.descartarDefeito(idDefeito); }

    // ------------------- Devoluções -------------------

    @Override
    public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data) {
        return sStock.registarDevolucao(stockIds, motivo, data);
    }

    @Override
    public Devolucao obterDevolucao(int id) { return sStock.obterDevolucao(id); }

    @Override
    public boolean existeDevolucao(int id) { return sStock.existeDevolucao(id); }

    @Override
    public boolean removerDevolucao(int id) { return sStock.removerDevolucao(id); }

    @Override
    public void marcarDevolucaoComoEnviada(int id) { sStock.marcarDevolucaoComoEnviada(id); }

    @Override
    public void marcarDevolucaoComoDevolvida(int id) {
        Devolucao dev = sStock.obterDevolucao(id);
        sStock.marcarDevolucaoComoDevolvida(id);
        if (dev != null) {
            Stock s = sStock.obterStock(dev.getCodStock());
            if (s != null) {
                Peca p = sStock.obterPeca(s.getCodPeca());
                float valor = s.getPreco_compra() * s.getQuantidade();
                String nome = p != null ? p.getNome() : String.valueOf(s.getCodPeca());
                sFinanceiro.registarMovimentoCompraStock(s.getCodPeca(), valor, "Reembolso Peça " + nome + "x" + s.getQuantidade());
            }
        }
    }

    @Override
    public void marcarDevolucaoComoInvalida(int id) { sStock.marcarDevolucaoComoInvalida(id); }

    // ------------------- Encomendas -------------------

    @Override
    public Encomenda registarEncomenda(List<Integer> id_peca, List<Float> preco_compra, List<Integer> quantidade, int cod_fornecedor) {
        return sStock.registarEncomenda(id_peca, preco_compra, quantidade, cod_fornecedor);
    }

    @Override
    public boolean removerEncomenda(int id) { return sStock.removerEncomenda(id); }

    @Override
    public List<Encomenda> obterEncomendas() { return sStock.obterEncomendas(); }

    @Override
    public Encomenda marcarEncomendaComoEnviada(int id) { return sStock.marcarEncomendaComoEnviada(id); }

    @Override
    public Encomenda marcarEncomendaComoRecebida(int id, List<String> numeros_serie, List<Integer> garantias) {
        List<Integer> stockIds = sStock.marcarEncomendaComoRecebida(id, numeros_serie, garantias).getCodStocks();
        for (int stockId : stockIds) {
            Stock s = sStock.obterStock(stockId);
            if (s != null) {
                int codPeca = s.getCodPeca();
                sFinanceiro.registarMovimentoCompraStock(codPeca, s.calcularValorTotal(), "Compra "+sStock.obterPeca(codPeca).getNome() +"x"+ s.getQuantidade());
            }
        }
        return sStock.marcarEncomendaComoRecebida(id, numeros_serie, garantias);
    }

    @Override
    public Map<Integer, Encomenda> gerarListaAutomatica() { return sStock.gerarListaAutomatica(); }

    // ------------------- Fornecedores -------------------
    // feito

    @Override
    public Fornecedor registarFornecedor(String nome, String telemovel, String email) {
        return sStock.registarFornecedor(nome, telemovel, email);
    }
    
    @Override
    public Fornecedor atualizarFornecedor(int id, String nome, String telemovel, String email){
        return sStock.atualizarFornecedor(id, nome, telemovel, email);
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

    @Override
    public List<Fornecedor> obterFornecedores(){
        return sStock.obterFornecedores();
    }

    // ------------------- Funcionários -------------------
    // feito

    @Override
    public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal) {
        return sFuncionarios.registarFuncionario(nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal);
    }
    
    @Override
    public Funcionario atualizarFuncionario(int id, String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal){
        return sFuncionarios.atualizarFuncionario(id, nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal);
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
    public List<Funcionario> obterFuncionarios(){
        return sFuncionarios.obterFuncionarios();
    }

    @Override
    public void registarHorasExtraFuncionario(int id_funcionario, int horas_extra) {
        sFuncionarios.adicionarHorasExtra(id_funcionario, horas_extra);
    }

    @Override
    public boolean registarPagamentoFuncionario(int id_funcionario) {
        float resultado = sFuncionarios.registarPagamentoFuncionario(id_funcionario);
        sFinanceiro.registarMovimentoPagamentoFuncionario(id_funcionario, resultado,"Pagamento "+ sFuncionarios.obterFuncionario(id_funcionario).getNome());
        return true;
    }

    // ------------------- Movimentos Financeiros -------------------
    // feito

    @Override
    public List<MovimentoFinanceiro> obterMovimentosFinanceiros() {
        return sFinanceiro.obterMovimentos();
    }

    @Override
    public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo) {
        return sFinanceiro.obterMovimentosFinanceirosFiltrados(desde, ate, tipo);
    }

    @Override
    public AnaliseFinanceira calcularAnaliseFinanceira(List<MovimentoFinanceiro> movimentos){
        return sFinanceiro.calcularAnaliseFinanceira(movimentos);
    }
}
