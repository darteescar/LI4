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

    @Override
    public int obter_quantidade_Stock_Peca_id(int id) {
        int total = 0;
        for (Stock s : stockDAO.getByPecaId(id)) total += s.getQuantidade();
        return total;
    }

    @Override
    public List<Integer> obter_Pecas_baixo_Stock_minimo() {
        List<Integer> out = new ArrayList<>();
        for (Peca p : pecaDAO.values())
            if (obter_quantidade_Stock_Peca_id(p.getId()) < p.getStock_minimo())
                out.add(p.getId());
        return out;
    }

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
    public int pecasDefeituosas_Stock(int id_peca) {
        int count = 0;
        for (Stock s : stockDAO.getByPecaId(id_peca))
            if (s.getEstado() == EstadoStock.PossivelDefeito) count++;
        return count;
    }

    // ------------------- Defeito -------------------

    @Override
    public Defeito registarDefeito(int codStock, String motivo, int idFuncionario, int quantidade) {
        Stock s = stockDAO.get(codStock);
        if (s == null) throw new EcoRideException("Stock " + codStock + " não encontrado.");
        if (quantidade <= 0 || quantidade > s.getQuantidade())
            throw new EcoRideException("Quantidade inválida: " + quantidade + " (disponível: " + s.getQuantidade() + ").");

        int targetStockId;
        if (quantidade == s.getQuantidade()) {
            // Marca toda a entrada como com possível defeito (incluindo StockComGarantia, sempre quantidade=1)
            s.setEstado(EstadoStock.PossivelDefeito);
            stockDAO.put(codStock, s);
            targetStockId = codStock;
        } else {
            // Parte a entrada — só acontece em Stock normal (StockComGarantia tem sempre quantidade=1)
            s.setQuantidade(s.getQuantidade() - quantidade);
            stockDAO.put(codStock, s);
            targetStockId = stockDAO.generateNewId();
            Stock splitStock = new Stock(targetStockId, s.getPreco_compra(), s.getCodPeca(),
                    s.getData_chegada(), quantidade, EstadoStock.PossivelDefeito);
            stockDAO.put(targetStockId, splitStock);
        }

        int id = defeitoDAO.generateNewId();
        Defeito novo = new Defeito(id, targetStockId, motivo, idFuncionario);
        defeitoDAO.put(id, novo);
        return novo;
    }

    @Override
    public Defeito obterDefeito(int id) { return defeitoDAO.get(id); }

    @Override
    public List<Defeito> obterDefeitos() { return new ArrayList<>(defeitoDAO.values()); }

    @Override
    public boolean removerDefeito(int id) { return defeitoDAO.remove(id) != null; }

    @Override
    public void marcarDefeitoComoPendenteDevolucao(int id) {
        Defeito d = defeitoDAO.get(id);
        if (d != null) {
            d.setEstado(EstadoDefeito.PendenteDevolucao);
            defeitoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDefeitoComoResolvido(int id) {
        Defeito d = defeitoDAO.get(id);
        if (d != null) {
            Stock s = stockDAO.get(d.getCodStock());
            if (s != null) {
                s.setEstado(EstadoStock.EmStock);
                stockDAO.put(d.getCodStock(), s);
            }
            d.setEstado(EstadoDefeito.Resolvido);
            defeitoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDefeitoComoInvalido(int id) {
        Defeito d = defeitoDAO.get(id);
        if (d != null) {
            Stock s = stockDAO.get(d.getCodStock());
            if (s != null) {
                s.setEstado(EstadoStock.Invalida);
                stockDAO.put(d.getCodStock(), s);
            }
            d.setEstado(EstadoDefeito.Invalido);
            defeitoDAO.put(id, d);
        }
    }

    // ------------------- Devolucao -------------------

    @Override
    public Devolucao registarDevolucao(LocalDate data_devolucao, String motivo, int id_stock, int quantidade) {
        Stock s = stockDAO.get(id_stock);
        if (s == null) throw new EcoRideException("Stock " + id_stock + " não encontrado.");
        if (quantidade <= 0 || quantidade > s.getQuantidade())
            throw new EcoRideException("Quantidade inválida: " + quantidade + " (disponível: " + s.getQuantidade() + ").");

        int targetStockId;
        if (quantidade == s.getQuantidade()) {
            s.setEstado(EstadoStock.PendenteDevolucao);
            stockDAO.put(id_stock, s);
            targetStockId = id_stock;
        } else {
            // Parte a entrada: reduz a original e cria uma nova para a devolução
            s.setQuantidade(s.getQuantidade() - quantidade);
            stockDAO.put(id_stock, s);
            targetStockId = stockDAO.generateNewId();
            Stock splitStock = new Stock(targetStockId, s.getPreco_compra(), s.getCodPeca(),
                    s.getData_chegada(), quantidade, EstadoStock.PendenteDevolucao);
            stockDAO.put(targetStockId, splitStock);
        }

        int id = devolucaoDAO.generateNewId();
        Devolucao nova = new Devolucao(id, data_devolucao, motivo, targetStockId);
        devolucaoDAO.put(id, nova);
        return nova;
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
            atualizaEstadoStock(d.getCodStock(), EstadoStock.Enviada);
            d.setEstado(EstadoDevolucao.Enviada);
            devolucaoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDevolucaoComoDevolvida(int id) {
        Devolucao d = devolucaoDAO.get(id);
        if (d != null) {
            atualizaEstadoStock(d.getCodStock(), EstadoStock.Devolvida);
            d.setEstado(EstadoDevolucao.Devolvida);
            devolucaoDAO.put(id, d);
        }
    }

    @Override
    public void marcarDevolucaoComoInvalida(int id) {
        Devolucao d = devolucaoDAO.get(id);
        if (d != null) {
            // A entrada de stock volta a ficar disponível
            atualizaEstadoStock(d.getCodStock(), EstadoStock.EmStock);
            d.setEstado(EstadoDevolucao.Invalida);
            devolucaoDAO.put(id, d);
        }
    }

    // ------------------- Encomenda -------------------

    @Override
    public Encomenda registarEncomenda(List<ItemEncomenda> itens, int cod_fornecedor) {
        int id = encomendaDAO.generateNewId();
        Encomenda e = new Encomenda(id, cod_fornecedor, itens);
        encomendaDAO.put(id, e);
        return e;
    }

    @Override
    public Encomenda atualizarEncomenda(int id, List<ItemEncomenda> itens, LocalDate data_pedido, LocalDate data_chegada, EstadoEncomenda estado) {
        Encomenda e = encomendaDAO.get(id);
        if (e != null) {
            if (itens != null && !itens.isEmpty()) e.setItensEncomendados(itens);
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
    public Encomenda marcarEncomendaComoRecebida(int id) {
        Encomenda e = encomendaDAO.get(id);
        if (e != null && e.getEstado() == EstadoEncomenda.ENVIADA) {
            List<Integer> novosStocks = new ArrayList<>();
            for (ItemEncomenda item : e.getItensEncomendados()) {
                int idStock = stockDAO.generateNewId();
                Stock novoStock = new Stock(idStock, item.getPreco_unitario(), item.getCodPeca(),
                        LocalDate.now(), item.getQuantidade());
                stockDAO.put(idStock, novoStock);
                novosStocks.add(idStock);
            }
            e.setEstado(EstadoEncomenda.RECEBIDA);
            e.setData_rececao(LocalDate.now());
            e.setCodEntradasStock(novosStocks);
            encomendaDAO.put(id, e);
        }
        return e;
    }

    @Override
    public boolean validaEncomenda_Rascunho(int id_Encomenda) {
        Encomenda e = encomendaDAO.get(id_Encomenda);
        return e != null && e.getEstado() == EstadoEncomenda.RASCUNHO;
    }

    @Override
    public int quantidade_encomendar_peca(int id_peca) {
        Peca p = pecaDAO.get(id_peca);
        if (p == null) return 0;
        return Math.max(0, p.getStock_minimo() - obter_quantidade_Stock_Peca_id(id_peca));
    }

    @Override
    public Map<Integer, List<ItemEncomenda>> gerarListaAutomatica() {
        Map<Integer, List<ItemEncomenda>> lista = new java.util.HashMap<>();
        for (Peca p : pecaDAO.values()) {
            int qtd = quantidade_encomendar_peca(p.getId());
            if (qtd > 0) {
                ItemEncomenda item = new ItemEncomenda(p.getId(), qtd, p.getPreco_venda());
                lista.computeIfAbsent(p.getCodFornecedor(), k -> new ArrayList<>()).add(item);
            }
        }
        return lista;
    }
}
