package app.ecoRideLN.sStock;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sStock.DevolucaoDAO;
import app.ecoRideCD.sStock.EncomendaDAO;
import app.ecoRideCD.sStock.FornecedorDAO;
import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideCD.sStock.StockDAO;
import app.ecoRideLN.sFinanceiro.ISFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SStockFacade implements ISStock {

    private static final float LIMIAR_PRECO_GARANTIA = 70.0f;

    private final FornecedorDAO fornecedoresDAO = new FornecedorDAO();
    private final PecaDAO pecasDAO = new PecaDAO();
    private final StockDAO stockDAO = new StockDAO();
    private final EncomendaDAO encomendasDAO = new EncomendaDAO();
    private final DevolucaoDAO devolucoesDAO = new DevolucaoDAO();

    private int idFornecedor = 1;
    private int idPeca = 1;
    private int idStock = 1;
    private int idEncomenda = 1;
    private int idDevolucao = 1;

    private final ISFinanceiro sFinanceiro;

    public SStockFacade(ISFinanceiro sFinanceiro) {
        this.sFinanceiro = sFinanceiro;
    }

    // -------- Fornecedor --------

    @Override
    public Fornecedor registarFornecedor(String nome, String email, String telemovel) {
        Validacoes.naoVazio(nome, "Nome");
        Validacoes.emailValido(email);
        Validacoes.telemovel(telemovel);

        Fornecedor f = new Fornecedor(idFornecedor++, nome, email, telemovel);
        fornecedoresDAO.put(f.getId(), f);
        return f;
    }

    @Override
    public Optional<Fornecedor> obterDadosFornecedor(int id) {
        return fornecedoresDAO.obterPorId(id);
    }

    @Override
    public boolean existeFornecedor(int id) {
        return fornecedoresDAO.containsKey(id);
    }

    @Override
    public void removerFornecedor(int id) {
        if (!existeFornecedor(id))
            throw new EcoRideException("Fornecedor não encontrado.");
        fornecedoresDAO.remove(id);
    }

    private Fornecedor obterFornecedorOuFalhar(int id) {
        return fornecedoresDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Fornecedor não encontrado."));
    }

    @Override
    public void atualizarNomeFornecedor(int id, String nome) {
        Validacoes.naoVazio(nome, "Nome");
        Fornecedor f = obterFornecedorOuFalhar(id);
        f.setNome(nome);
        fornecedoresDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarTelemovelFornecedor(int id, String telemovel) {
        Validacoes.telemovel(telemovel);
        Fornecedor f = obterFornecedorOuFalhar(id);
        f.setTelemovel(telemovel);
        fornecedoresDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarEmailFornecedor(int id, String email) {
        Validacoes.emailValido(email);
        Fornecedor f = obterFornecedorOuFalhar(id);
        f.setEmail(email);
        fornecedoresDAO.put(f.getId(), f);
    }

    // -------- Peca --------

    @Override
    public Peca registarPeca(String referencia, int stock_minimo, float preco_venda, int idFornecedor) {
        Validacoes.naoVazio(referencia, "Referência");
        Validacoes.inteiroNaoNegativo(stock_minimo, "Stock mínimo");
        Validacoes.valorMonetario(preco_venda, "Preço de venda");

        if (!existeFornecedor(idFornecedor))
            throw new EcoRideException("Fornecedor não encontrado.");

        if (pecasDAO.obterPorReferencia(referencia).isPresent())
            throw new EcoRideException("Já existe peça com esta referência.");

        Peca p = new Peca(idPeca++, referencia, stock_minimo, preco_venda, idFornecedor);
        pecasDAO.put(p.getId(), p);
        return p;
    }

    @Override
    public Optional<Peca> obterDadosPeca(int id) {
        return pecasDAO.obterPorId(id);
    }

    @Override
    public boolean existePecaPorId(int id) {
        return pecasDAO.containsKey(id);
    }

    @Override
    public Optional<Peca> existePecaPorReferencia(String referencia) {
        return pecasDAO.obterPorReferencia(referencia);
    }

    @Override
    public void removerPeca(int id) {
        Peca p = pecasDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
        p.setAtiva(false);
        pecasDAO.put(p.getId(), p);
    }

    private Peca obterPecaOuFalhar(int id) {
        return pecasDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
    }

    @Override
    public void atualizarReferenciaPeca(int id, String referencia) {
        Validacoes.naoVazio(referencia, "Referência");
        Peca p = obterPecaOuFalhar(id);
        if (pecasDAO.obterPorReferencia(referencia).filter(o -> o.getId() != id).isPresent())
            throw new EcoRideException("Já existe peça com esta referência.");
        p.setReferencia(referencia);
        pecasDAO.put(p.getId(), p);
    }

    @Override
    public void atualizarStockMinimoPeca(int id, int stock_minimo) {
        Validacoes.inteiroNaoNegativo(stock_minimo, "Stock mínimo");
        Peca p = obterPecaOuFalhar(id);
        p.setStock_minimo(stock_minimo);
        pecasDAO.put(p.getId(), p);
    }

    @Override
    public void atualizarPrecoVendaPeca(int id, float preco_venda) {
        Validacoes.valorMonetario(preco_venda, "Preço de venda");
        Peca p = obterPecaOuFalhar(id);
        p.setPreco_venda(preco_venda);
        pecasDAO.put(p.getId(), p);
    }

    @Override
    public void atualizarIdFornecedorPeca(int id, int idFornecedor) {
        if (!existeFornecedor(idFornecedor))
            throw new EcoRideException("Fornecedor não encontrado.");
        Peca p = obterPecaOuFalhar(id);
        p.setCodFornecedor(idFornecedor);
        pecasDAO.put(p.getId(), p);
    }

    @Override
    public void atualizarFlagDisponibilidadePeca(int id, boolean novaFlag) {
        Peca p = obterPecaOuFalhar(id);
        p.setDisponivel(novaFlag);
        pecasDAO.put(p.getId(), p);
    }

    // -------- Stock --------

    @Override
    public Stock registarStockPecaNormal(int idPeca, float preco_compra, LocalDateTime data) {
        Validacoes.valorMonetario(preco_compra, "Preço de compra");
        Validacoes.naoNulo(data, "Data de chegada");
        if (!existePecaPorId(idPeca))
            throw new EcoRideException("Peça não encontrada.");
        if (preco_compra > LIMIAR_PRECO_GARANTIA)
            throw new EcoRideException("Stock com preço de compra superior a 70€ requer nº de série e garantia.");

        Stock s = new Stock(idStock++, preco_compra, idPeca, data, null, null);
        stockDAO.put(s.getId(), s);
        return s;
    }

    @Override
    public Stock registarStockPecaSuperior70(int idPeca, float preco_compra, LocalDateTime data,
                                              String nr_serie, int garantia) {
        Validacoes.valorMonetario(preco_compra, "Preço de compra");
        Validacoes.naoNulo(data, "Data de chegada");
        Validacoes.naoVazio(nr_serie, "Número de série");
        Validacoes.inteiroPositivo(garantia, "Garantia");
        if (!existePecaPorId(idPeca))
            throw new EcoRideException("Peça não encontrada.");
        if (preco_compra <= LIMIAR_PRECO_GARANTIA)
            throw new EcoRideException("Use o registo normal para preços inferiores ou iguais a 70€.");

        Stock s = new Stock(idStock++, preco_compra, idPeca, data, nr_serie, garantia);
        stockDAO.put(s.getId(), s);
        return s;
    }

    @Override
    public Optional<Stock> obterDadosStock(int id) {
        return stockDAO.obterPorId(id);
    }

    @Override
    public boolean existeStock(int id) {
        return stockDAO.containsKey(id);
    }

    @Override
    public void removerStock(int id) {
        if (!existeStock(id))
            throw new EcoRideException("Entrada de stock não encontrada.");
        stockDAO.remove(id);
    }

    private Stock obterStockOuFalhar(int id) {
        return stockDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Entrada de stock não encontrada."));
    }

    @Override
    public void atualizaEstadoStock(int id, EstadoStock estado) {
        Validacoes.naoNulo(estado, "Estado");
        Stock s = obterStockOuFalhar(id);
        s.setEstado(estado);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public long obterQuantidadeStockPorPecaId(int idPeca) {
        return stockDAO.obterPorPecaEEstado(idPeca, EstadoStock.EM_STOCK).size();
    }

    @Override
    public long obterQuantidadeStockPorPecaReferencia(String referencia) {
        return pecasDAO.obterPorReferencia(referencia)
                .map(p -> obterQuantidadeStockPorPecaId(p.getId()))
                .orElse(0L);
    }

    @Override
    public void registarDefeitoEntradaStock(int idStock) {
        Stock s = obterStockOuFalhar(idStock);
        s.setEstado(EstadoStock.POSSIVEL_DEFEITO);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public long pecasDefeituosasStock(int idPeca) {
        return stockDAO.quantidadePorPecaEEstado(idPeca, EstadoStock.POSSIVEL_DEFEITO);
    }

    @Override
    public void atualizarPrecoCompraStock(int id, float preco_compra) {
        Validacoes.valorMonetario(preco_compra, "Preço de compra");
        Stock s = obterStockOuFalhar(id);
        s.setPreco_compra(preco_compra);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public void atualizarDataRececaoStock(int id, LocalDateTime data_chegada) {
        Validacoes.naoNulo(data_chegada, "Data de chegada");
        Stock s = obterStockOuFalhar(id);
        s.setData_chegada(data_chegada);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public void atualizarGarantiaStock(int id, int garantia) {
        Validacoes.inteiroPositivo(garantia, "Garantia");
        Stock s = obterStockOuFalhar(id);
        s.setGarantia(garantia);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public void atualizarNrSerieStock(int id, String nr_serie) {
        Validacoes.naoVazio(nr_serie, "Número de série");
        Stock s = obterStockOuFalhar(id);
        s.setNr_serie(nr_serie);
        stockDAO.put(s.getId(), s);
    }

    // -------- Devolucao --------

    @Override
    public Devolucao criarDevolucao(String motivo, int idStock) {
        Validacoes.naoVazio(motivo, "Motivo");
        Stock s = obterStockOuFalhar(idStock);
        if (s.getEstado() != EstadoStock.POSSIVEL_DEFEITO)
            throw new EcoRideException("Apenas stock em POSSIVEL_DEFEITO pode ser devolvido.");

        Devolucao d = new Devolucao(idDevolucao++, LocalDateTime.now(), motivo, idStock);
        devolucoesDAO.put(d.getId(), d);
        s.setEstado(EstadoStock.PENDENTE_DEVOLUCAO);
        stockDAO.put(s.getId(), s);
        return d;
    }

    @Override
    public void devolverPecas(int idDevolucao, int quantidade) {
        Validacoes.inteiroPositivo(quantidade, "Quantidade");
        Devolucao d = devolucoesDAO.obterPorId(idDevolucao)
                .orElseThrow(() -> new EcoRideException("Devolução não encontrada."));
        if (d.getEstado() != EstadoDevolucao.PENDENTE_DEVOLUCAO)
            throw new EcoRideException("Devolução não está pendente.");

        Stock s = obterStockOuFalhar(d.getCodStock());
        long defeituosas = stockDAO.quantidadePorPecaEEstado(s.getCodPeca(), EstadoStock.POSSIVEL_DEFEITO)
                + stockDAO.quantidadePorPecaEEstado(s.getCodPeca(), EstadoStock.PENDENTE_DEVOLUCAO);
        if (quantidade > defeituosas)
            throw new EcoRideException("Quantidade devolvida excede a quantidade defeituosa em stock.");

        d.setEstado(EstadoDevolucao.DEVOLVIDA);
        devolucoesDAO.put(d.getId(), d);
        s.setEstado(EstadoStock.DEVOLVIDA_AO_FORNECEDOR);
        stockDAO.put(s.getId(), s);
    }

    @Override
    public Optional<Devolucao> obterDadosDevolucao(int id) {
        return devolucoesDAO.obterPorId(id);
    }

    @Override
    public boolean existeDevolucao(int id) {
        return devolucoesDAO.containsKey(id);
    }

    @Override
    public void removerDevolucao(int id) {
        Devolucao d = devolucoesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Devolução não encontrada."));

        if (d.getEstado() == EstadoDevolucao.PENDENTE_DEVOLUCAO) {
            Stock s = obterStockOuFalhar(d.getCodStock());
            Peca p = obterPecaOuFalhar(s.getCodPeca());
            sFinanceiro.aumentarGastoPecasDoMes(LocalDateTime.now(), p.getPreco_venda(),
                    "Reposição de gasto com peça pela remoção de devolução pendente",
                    s.getId(), "Stock");
            s.setEstado(EstadoStock.INVALIDA_PARA_DEVOLUCAO);
            stockDAO.put(s.getId(), s);
        }
        devolucoesDAO.remove(id);
    }

    private Devolucao obterDevolucaoOuFalhar(int id) {
        return devolucoesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Devolução não encontrada."));
    }

    @Override
    public void atualizarDataDevolucao(int id, LocalDateTime data) {
        Validacoes.naoNulo(data, "Data");
        Devolucao d = obterDevolucaoOuFalhar(id);
        d.setData(data);
        devolucoesDAO.put(d.getId(), d);
    }

    @Override
    public void atualizarMotivoDevolucao(int id, String motivo) {
        Validacoes.naoVazio(motivo, "Motivo");
        Devolucao d = obterDevolucaoOuFalhar(id);
        d.setMotivo(motivo);
        devolucoesDAO.put(d.getId(), d);
    }

    @Override
    public void atualizarCodStockDevolucao(int id, int codStock) {
        if (!existeStock(codStock))
            throw new EcoRideException("Entrada de stock não encontrada.");
        Devolucao d = obterDevolucaoOuFalhar(id);
        d.setCodStock(codStock);
        devolucoesDAO.put(d.getId(), d);
    }

    @Override
    public void atualizarEstadoDevolucao(int id, EstadoDevolucao estado) {
        Validacoes.naoNulo(estado, "Estado");
        Devolucao d = obterDevolucaoOuFalhar(id);
        d.setEstado(estado);
        devolucoesDAO.put(d.getId(), d);
    }

    @Override
    public boolean validaEstadoDevolucao_PendenteDevolucao(int id) {
        return obterDevolucaoOuFalhar(id).getEstado() == EstadoDevolucao.PENDENTE_DEVOLUCAO;
    }

    // -------- Encomenda --------

    @Override
    public List<Peca> obterPecasBaixoStockMinimo() {
        return pecasDAO.values().stream()
                .filter(Peca::isAtiva)
                .filter(p -> obterQuantidadeStockPorPecaId(p.getId()) < p.getStock_minimo())
                .collect(Collectors.toList());
    }

    @Override
    public int quantidadeAEncomendarPeca(int idPeca) {
        Peca p = obterPecaOuFalhar(idPeca);
        long emStock = obterQuantidadeStockPorPecaId(idPeca);
        return Math.max(0, (int) (p.getStock_minimo() - emStock));
    }

    @Override
    public Encomenda criarEncomenda(int idFornecedor) {
        if (!existeFornecedor(idFornecedor))
            throw new EcoRideException("Fornecedor não encontrado.");
        Encomenda e = new Encomenda(idEncomenda++, idFornecedor);
        encomendasDAO.put(e.getId(), e);
        return e;
    }

    @Override
    public Optional<Encomenda> obterDadosEncomenda(int id) {
        return encomendasDAO.obterPorId(id);
    }

    @Override
    public void adicionarPecasEncomendaStock(int idEncomenda, List<QuantidadePeca> pecas, float preco_compra) {
        Encomenda e = encomendasDAO.obterPorId(idEncomenda)
                .orElseThrow(() -> new EcoRideException("Encomenda não encontrada."));
        if (e.getEstado() != EstadoEncomenda.RECEBIDA)
            throw new EcoRideException("Só é possível adicionar peças ao stock após a receção.");

        Validacoes.valorMonetario(preco_compra, "Preço de compra");
        Validacoes.naoNulo(pecas, "Lista de peças");
        for (QuantidadePeca qp : pecas) {
            Peca p = obterPecaOuFalhar(qp.getCodPeca());
            for (int i = 0; i < qp.getQuantidade(); i++) {
                Stock s;
                if (preco_compra > LIMIAR_PRECO_GARANTIA) {
                    s = new Stock(idStock++, preco_compra, p.getId(), LocalDateTime.now(), null, null);
                } else {
                    s = new Stock(idStock++, preco_compra, p.getId(), LocalDateTime.now(), null, null);
                }
                stockDAO.put(s.getId(), s);
                e.getCodEntradasStock().add(s.getId());
            }
        }
        encomendasDAO.put(e.getId(), e);
    }

    @Override
    public void removerEncomenda(int id) {
        if (!encomendasDAO.containsKey(id))
            throw new EcoRideException("Encomenda não encontrada.");
        encomendasDAO.remove(id);
    }

    private Encomenda obterEncomendaOuFalhar(int id) {
        return encomendasDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Encomenda não encontrada."));
    }

    @Override
    public void atualizarDataRececaoEncomenda(int id, LocalDateTime data) {
        Validacoes.naoNulo(data, "Data de receção");
        Encomenda e = obterEncomendaOuFalhar(id);
        e.setData_rececao(data);
        encomendasDAO.put(e.getId(), e);
    }

    @Override
    public void atualizarDataEnvioEncomenda(int id, LocalDateTime data) {
        Validacoes.naoNulo(data, "Data de envio");
        Encomenda e = obterEncomendaOuFalhar(id);
        e.setData_envio(data);
        encomendasDAO.put(e.getId(), e);
    }

    @Override
    public void atualizarEstadoEncomenda(int id, EstadoEncomenda estado) {
        Validacoes.naoNulo(estado, "Estado");
        Encomenda e = obterEncomendaOuFalhar(id);
        e.setEstado(estado);
        encomendasDAO.put(e.getId(), e);
    }

    @Override
    public boolean validaEncomendaRascunho(int id) {
        return obterEncomendaOuFalhar(id).getEstado() == EstadoEncomenda.RASCUNHO;
    }

    // -------- helpers para outros subsistemas --------

    public List<Stock> stocksDisponiveisParaPeca(int idPeca) {
        return stockDAO.obterPorPecaEEstado(idPeca, EstadoStock.EM_STOCK);
    }

    public Optional<Stock> obterStockInterno(int id) {
        return stockDAO.obterPorId(id);
    }

    public void marcarUsadoEmConserto(int idStock) {
        Stock s = obterStockOuFalhar(idStock);
        if (s.getEstado() != EstadoStock.EM_STOCK)
            throw new EcoRideException("Stock não está disponível para conserto.");
        s.setEstado(EstadoStock.USADA_EM_CONSERTO);
        stockDAO.put(s.getId(), s);
    }

    public Optional<Peca> obterPecaInterna(int idPeca) {
        return pecasDAO.obterPorId(idPeca);
    }
}
