package app.ecoRideLN;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.common.EcoRideException;
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
import app.ecoRideLN.sNotificacoes.SNotificacoesFacade;
import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Diagnostico;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.ISOrdensServico;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sOrdensServico.SOrdensServicoFacade;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import app.ecoRideLN.sStock.Defeito;
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

    // ------------------- Autenticação -------------------

    @Override
    public Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador) {
        return sAutenticacao.registarUtilizador(password, idFuncionario, cargo, identificador);
    }

    @Override 
    public Utilizador atualizarUtilizador(int id, int idFuncionario, Cargo cargo, String identificador) {
        return sAutenticacao.atualizarUtilizador(id, idFuncionario, cargo, identificador);
    }

    @Override
    public List<Utilizador>  obterUtilizadores() {
        return sAutenticacao.obterUtilizadores();
    }

    @Override
    public boolean removerUtilizador(int idUtilizador) {
        return sAutenticacao.removerUtilizador(idUtilizador);
    }

    @Override
    public boolean autenticar(int idUtilizador, String password) {
        return sAutenticacao.autenticar(idUtilizador, password);
    }

    @Override
    public Utilizador obterUtilizadorPorIdentificador(String identificador) {
        return sAutenticacao.obterUtilizadorPorIdentificador(identificador);
    }

    @Override
    public boolean atualizarPalavraPasseUtilizador(int idUtilizador, String passwordvelha, String novaPassword) {
        return sAutenticacao.atualizarPalavraPasseUtilizador(idUtilizador, passwordvelha, novaPassword);
    }

    @Override
    public Cargo obterCargoUtilizador(int idUtilizador) {
        return sAutenticacao.obterCargoUtilizador(idUtilizador);
    }

    @Override
    public int obterIdFuncionario_Utilizador(int idUtilizador) {
        return sAutenticacao.obterUtilizador(idUtilizador).getIdFuncionario();
    }

    // ------------------- Notificações -------------------
    // feito

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
    public Diagnostico registarDiagnosticoOS(int idOS, Map<Integer, Integer> pecasQuantidades, List<Reparacao> reparacoes, String descricao, int id_funcionario) {
        List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).collect(java.util.stream.Collectors.toList());
        float orcamento = 0;
        for (Reparacao r : reparacoes) orcamento += r.getPreco();
        for (Map.Entry<Integer, Integer> e : pecasQuantidades.entrySet()) {
            Peca p = sStock.obterPeca(e.getKey());
            if (p != null) orcamento += e.getValue() * p.getPreco_venda();
        }
        List<Integer> destinatarios = sAutenticacao.obterUtilizadoresPorCargo(Cargo.Gerente, Cargo.Secretaria);
        int idUtilRemetente = obterIdUtilizadorPorFuncionario(id_funcionario);
        sNotificacoes.registarNotificacaoOS("Orçamento da OS#" + idOS + " aguarda aprovação do cliente", idUtilRemetente, destinatarios, idOS);
        return sOrdensServico.registarDiagnosticoOS(idOS, pecasQuantidades, codReps, orcamento, descricao, id_funcionario);
    }

    @Override
    public Conserto registarConsertoOS(int id_OS, Map<Integer, Integer> pecaQuantidades, List<Reparacao> reparacoes, int id_funcionario, CheckList checklist) {
        if (!checklist.isCheckListComplete())
            throw new EcoRideException("Checklist incompleta. Conserto não pode ser registado.");

        // FIFO: para cada peça, atribui stocks por ordem de chegada e decrementa quantidade
        Map<Integer, Integer> stocksUsados = new java.util.LinkedHashMap<>();
        float orcamento = 0;
        for (Map.Entry<Integer, Integer> entry : pecaQuantidades.entrySet()) {
            int codPeca = entry.getKey();
            int qtd     = entry.getValue();
            Map<Integer, Integer> atribuidos = sStock.atribuirStocksFIFO(codPeca, qtd);
            stocksUsados.putAll(atribuidos);
            Peca p = sStock.obterPeca(codPeca);
            if (p != null) orcamento += qtd * p.getPreco_venda();
        }

        List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).collect(java.util.stream.Collectors.toList());
        for (Reparacao r : reparacoes) orcamento += r.getPreco();
        List<Integer> destinatarios = sAutenticacao.obterUtilizadoresPorCargo(Cargo.Gerente, Cargo.Secretaria);
        int idUtilRemetente = obterIdUtilizadorPorFuncionario(id_funcionario);
        sNotificacoes.registarNotificacaoOS("Execução da OS#" + id_OS + " concluída", idUtilRemetente, destinatarios, id_OS);
        return sOrdensServico.registarConsertoOS(id_OS, stocksUsados, codReps, orcamento, id_funcionario);
    }

    private int obterIdUtilizadorPorFuncionario(int idFuncionario) {
        return sAutenticacao.obterUtilizadores().stream()
            .filter(u -> u.getIdFuncionario() == idFuncionario)
            .map(Utilizador::getId)
            .findFirst()
            .orElse(0); // 0 → gravado como NULL no DAO (sem remetente)
    }

    @Override
    public List<Defeito> reportarDefeitoFungivelConsertoOS(int idOS, int codPeca, String motivo, int idFuncionario) {
    List<Integer> stocksDaPeca = sOrdensServico.obterStocksUsadosConsertoOS(idOS).entrySet().stream()
        .filter(e -> {
            Stock s = sStock.obterStock(e.getKey());
            return s != null && s.getCodPeca() == codPeca;  // sem instanceof
        })
        .map(Map.Entry::getKey)
        .collect(java.util.stream.Collectors.toList());

    if (stocksDaPeca.isEmpty())
        throw new EcoRideException("Nenhum stock da peça " + codPeca + " na OS " + idOS);

    List<Integer> destinatarios = sAutenticacao.obterUtilizadoresPorCargo(Cargo.Gerente, Cargo.GestorStock);
    String nome = sStock.obterPeca(codPeca) != null ? sStock.obterPeca(codPeca).getNome() : String.valueOf(codPeca);
    sNotificacoes.registarNotificacaoStock("Possível defeito nas Peças " + nome + " da OS " + idOS, idFuncionario, destinatarios, codPeca);
    return sStock.registarDefeito(stocksDaPeca, motivo, idFuncionario);
    }

    @Override
    public boolean registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        OrdemServico os = sOrdensServico.obterOS(id_OS);
        sOrdensServico.registarPagamentoOS(id_OS, metodo_pagamento);
        List<Integer> reparacoes = os.getConserto().getCod_reparacoes();
        List<Integer> stocks = os.getConserto().getCodStocks();
        for (int codRep : reparacoes) {
            Reparacao r = sReparacoes.obterReparacao(codRep);
            if (r != null) sFinanceiro.registarMovimentoReparacaoOS(codRep, r.getPreco(), "Pagamento reparação " + r.getNomenclatura() + " OS#" + id_OS);
        }
        for (int codStock : stocks) {
            Stock s = sStock.obterStock(codStock);
            if (s != null) {
                Peca p = sStock.obterPeca(s.getCodPeca());
                String nome = p != null ? p.getNome() : String.valueOf(s.getCodPeca());
                sFinanceiro.registarMovimentoVendaPeca(codStock, s.calcularValorTotal(), "Pagamento peça " + nome + " OS#" + id_OS);
            }
        }
        return true;
    }

    @Override
    public List<OrdemServico> obterOSs_Cliente(int id) {
        return sOrdensServico.filtrarOSs(null, null, null, id, null);
    }

    @Override
    public List<OrdemServico> obterOSs_Trotinete(int id_trotinete) {
        return sOrdensServico.obterOSs_Trotinete(id_trotinete);
    }

    @Override
    public boolean aprovarOrcamentoOS(int id, int idFuncionario) {
        Cargo cargo = sAutenticacao.obterCargoUtilizador(obterIdUtilizadorPorFuncionario(idFuncionario));
        if (cargo != Cargo.Gerente && cargo != Cargo.Secretaria)
            throw new IllegalArgumentException("Apenas o Gerente ou a Secretaria podem aprovar um orçamento.");
        boolean resultado = sOrdensServico.aprovarOrcamentoOS(id);
        if (resultado) {
            OrdemServico os = sOrdensServico.obterOS(id);
            List<Integer> destinatarios = sAutenticacao.obterUtilizadores().stream()
                .filter(u -> u.getIdFuncionario() == os.getCodMecanico())
                .map(Utilizador::getId)
                .collect(java.util.stream.Collectors.toList());
            if (!destinatarios.isEmpty())
                sNotificacoes.registarNotificacaoOS("Orçamento da OS#" + id + " aprovado. Pode avançar com o conserto.", 0, destinatarios, id);
        }
        return resultado;
    }

    @Override
    public boolean rejeitarOrcamentoOS(int id, int idFuncionario) {
        Cargo cargo = sAutenticacao.obterCargoUtilizador(obterIdUtilizadorPorFuncionario(idFuncionario));
        if (cargo != Cargo.Gerente && cargo != Cargo.Secretaria)
            throw new IllegalArgumentException("Apenas o Gerente ou a Secretaria podem rejeitar um orçamento.");
        boolean resultado = sOrdensServico.rejeitarOrcamentoOS(id);
        if (resultado) {
            OrdemServico os = sOrdensServico.obterOS(id);
            List<Integer> destinatarios = sAutenticacao.obterUtilizadores().stream()
                .filter(u -> u.getIdFuncionario() == os.getCodMecanico())
                .map(Utilizador::getId)
                .collect(java.util.stream.Collectors.toList());
            if (!destinatarios.isEmpty())
                sNotificacoes.registarNotificacaoOS("Orçamento da OS#" + id + " rejeitado pelo cliente.", 0, destinatarios, id);
        }
        return resultado;
    }

    @Override
    public boolean atribuirOS(int id, int id_funcionario){
        return sOrdensServico.atribuirOS(id, id_funcionario);
    }

    @Override
    public boolean registarNotificacaoPagamentoOS(int id_OS, int idFuncionario){
        Cargo cargo = sAutenticacao.obterCargoUtilizador(obterIdUtilizadorPorFuncionario(idFuncionario));
        if (cargo != Cargo.Gerente && cargo != Cargo.Secretaria)
            throw new IllegalArgumentException("Apenas o Gerente ou a Secretaria podem registar notificações de pagamento.");
        return sOrdensServico.registarNotificacaoPagamentoOS(id_OS);
    }

    // ------------------- Clientes -------------------
    // feito

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

    @Override
    public boolean removerCliente(int id) {
        return sClientes.removerCliente(id);
    }

    @Override
    public List<Cliente> obterClientes() {
        return sClientes.obterClientes();
    }

    // ------------------- Trotinetes -------------------
    // feito

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

    @Override
    public boolean removerTrotinete(int id) {
        return sClientes.removerTrotinete(id);
    }

    @Override
    public List<Trotinete> obterTrotinetes() {
        return sClientes.obterTrotinetes();
    }

    // ------------------- Reparações -------------------
    // feito

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
    public Peca registarPeca(String ref, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, int garantia) {
        return sStock.registarPeca(ref, marca, nome, descricao, stock_minimo, preco_venda, id_fornecedor, garantia);
    }

    @Override
    public Peca atualizarPeca(int id, String ref, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa, int garantia) {
        return sStock.atualizarPeca(id, ref, marca, nome, descricao, stock_minimo, preco_venda, id_fornecedor, ativa, garantia);
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

    @Override
    public List<Peca> obterPecasAtivas() {
        return sStock.obterPecasAtivas();
    }

    // ------------------- Stock -------------------

    @Override
    public Stock registarStock(int id_peca, float preco_compra, LocalDate data, int quantidade) {

        Peca p = sStock.obterPeca(id_peca);
        if (!p.isAtiva()) {
            throw new EcoRideException("Peça " + p.getNome() + " está inativa. Stock não pode ser registado.");
        }

        Stock s = sStock.registarStock(id_peca, preco_compra, data, quantidade);
        sFinanceiro.registarMovimentoCompraStock(s.getId(), preco_compra * quantidade, "Compra " + sStock.obterPeca(id_peca).getNome() + "x" + quantidade);
        return s;
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
    public Devolucao obterDevolucao(int id) { return sStock.obterDevolucao(id); }

    @Override
    public boolean existeDevolucao(int id) { return sStock.existeDevolucao(id); }


    @Override
    public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data) {
        return sStock.registarDevolucao(stockIds, motivo, data);
    }

    @Override
    public List<Devolucao> obterDevolucoes() { return sStock.obterDevolucoes(); }

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
                sFinanceiro.registarMovimentoCompraStock(s.getId(), -valor, "Reembolso Peça " + nome + "x" + s.getQuantidade());
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
    public Encomenda marcarEncomendaComoRecebida(int id) {
        Encomenda e = sStock.marcarEncomendaComoRecebida(id);
        for (int stockId : e.getCodStocks()) {
            Stock s = sStock.obterStock(stockId);
            if (s != null) {
                Peca p = sStock.obterPeca(s.getCodPeca());
                String nome = p != null ? p.getNome() : String.valueOf(s.getCodPeca());
                sFinanceiro.registarMovimentoCompraStock(stockId, s.calcularValorTotal(), "Compra " + nome + "x" + s.getQuantidade());
            }
        }
        return e;
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

    @Override
    public List<Peca> obterPecasDoFornecedor(int id_fornecedor) {
        return sStock.obterPecasDoFornecedor(id_fornecedor);
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
        sAutenticacao.obterUtilizadores().stream()
                .filter(u -> u.getIdFuncionario() == id)
                .forEach(u -> sAutenticacao.removerUtilizador(u.getId()));
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
