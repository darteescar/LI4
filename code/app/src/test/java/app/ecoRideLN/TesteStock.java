package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sStock.*;
import app.ecoRideLN.sStock.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para SStockFacade (Fornecedores, Peças, Stocks, Defeitos).
 * Usa DAOs em memória (HashMap) injetados via construtor package-protected.
 */
@DisplayName("Stock, Peças e Fornecedores")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteStock {

    // ── DAOs falsos ───────────────────────────────────────────────────────────

    static FornecedorDAO fornecedorDaoFake() {
        return new FornecedorDAO() {
            private final Map<Integer, Fornecedor> s = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);
            @Override public int size() { return s.size(); }
            @Override public boolean isEmpty() { return s.isEmpty(); }
            @Override public boolean containsKey(Object k) { return s.containsKey(k); }
            @Override public boolean containsValue(Object v) { return s.containsValue(v); }
            @Override public Fornecedor get(Object k) { return s.get(k); }
            @Override public Fornecedor put(Integer k, Fornecedor v) { return s.put(k, v); }
            @Override public Fornecedor remove(Object k) { return s.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Fornecedor> m) { s.putAll(m); }
            @Override public void clear() { s.clear(); }
            @Override public Set<Integer> keySet() { return s.keySet(); }
            @Override public Collection<Fornecedor> values() { return s.values(); }
            @Override public Set<Map.Entry<Integer, Fornecedor>> entrySet() { return s.entrySet(); }
            @Override public int insert(Fornecedor f) {
                int id = seq.getAndIncrement(); f.setId(id); s.put(id, f); return id;
            }
        };
    }

    static PecaDAO pecaDaoFake(Map<Integer, Peca> pecaStore, AtomicInteger pecaSeq) {
        return new PecaDAO() {
            @Override public int size() { return pecaStore.size(); }
            @Override public boolean isEmpty() { return pecaStore.isEmpty(); }
            @Override public boolean containsKey(Object k) { return pecaStore.containsKey(k); }
            @Override public boolean containsValue(Object v) { return pecaStore.containsValue(v); }
            @Override public Peca get(Object k) { return pecaStore.get(k); }
            @Override public Peca put(Integer k, Peca v) { return pecaStore.put(k, v); }
            @Override public Peca remove(Object k) { return pecaStore.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Peca> m) { pecaStore.putAll(m); }
            @Override public void clear() { pecaStore.clear(); }
            @Override public Set<Integer> keySet() { return pecaStore.keySet(); }
            @Override public Collection<Peca> values() { return pecaStore.values(); }
            @Override public Set<Map.Entry<Integer, Peca>> entrySet() { return pecaStore.entrySet(); }
            @Override public int insert(Peca p) {
                int id = pecaSeq.getAndIncrement(); p.setId(id); pecaStore.put(id, p); return id;
            }
            @Override public List<Peca> getPecasByFornecedorId(int idF) {
                return pecaStore.values().stream()
                        .filter(p -> p.getCodFornecedor() == idF)
                        .collect(Collectors.toList());
            }
        };
    }

    static StockDAO stockDaoFake(Map<Integer, Stock> stockStore, AtomicInteger stockSeq,
                                  Map<Integer, Defeito> defStore, AtomicInteger defSeq) {
        return new StockDAO() {
            @Override public int size() { return stockStore.size(); }
            @Override public boolean isEmpty() { return stockStore.isEmpty(); }
            @Override public boolean containsKey(Object k) { return stockStore.containsKey(k); }
            @Override public boolean containsValue(Object v) { return stockStore.containsValue(v); }
            @Override public Stock get(Object k) { return stockStore.get(k); }
            @Override public Stock put(Integer k, Stock v) { return stockStore.put(k, v); }
            @Override public Stock remove(Object k) { return stockStore.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Stock> m) { stockStore.putAll(m); }
            @Override public void clear() { stockStore.clear(); }
            @Override public Set<Integer> keySet() { return stockStore.keySet(); }
            @Override public Collection<Stock> values() { return stockStore.values(); }
            @Override public Set<Map.Entry<Integer, Stock>> entrySet() { return stockStore.entrySet(); }
            @Override public int insert(Stock s) {
                int id = stockSeq.getAndIncrement(); s.setId(id); stockStore.put(id, s); return id;
            }
            @Override public Stock removerComMovimentos(int id) { return stockStore.remove(id); }
            @Override public List<Stock> getByPecaId(int id) {
                return stockStore.values().stream()
                        .filter(s -> s.getCodPeca() == id).collect(Collectors.toList());
            }
            @Override public List<Stock> getOperacionais() {
                return stockStore.values().stream()
                        .filter(s -> s.getEstado() == EstadoStock.StockEmArmazem)
                        .collect(Collectors.toList());
            }
            @Override public Map<Integer, Integer> atribuirFIFO(int codPeca, int qty) {
                return Map.of();
            }
            @Override public List<Defeito> registarDefeito(int codPeca, String motivo, int idFunc) {
                List<Defeito> criados = new ArrayList<>();
                for (Stock s : getByPecaId(codPeca)) {
                    if (s.getEstado() == EstadoStock.StockEmArmazem) {
                        int id = defSeq.getAndIncrement();
                        Defeito d = new Defeito(id, s.getId(), motivo, idFunc, s.getEstado());
                        defStore.put(id, d);
                        s.setEstado(EstadoStock.StockComPossivelDefeito);
                        criados.add(d);
                    }
                }
                return criados;
            }
            @Override public Devolucao registarDevolucao(int codStock, String motivo, LocalDate data) {
                return null;
            }
            @Override public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) { return null; }
            @Override public void resolverDefeitoComSplit(int idDefeito, int qty, String motivo, LocalDate data) {}
            @Override public void marcarDevolucaoComoEnviada(int id) {}
            @Override public void marcarDevolucaoComoDevolvida(int id) {}
            @Override public void marcarDevolucaoComoInvalida(int id) {}
        };
    }

    static DefeitoDAO defeitoDaoFake(Map<Integer, Defeito> defStore) {
        return new DefeitoDAO() {
            @Override public int size() { return defStore.size(); }
            @Override public boolean isEmpty() { return defStore.isEmpty(); }
            @Override public boolean containsKey(Object k) { return defStore.containsKey(k); }
            @Override public boolean containsValue(Object v) { return defStore.containsValue(v); }
            @Override public Defeito get(Object k) { return defStore.get(k); }
            @Override public Defeito put(Integer k, Defeito v) { return defStore.put(k, v); }
            @Override public Defeito remove(Object k) { return defStore.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Defeito> m) { defStore.putAll(m); }
            @Override public void clear() { defStore.clear(); }
            @Override public Set<Integer> keySet() { return defStore.keySet(); }
            @Override public Collection<Defeito> values() { return defStore.values(); }
            @Override public Set<Map.Entry<Integer, Defeito>> entrySet() { return defStore.entrySet(); }
            @Override public int insert(Defeito d) { defStore.put(d.getId(), d); return d.getId(); }
        };
    }

    static DevolucaoDAO devolucaoDaoFake() {
        return new DevolucaoDAO() {
            private final Map<Integer, Devolucao> s = new HashMap<>();
            @Override public int size() { return s.size(); }
            @Override public boolean isEmpty() { return s.isEmpty(); }
            @Override public boolean containsKey(Object k) { return s.containsKey(k); }
            @Override public boolean containsValue(Object v) { return s.containsValue(v); }
            @Override public Devolucao get(Object k) { return s.get(k); }
            @Override public Devolucao put(Integer k, Devolucao v) { return s.put(k, v); }
            @Override public Devolucao remove(Object k) { return s.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Devolucao> m) { s.putAll(m); }
            @Override public void clear() { s.clear(); }
            @Override public Set<Integer> keySet() { return s.keySet(); }
            @Override public Collection<Devolucao> values() { return s.values(); }
            @Override public Set<Map.Entry<Integer, Devolucao>> entrySet() { return s.entrySet(); }
            @Override public List<Devolucao> getPendentes() { return List.of(); }
        };
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private SStockFacade facade;
    private Fornecedor fornecedor;
    private Peca peca;

    private final Map<Integer, Peca>    pecaStore  = new HashMap<>();
    private final Map<Integer, Stock>   stockStore = new HashMap<>();
    private final Map<Integer, Defeito> defStore   = new HashMap<>();
    private final AtomicInteger pecaSeq  = new AtomicInteger(1);
    private final AtomicInteger stockSeq = new AtomicInteger(1);
    private final AtomicInteger defSeq   = new AtomicInteger(1);

    @BeforeEach
    void setUp() {
        facade = new SStockFacade(
                fornecedorDaoFake(),
                pecaDaoFake(pecaStore, pecaSeq),
                stockDaoFake(stockStore, stockSeq, defStore, defSeq),
                new EncomendaDAO() {},   // não usado nestes testes
                defeitoDaoFake(defStore),
                devolucaoDaoFake()
        );
        fornecedor = facade.registarFornecedor("TechParts Lda", "212345678", "info@tp.pt");
        peca = facade.registarPeca("REF-001", "Shimano", "Travão Dianteiro",
                "Travão para trotinete", 5, 29.99f, fornecedor.getId(), 12);
    }

    // ── Fornecedor: inserção ──────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registarFornecedor: dados válidos criam fornecedor")
    void fornecedor_registar_valido() {
        assertNotNull(fornecedor);
        assertEquals("TechParts Lda", fornecedor.getNome());
        assertTrue(fornecedor.getId() > 0);
    }

    @Test @Order(2) @DisplayName("registarFornecedor: nome vazio lança exceção")
    void fornecedor_registar_nomeVazio() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFornecedor("", "212345678", "a@b.pt"));
    }

    @Test @Order(3) @DisplayName("registarFornecedor: sem contacto lança exceção")
    void fornecedor_registar_semContacto() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFornecedor("Fornecedor X", null, null));
    }

    @Test @Order(4) @DisplayName("registarFornecedor: email inválido lança exceção")
    void fornecedor_registar_emailInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFornecedor("X", null, "emailInvalido"));
    }

    @Test @Order(5) @DisplayName("registarFornecedor: só email é suficiente")
    void fornecedor_registar_soEmail() {
        assertDoesNotThrow(() -> facade.registarFornecedor("SóEmail SA", null, "c@se.pt"));
    }

    @Test @Order(6) @DisplayName("registarFornecedor: só telefone é suficiente")
    void fornecedor_registar_soTelefone() {
        assertDoesNotThrow(() -> facade.registarFornecedor("SóTel Lda", "212345678", null));
    }

    // ── Fornecedor: atualização ───────────────────────────────────────────────

    @Test @Order(7) @DisplayName("atualizarFornecedor: dados válidos atualizam corretamente")
    void fornecedor_atualizar_valido() {
        facade.atualizarFornecedor(fornecedor.getId(), "TechParts 2.0", "212345678", "novo@tp.pt");
        assertEquals("TechParts 2.0", facade.obterFornecedor(fornecedor.getId()).getNome());
    }

    @Test @Order(8) @DisplayName("atualizarFornecedor: ID inexistente lança exceção")
    void fornecedor_atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarFornecedor(999, "X", null, "a@b.pt"));
    }

    // ── Fornecedor: remoção ───────────────────────────────────────────────────

    @Test @Order(9) @DisplayName("removerFornecedor: remove corretamente")
    void fornecedor_remover() {
        assertTrue(facade.removerFornecedor(fornecedor.getId()));
        assertNull(facade.obterFornecedor(fornecedor.getId()));
    }

    // ── Peça: inserção ────────────────────────────────────────────────────────

    @Test @Order(10) @DisplayName("registarPeca: dados válidos criam peça ativa")
    void peca_registar_valido() {
        assertNotNull(peca);
        assertEquals("REF-001", peca.getReferencia());
        assertTrue(peca.isAtiva());
        assertEquals(12, peca.getGarantia());
    }

    @Test @Order(11) @DisplayName("registarPeca: referência vazia lança exceção")
    void peca_registar_refVazia() {
        assertThrows(EcoRideException.class, () ->
                facade.registarPeca("", "M", "N", "D", 0, 10f, fornecedor.getId(), 1));
    }

    @Test @Order(12) @DisplayName("registarPeca: garantia zero lança exceção")
    void peca_registar_garantiaZero() {
        assertThrows(EcoRideException.class, () ->
                facade.registarPeca("REF-002", "M", "N", "D", 0, 10f, fornecedor.getId(), 0));
    }

    @Test @Order(13) @DisplayName("registarPeca: preço negativo lança exceção")
    void peca_registar_precoNegativo() {
        assertThrows(EcoRideException.class, () ->
                facade.registarPeca("REF-002", "M", "N", "D", 0, -1f, fornecedor.getId(), 1));
    }

    @Test @Order(14) @DisplayName("registarPeca: fornecedor inexistente lança exceção")
    void peca_registar_fornecedorInexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.registarPeca("REF-002", "M", "N", "D", 0, 10f, 999, 1));
    }

    // ── Peça: atualização ─────────────────────────────────────────────────────

    @Test @Order(15) @DisplayName("atualizarPeca: dados válidos atualizam corretamente")
    void peca_atualizar_valido() {
        facade.atualizarPeca(peca.getId(), "REF-001-v2", "Shimano", "Travão Pro",
                "Travão melhorado", 10, 39.99f, fornecedor.getId(), true, 24);
        assertEquals("REF-001-v2", facade.obterPeca(peca.getId()).getReferencia());
        assertEquals(24, facade.obterPeca(peca.getId()).getGarantia());
    }

    @Test @Order(16) @DisplayName("atualizarPeca: ID inexistente lança exceção")
    void peca_atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarPeca(999, "R", "M", "N", "D", 0, 1f, fornecedor.getId(), true, 1));
    }

    // ── Stock: inserção ───────────────────────────────────────────────────────

    @Test @Order(17) @DisplayName("registarStock: cria stock em armazém")
    void stock_registar_valido() {
        Stock s = facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 10);
        assertNotNull(s);
        assertEquals(EstadoStock.StockEmArmazem, s.getEstado());
        assertEquals(10, s.getQuantidade());
    }

    @Test @Order(18) @DisplayName("registarStock: garantia calculada a partir da peça")
    void stock_registar_garantia() {
        LocalDate hoje = LocalDate.now();
        Stock s = facade.registarStock(peca.getId(), 15.0f, hoje, 5);
        assertEquals(hoje.plusMonths(12), s.getGarantia());
    }

    @Test @Order(19) @DisplayName("registarStock: quantidade zero lança exceção")
    void stock_registar_quantidadeZero() {
        assertThrows(EcoRideException.class, () ->
                facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 0));
    }

    @Test @Order(20) @DisplayName("registarStock: data nula lança exceção")
    void stock_registar_dataNula() {
        assertThrows(EcoRideException.class, () ->
                facade.registarStock(peca.getId(), 15.0f, null, 5));
    }

    @Test @Order(21) @DisplayName("registarStock: preço negativo lança exceção")
    void stock_registar_precoNegativo() {
        assertThrows(EcoRideException.class, () ->
                facade.registarStock(peca.getId(), -1f, LocalDate.now(), 5));
    }

    // ── Stock: remoção ────────────────────────────────────────────────────────

    @Test @Order(22) @DisplayName("removerStock: remove corretamente")
    void stock_remover() {
        Stock s = facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 5);
        assertTrue(facade.removerStock(s.getId()));
        assertNull(facade.obterStock(s.getId()));
    }

    // ── Defeito ───────────────────────────────────────────────────────────────

    @Test @Order(23) @DisplayName("registarDefeito: muda estado do stock para StockComPossivelDefeito")
    void defeito_registar() {
        facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 5);
        List<Defeito> ds = facade.registarDefeito(peca.getId(), "Peça partida", 1);
        assertFalse(ds.isEmpty());
        assertEquals(EstadoStock.StockComPossivelDefeito,
                facade.obterStock(ds.get(0).getCodStock()).getEstado());
    }

    @Test @Order(24) @DisplayName("registarDefeito: motivo vazio lança exceção")
    void defeito_registar_motivoVazio() {
        facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 5);
        assertThrows(EcoRideException.class, () ->
                facade.registarDefeito(peca.getId(), "", 1));
    }

    @Test @Order(25) @DisplayName("registarDefeito: ID funcionário negativo lança exceção")
    void defeito_registar_funcInvalido() {
        facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 5);
        assertThrows(EcoRideException.class, () ->
                facade.registarDefeito(peca.getId(), "Motivo", -1));
    }

    @Test @Order(26) @DisplayName("descartarDefeito: repõe estado EmArmazem no stock")
    void defeito_descartar() {
        facade.registarStock(peca.getId(), 15.0f, LocalDate.now(), 5);
        List<Defeito> ds = facade.registarDefeito(peca.getId(), "Partido", 1);
        int idDefeito = ds.get(0).getId();
        facade.descartarDefeito(idDefeito);
        assertEquals(EstadoStock.StockEmArmazem,
                facade.obterStock(ds.get(0).getCodStock()).getEstado());
    }

    // ── Estado final ──────────────────────────────────────────────────────────

    @Test @Order(27) @DisplayName("estrutura final: quantidade total de stocks somada corretamente")
    void estruturaFinal() {
        facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 3);
        facade.registarStock(peca.getId(), 12.0f, LocalDate.now(), 7);
        int total = facade.obterStocks().stream().mapToInt(Stock::getQuantidade).sum();
        assertEquals(10, total);
    }
}
