package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.common.EcoRideException;
import app.common.Validacoes;
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
        Validacoes.naoVazio(nome, "Nome do fornecedor");
        // contacto pode ser telefone ou email — valida o que estiver preenchido
        if (telemovel != null && !telemovel.isBlank()) Validacoes.telefone(telemovel);
        if (email != null && !email.isBlank()) Validacoes.emailValido(email);
        if ((telemovel == null || telemovel.isBlank()) && (email == null || email.isBlank()))
            throw new EcoRideException("O fornecedor deve ter pelo menos um contacto (telefone ou email).");
        int id = fornecedorDAO.generateNewId();
        Fornecedor novo = new Fornecedor(id, nome, telemovel, email);
        fornecedorDAO.put(id, novo);
        return novo;
    }

    @Override
    public Fornecedor atualizarFornecedor(int id, String nome, String telemovel, String email) {
        Fornecedor f = fornecedorDAO.get(id);
        if (f == null) throw new EcoRideException("Fornecedor " + id + " não encontrado.");
        Validacoes.naoVazio(nome, "Nome do fornecedor");
        if (telemovel != null && !telemovel.isBlank()) Validacoes.telefone(telemovel);
        if (email != null && !email.isBlank()) Validacoes.emailValido(email);
        if ((telemovel == null || telemovel.isBlank()) && (email == null || email.isBlank()))
            throw new EcoRideException("O fornecedor deve ter pelo menos um contacto (telefone ou email).");
        f.setNome(nome);
        f.setTelemovel(telemovel);
        f.setEmail(email);
        fornecedorDAO.put(id, f);
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

    @Override
    public List<Peca> obterPecasDoFornecedor(int id_fornecedor) {
        return pecaDAO.getPecasByFornecedorId(id_fornecedor);
    }

    // ------------------- Peca -------------------

    @Override
    public Peca registarPeca(String ref, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, int garantia) {
        Validacoes.naoVazio(ref, "Referência");
        Validacoes.naoVazio(nome, "Nome da peça");
        Validacoes.naoVazio(descricao, "Descrição");
        Validacoes.inteiroNaoNegativo(stock_minimo, "Stock mínimo");
        Validacoes.inteiroPositivo(garantia, "Garantia");
        Validacoes.valorMonetario(preco_venda, "Preço de venda");
        if (!fornecedorDAO.containsKey(id_fornecedor)) throw new EcoRideException("Fornecedor " + id_fornecedor + " não encontrado.");
        int id = pecaDAO.generateNewId();
        Peca nova = new Peca(id, ref, marca, nome, descricao, stock_minimo, preco_venda, id_fornecedor, true, garantia);
        pecaDAO.put(id, nova);
        return nova;
    }

    @Override
    public Peca atualizarPeca(int id, String referencia, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa, int garantia) {
        Peca p = pecaDAO.get(id);
        if (p == null) throw new EcoRideException("Peça " + id + " não encontrada.");
        Validacoes.naoVazio(referencia, "Referência");
        Validacoes.naoVazio(nome, "Nome da peça");
        Validacoes.naoVazio(descricao, "Descrição");
        Validacoes.inteiroNaoNegativo(stock_minimo, "Stock mínimo");
        Validacoes.inteiroPositivo(garantia, "Garantia");
        Validacoes.valorMonetario(preco_venda, "Preço de venda");
        if (!fornecedorDAO.containsKey(id_fornecedor)) throw new EcoRideException("Fornecedor " + id_fornecedor + " não encontrado.");
        p.setReferencia(referencia);
        p.setMarca(marca);
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setStock_minimo(stock_minimo);
        p.setPreco_venda(preco_venda);
        p.setCodFornecedor(id_fornecedor);
        p.setAtiva(ativa);
        p.setGarantia(garantia);
        pecaDAO.put(id, p);
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
    public List<Peca> obterPecasAtivas() {
        List<Peca> resultado = new ArrayList<>();
        for (Peca p : pecaDAO.values()) {
            if (p.isAtiva()) resultado.add(p);
        }
        return resultado;
    }

    // ------------------- Stock -------------------

    @Override
    public Stock registarStock(int id_peca, float preco_compra, LocalDate data, int quantidade) {
        Validacoes.valorMonetario(preco_compra, "Preço de compra");
        Validacoes.naoNulo(data, "Data de receção");
        Validacoes.inteiroPositivo(quantidade, "Quantidade");
        int id = stockDAO.generateNewId();
        int garantia = pecaDAO.get(id_peca) != null ? pecaDAO.get(id_peca).getGarantia() : 0;
        LocalDate dataGarantia = garantia > 0 ? data.plusMonths(garantia) : null;
        Stock novo = new Stock(id, preco_compra, id_peca, data, quantidade, dataGarantia);
        stockDAO.put(id, novo);
        return novo;
    }

    @Override
    public Stock atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade) {
        Stock s = stockDAO.get(id_stock);
        if (s != null && preco_compra >= 0 && cod_Peca >= 0 && data_rececao != null && quantidade >= 0) {
            s.setPreco_compra(preco_compra);
            s.setCodPeca(cod_Peca);
            s.setData_chegada(data_rececao);
            s.setQuantidade(quantidade);
            int garantia = pecaDAO.get(cod_Peca) != null ? pecaDAO.get(cod_Peca).getGarantia() : 0;
            LocalDate dataGarantia = garantia > 0 ? data_rececao.plusMonths(garantia) : null;
            s.setGarantia(dataGarantia);
            stockDAO.put(id_stock, s);
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
    public Map<Integer, Integer> atribuirStocksFIFO(int codPeca, int quantidade) {
        Map<Integer, Integer> resultado = new LinkedHashMap<>();
        int restante = quantidade;
        for (Stock s : stockDAO.getByPecaId(codPeca)) {
            if (restante <= 0) break;
            if (s.getEstado() != EstadoStock.StockEmArmazem || s.getQuantidade() <= 0) continue;
            int consumir = Math.min(s.getQuantidade(), restante);
            resultado.put(s.getId(), consumir);
            s.setQuantidade(s.getQuantidade() - consumir);
            if (s.getQuantidade() == 0) s.setEstado(EstadoStock.StockUsadoConserto);
            stockDAO.put(s.getId(), s);
            restante -= consumir;
        }
        if (restante > 0)
            throw new EcoRideException("Stock insuficiente para a peça " + codPeca + ". Faltam " + restante + " unidades.");
        return resultado;
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
        if (motivo == null || motivo.isBlank()) throw new EcoRideException("Motivo não pode ser vazio.");
        if (idFuncionario < 0) throw new EcoRideException("ID do funcionário não pode ser negativo.");
        List<Defeito> resultado = new ArrayList<>();
        for (int stockId : stockIds) {
            Stock s = stockDAO.get(stockId);
            if (s == null)
                throw new EcoRideException("Stock " + stockId + " não encontrado.");
            EstadoStock estadoAtual = s.getEstado();
            if (estadoAtual != EstadoStock.StockEmArmazem && estadoAtual != EstadoStock.StockUsadoConserto)
                throw new EcoRideException("Stock " + stockId + " não pode ser sinalizado com defeito (estado: " + estadoAtual + ").");
            s.setEstado(EstadoStock.StockComPossivelDefeito);
            stockDAO.put(stockId, s);
            int id = defeitoDAO.generateNewId();
            Defeito novo = new Defeito(id, stockId, motivo, idFuncionario, estadoAtual);
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
            s.setEstado(d.getEstadoAnterior());
            stockDAO.put(s.getId(), s);
        }
        defeitoDAO.remove(idDefeito);
    }

    // ------------------- Devolucao -------------------

    @Override
    public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data) {
        if (motivo == null || motivo.isBlank()) throw new EcoRideException("Motivo não pode ser vazio.");
        if (data == null) throw new EcoRideException("Data não pode ser nula.");
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
            d.setEstado(EstadoDevolucao.Enviada);
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
    public Encomenda registarEncomenda(List<Integer> id_peca, List<Float> preco_compra, List<Integer> quantidade, int cod_fornecedor) {
        int id = encomendaDAO.generateNewId();
        List<Integer> stockIds = new ArrayList<>();
        LocalDate dataPedido = LocalDate.now();
        for (int i = 0; i < id_peca.size(); i++) {
            Peca p = pecaDAO.get(id_peca.get(i));
            if (p == null) throw new EcoRideException("Peça " + id_peca.get(i) + " não encontrada.");
            int garantia = p.getGarantia();
            LocalDate dataGarantia = garantia > 0 ? dataPedido.plusMonths(garantia) : null;
            Stock s = new Stock(stockDAO.generateNewId(), preco_compra.get(i), id_peca.get(i), null, quantidade.get(i), EstadoStock.StockEncomendado, dataGarantia);
            stockDAO.put(s.getId(), s);
            stockIds.add(s.getId());
        }
        Encomenda e = new Encomenda(id, cod_fornecedor, stockIds);
        encomendaDAO.put(id, e);
        return e;
    }

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
        if (e == null) throw new EcoRideException("Encomenda " + id + " não encontrada.");
        if (e.getEstado() == EstadoEncomenda.ENVIADA) {
            for (int stockId : e.getCodStocks()) {
                Stock s = stockDAO.get(stockId);
                if (s != null) {
                    s.setEstado(EstadoStock.StockEmArmazem);
                    s.setData_chegada(LocalDate.now());
                    stockDAO.put(stockId, s);
                }
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
    public Map<Integer, Map<Integer, Integer>> gerarListaAutomatica() {
        Map<Integer, Map<Integer, Integer>> resultado = new java.util.HashMap<>();
        for (Peca p : pecaDAO.values()) {
            int stockTotal = obter_quantidade_Stock_Peca_id(p.getId());
            if (stockTotal < p.getStock_minimo()) {
                int qtd = p.getStock_minimo() - stockTotal;
                int fornecedor = p.getCodFornecedor();
                resultado.computeIfAbsent(fornecedor, k -> new java.util.HashMap<>()).put(p.getId(), qtd);
            }
        }
        return resultado;
    }
}
