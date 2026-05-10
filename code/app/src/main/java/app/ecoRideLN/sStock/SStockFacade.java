package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.common.EcoRideException;
import app.ecoRideCD.sStock.DefeitoDAO;
import app.ecoRideCD.sStock.DevolucaoDAO;
import app.ecoRideCD.sStock.EncomendaDAO;
import app.ecoRideCD.sStock.FornecedorDAO;
import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideCD.sStock.StockDAO;

public class SStockFacade implements ISStock {

    private final FornecedorDAO fornecedorDAO = FornecedorDAO.getInstance();
    private final EncomendaDAO  encomendaDAO  = EncomendaDAO.getInstance();
    private final PecaDAO       pecaDAO       = PecaDAO.getInstance();
    private final DevolucaoDAO  devolucaoDAO  = DevolucaoDAO.getInstance();
    private final StockDAO      stockDAO      = StockDAO.getInstance();
    private final DefeitoDAO    defeitoDAO    = DefeitoDAO.getInstance();

    // ------------------- Fornecedor -------------------

    @Override
    public Fornecedor registarFornecedor(String nome, String telemovel, String email) {
        int id = fornecedorDAO.generateNewId();
        Fornecedor novo = new Fornecedor(id, nome, telemovel, email);
        fornecedorDAO.put(id, novo);
        return novo;
    }

    @Override
    public Fornecedor atualizarFornecedor(int id, String nome, String telemovel, String email) {
        Fornecedor f = fornecedorDAO.get(id);
        if (f != null) {
            if (nome != null && !nome.isEmpty())           f.setNome(nome);
            if (telemovel != null && !telemovel.isEmpty()) f.setTelemovel(telemovel);
            if (email != null && !email.isEmpty())         f.setEmail(email);
            fornecedorDAO.put(id, f);
        }
        return f;
    }

    @Override
    public Fornecedor obterFornecedor(int id) { return fornecedorDAO.get(id); }

    @Override
    public boolean existeFornecedor(int id) { return fornecedorDAO.containsKey(id); }

    @Override
    public boolean removerFornecedor(int id) { return fornecedorDAO.remove(id) != null; }

    @Override
    public List<Fornecedor> obterFornecedores() { return new ArrayList<>(fornecedorDAO.values()); }

    // ------------------- Peca -------------------

    @Override
    public Peca registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor) {
        int id = pecaDAO.generateNewId();
        Peca nova = new Peca(id, ref, nome, descricao, stock_minimo, preco_venda, id_fornecedor, true);
        pecaDAO.put(id, nova);
        return nova;
    }

    @Override
    public Peca atualizarPeca(int id, String referencia, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa) {
        Peca p = pecaDAO.get(id);
        if (p != null) {
            if (referencia != null && !referencia.isEmpty()) p.setReferencia(referencia);
            if (nome != null)        p.setNome(nome);
            if (descricao != null)   p.setDescricao(descricao);
            if (stock_minimo >= 0)   p.setStock_minimo(stock_minimo);
            if (preco_venda >= 0)    p.setPreco_venda(preco_venda);
            if (id_fornecedor >= 0)  p.setCodFornecedor(id_fornecedor);
            p.setAtiva(ativa);
            pecaDAO.put(id, p);
        }
        return p;
    }

    @Override
    public Peca obterPeca(int id) { return pecaDAO.get(id); }

    @Override
    public boolean existePeca_id(int id) { return pecaDAO.containsKey(id); }

    @Override
    public boolean existePeca_ref(String ref) { return pecaDAO.getByReference(ref); }

    @Override
    public boolean removerPeca(int id) { return pecaDAO.remove(id) != null; }

    @Override
    public List<Peca> obterPecas() { return new ArrayList<>(pecaDAO.values()); }

    // ------------------- Stock -------------------

    @Override
    public Stock registarStockComGarantia(int id_peca, float preco_compra, LocalDate data, int garantia, String nr_serie) {
        int id = stockDAO.generateNewId();
        Stock novo = new StockComGarantia(id, preco_compra, id_peca, data, nr_serie, garantia);
        stockDAO.put(id, novo);
        return novo;
    }

    @Override
    public Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDate data, int quantidade) {
        int id = stockDAO.generateNewId();
        Stock novo = new Stock(id, preco_compra, id_peca, data, quantidade);
        stockDAO.put(id, novo);
        return novo;
    }

    @Override
    public Stock atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade) {
        Stock s = stockDAO.get(id_stock);
        if (s != null) {
            if (preco_compra >= 0)    s.setPreco_compra(preco_compra);
            if (cod_Peca >= 0)        s.setCodPeca(cod_Peca);
            if (data_rececao != null) s.setData_chegada(data_rececao);
            if (quantidade >= 0)      s.setQuantidade(quantidade);
            stockDAO.put(id_stock, s);
        }
        return s;
    }

    @Override
    public Stock atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade, int garantia, String nr_serie) {
        Stock s = stockDAO.get(id_stock);
        if (s instanceof StockComGarantia g) {
            if (preco_compra >= 0)                       g.setPreco_compra(preco_compra);
            if (cod_Peca >= 0)                           g.setCodPeca(cod_Peca);
            if (data_rececao != null)                    g.setData_chegada(data_rececao);
            if (quantidade >= 0)                         g.setQuantidade(quantidade);
            if (garantia != 0)                           g.setGarantia(garantia);
            if (nr_serie != null && !nr_serie.isEmpty()) g.setNr_serie(nr_serie);
            stockDAO.put(id_stock, g);
        }
        return s;
    }

    @Override
    public Stock obterStock(int id) { return stockDAO.get(id); }

    @Override
    public boolean existeStock(int id) { return stockDAO.containsKey(id); }

    @Override
    public boolean removerStock(int id) { return stockDAO.remove(id) != null; }

    @Override
    public List<Stock> obterStocks() { return new ArrayList<>(stockDAO.values()); }

    @Override
    public Stock atualizaEstadoStock(int id, EstadoStock estado) {
        Stock s = stockDAO.get(id);
        if (s != null) {
            s.setEstado(estado);
            stockDAO.put(id, s);
        }
        return s;
    }

    @Override
    public int obter_quantidade_Stock_Peca_id(int id) {
        int total = 0;
        for (Stock s : stockDAO.getByPecaId(id)) total += s.getQuantidade();
        return total;
    }

    // ------------------- Defeito -------------------

    @Override
    public List<Defeito> registarDefeito(List<Integer> stockIds, String motivo, int idFuncionario) {
        List<Defeito> resultado = new ArrayList<>();
        for (int stockId : stockIds) {
            Stock s = stockDAO.get(stockId);
            if (s == null)
                throw new EcoRideException("Stock " + stockId + " não encontrado.");
            if (s.getEstado() != EstadoStock.StockEmArmazem)
                throw new EcoRideException("Stock " + stockId + " não está disponível (estado: " + s.getEstado() + ").");
            s.setEstado(EstadoStock.StockComPossivelDefeito);
            stockDAO.put(stockId, s);
            int id = defeitoDAO.generateNewId();
            Defeito novo = new Defeito(id, stockId, motivo, idFuncionario);
            defeitoDAO.put(id, novo);
            resultado.add(novo);
        }
        return resultado;
    }

    @Override
    public Defeito obterDefeito(int id) { return defeitoDAO.get(id); }

    @Override
    public List<Defeito> obterDefeitos() { return new ArrayList<>(defeitoDAO.values()); }

    @Override
    public boolean removerDefeito(int id) { return defeitoDAO.remove(id) != null; }

    @Override
    public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) {
        Defeito d = defeitoDAO.get(idDefeito);
        if (d == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
        Stock s = stockDAO.get(d.getCodStock());
        if (s != null) {
            s.setEstado(EstadoStock.StockPendenteDeDevolucao);
            stockDAO.put(s.getId(), s);
        }
        int idDev = devolucaoDAO.generateNewId();
        Devolucao dev = new Devolucao(idDev, data, motivo, d.getCodStock());
        devolucaoDAO.put(idDev, dev);
        defeitoDAO.remove(idDefeito);
        return dev;
    }

    @Override
    public void descartarDefeito(int idDefeito) {
        Defeito d = defeitoDAO.get(idDefeito);
        if (d == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
        Stock s = stockDAO.get(d.getCodStock());
        if (s != null) {
            s.setEstado(EstadoStock.StockEmArmazem);
            stockDAO.put(s.getId(), s);
        }
        defeitoDAO.remove(idDefeito);
    }

    // ------------------- Devolucao -------------------

    @Override
    public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data) {
        List<Devolucao> resultado = new ArrayList<>();
        for (int stockId : stockIds) {
            Stock s = stockDAO.get(stockId);
            if (s == null)
                throw new EcoRideException("Stock " + stockId + " não encontrado.");
            if (s.getEstado() != EstadoStock.StockEmArmazem)
                throw new EcoRideException("Stock " + stockId + " não está disponível (estado: " + s.getEstado() + ").");
            s.setEstado(EstadoStock.StockPendenteDeDevolucao);
            stockDAO.put(stockId, s);
            int id = devolucaoDAO.generateNewId();
            Devolucao nova = new Devolucao(id, data, motivo, stockId);
            devolucaoDAO.put(id, nova);
            resultado.add(nova);
        }
        return resultado;
    }

    @Override
    public Devolucao obterDevolucao(int id) { return devolucaoDAO.get(id); }

    @Override
    public boolean existeDevolucao(int id) { return devolucaoDAO.containsKey(id); }

    @Override
    public boolean removerDevolucao(int id) { return devolucaoDAO.remove(id) != null; }

    @Override
    public List<Devolucao> obterDevolucoes() { return new ArrayList<>(devolucaoDAO.values()); }

    @Override
    public void marcarDevolucaoComoEnviada(int id) {
        Devolucao d = devolucaoDAO.get(id);
        if (d != null) {
            atualizaEstadoStock(d.getCodStock(), EstadoStock.StockEnviadoParaFornecedor);
            d.setEstado(EstadoDevolucao.Devolvida);
            devolucaoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDevolucaoComoDevolvida(int id) {
        Devolucao d = devolucaoDAO.get(id);
        if (d != null) {
            atualizaEstadoStock(d.getCodStock(), EstadoStock.StockDevolvidoFornecedor);
            d.setEstado(EstadoDevolucao.Devolvida);
            devolucaoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDevolucaoComoInvalida(int id) {
        Devolucao d = devolucaoDAO.get(id);
        if (d != null) {
            // A entrada de stock volta a ficar disponível
            atualizaEstadoStock(d.getCodStock(), EstadoStock.StockEmArmazem);
            d.setEstado(EstadoDevolucao.Invalida);
            devolucaoDAO.put(id, d);
        }
    }

    // ------------------- Encomenda -------------------

    @Override
    public Stock registarStock_Encomenda(int id_peca, float preco_compra, int quantidade) {
        int id = stockDAO.generateNewId();
        Stock novo = new Stock(id, preco_compra, id_peca, null, quantidade, EstadoStock.StockEncomendado);
        stockDAO.put(id, novo);
        return novo;
    }

    @Override
    public Encomenda registarEncomenda(List<Integer> id_peca, List<Float> preco_compra, List<Integer> quantidade, int cod_fornecedor) {
        int id = encomendaDAO.generateNewId();
        List<Integer> stockIds = new ArrayList<>();
        for (int i = 0; i < id_peca.size(); i++) {
            Peca p = pecaDAO.get(id_peca.get(i));
            if (p.getPreco_venda() >= 70) {
                StockComGarantia s = new StockComGarantia(stockDAO.generateNewId(), preco_compra.get(i), id_peca.get(i), null, "N/A", 24);
                stockDAO.put(s.getId(), s);
                stockIds.add(s.getId());
            } else {
                Stock s = new Stock(stockDAO.generateNewId(), preco_compra.get(i), id_peca.get(i), null, quantidade.get(i), EstadoStock.StockEncomendado);
                stockDAO.put(s.getId(), s);
                stockIds.add(s.getId());
            }
        }
        Encomenda e = new Encomenda(id, cod_fornecedor, stockIds);
        encomendaDAO.put(id, e);
        return e;
    }

    @Override
    public Encomenda atualizarEncomenda(int id, List<Integer> stockIds, LocalDate data_pedido, LocalDate data_chegada, EstadoEncomenda estado) {
        Encomenda e = encomendaDAO.get(id);
        if (e != null) {
            if (stockIds != null && !stockIds.isEmpty()) e.setCodStocks(stockIds);
            if (data_pedido != null)  e.setData_criacao(data_pedido);
            if (data_chegada != null) e.setData_rececao(data_chegada);
            if (estado != null)       e.setEstado(estado);
            encomendaDAO.put(id, e);
        }
        return e;
    }

    @Override
    public Encomenda obterEncomenda(int id) { return encomendaDAO.get(id); }

    @Override
    public boolean removerEncomenda(int id) { return encomendaDAO.remove(id) != null; }

    @Override
    public List<Encomenda> obterEncomendas() { return new ArrayList<>(encomendaDAO.values()); }

    @Override
    public Encomenda marcarEncomendaComoEnviada(int id) {
        Encomenda e = encomendaDAO.get(id);
        if (e != null && e.getEstado() == EstadoEncomenda.RASCUNHO) {
            e.setEstado(EstadoEncomenda.ENVIADA);
            e.setData_envio(LocalDate.now());
            encomendaDAO.put(id, e);
        }
        return e;
    }

    @Override
    public Encomenda marcarEncomendaComoRecebida(int id, List<String> numeros_serie, List<Integer> garantias) {
        Encomenda e = encomendaDAO.get(id);
        if (numeros_serie.size() != garantias.size())
            throw new EcoRideException("A lista de números de série e garantias deve ter o mesmo tamanho.");
        if (numeros_serie.size() > e.getCodStocks().size() || garantias.size() > e.getCodStocks().size())
            throw new EcoRideException("A lista de números de série e garantias não pode ser maior que a quantidade de stocks na encomenda.");
        int contador = 0;
        if (e.getEstado() == EstadoEncomenda.ENVIADA) {
            for (int stockId : e.getCodStocks()) {
                Stock s = stockDAO.get(stockId);
                if (s != null) {
                    Peca p = pecaDAO.get(s.getCodPeca());
                    if (p.getPreco_venda() >= 70) {
                        if (s instanceof StockComGarantia scg) {
                            scg.setNr_serie(numeros_serie.get(contador));
                            scg.setGarantia(garantias.get(contador));
                        }
                    }
                    s.setEstado(EstadoStock.StockEmArmazem);
                    s.setData_chegada(LocalDate.now());
                    stockDAO.put(stockId, s);
                }
                contador++;
            }
            e.setEstado(EstadoEncomenda.RECEBIDA);
            e.setData_rececao(LocalDate.now());
            encomendaDAO.put(id, e);
        } else {
            throw new EcoRideException("Encomenda " + id + " não está no estado ENVIADA.");
        }
        return e;
    }

    @Override
    public Map<Integer, Encomenda> gerarListaAutomatica() {
        Map<Integer, List<Integer>> idsPecas    = new java.util.HashMap<>();
        Map<Integer, List<Float>>   precos      = new java.util.HashMap<>();
        Map<Integer, List<Integer>> quantidades = new java.util.HashMap<>();
        for (Peca p : pecaDAO.values()) {
            int stockTotal = obter_quantidade_Stock_Peca_id(p.getId());
            if (stockTotal < p.getStock_minimo()) {
                int qtd         = p.getStock_minimo() - stockTotal;
                int fornecedor  = p.getCodFornecedor();
                idsPecas   .computeIfAbsent(fornecedor, k -> new ArrayList<>()).add(p.getId());
                precos     .computeIfAbsent(fornecedor, k -> new ArrayList<>()).add(0f);
                quantidades.computeIfAbsent(fornecedor, k -> new ArrayList<>()).add(qtd);
            }
        }
        Map<Integer, Encomenda> resultado = new java.util.HashMap<>();
        for (int fornecedor : idsPecas.keySet()) {
            Encomenda e = registarEncomenda(
                    idsPecas.get(fornecedor),
                    precos.get(fornecedor),
                    quantidades.get(fornecedor),
                    fornecedor);
            resultado.put(fornecedor, e);
        }
        return resultado;
    }
}
