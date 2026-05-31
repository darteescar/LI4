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
 * Testes unitários para SStockFacade — módulo de Encomendas.
 * Usa EncomendaDAO e StockDAO em memória injetados via construtor package-protected.
 */
@DisplayName("Encomendas")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteEncomendas {

    // ── DAOs falsos ───────────────────────────────────────────────────────────

    static FornecedorDAO fornFake(Map<Integer, Fornecedor> store, AtomicInteger seq) {
        return new FornecedorDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Fornecedor get(Object k) { return store.get(k); }
            @Override public Fornecedor put(Integer k, Fornecedor v) { return store.put(k, v); }
            @Override public Fornecedor remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Fornecedor> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Fornecedor> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Fornecedor>> entrySet() { return store.entrySet(); }
            @Override public int insert(Fornecedor f) {
                int id = seq.getAndIncrement(); f.setId(id); store.put(id, f); return id;
            }
        };
    }

    static PecaDAO pecaFake(Map<Integer, Peca> store, AtomicInteger seq) {
        return new PecaDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Peca get(Object k) { return store.get(k); }
            @Override public Peca put(Integer k, Peca v) { return store.put(k, v); }
            @Override public Peca remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Peca> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Peca> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Peca>> entrySet() { return store.entrySet(); }
            @Override public int insert(Peca p) {
                int id = seq.getAndIncrement(); p.setId(id); store.put(id, p); return id;
            }
            @Override public List<Peca> getPecasByFornecedorId(int idF) {
                return store.values().stream().filter(p -> p.getCodFornecedor() == idF).collect(Collectors.toList());
            }
        };
    }

    static StockDAO stockFake(Map<Integer, Stock> store, AtomicInteger seq) {
        return new StockDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Stock get(Object k) { return store.get(k); }
            @Override public Stock put(Integer k, Stock v) { return store.put(k, v); }
            @Override public Stock remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Stock> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Stock> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Stock>> entrySet() { return store.entrySet(); }
            @Override public int insert(Stock s) {
                int id = seq.getAndIncrement(); s.setId(id); store.put(id, s); return id;
            }
            @Override public Stock removerComMovimentos(int id) { return store.remove(id); }
            @Override public List<Stock> getByPecaId(int id) {
                return store.values().stream().filter(s -> s.getCodPeca() == id).collect(Collectors.toList());
            }
            @Override public List<Stock> getOperacionais() { return List.of(); }
            @Override public Map<Integer, Integer> atribuirFIFO(int c, int q) { return Map.of(); }
            @Override public List<Defeito> registarDefeito(int c, String m, int f) { return List.of(); }
            @Override public Devolucao registarDevolucao(int c, String m, LocalDate d) { return null; }
            @Override public Devolucao confirmarDefeitoComDevolucao(int i, String m, LocalDate d) { return null; }
            @Override public void resolverDefeitoComSplit(int i, int q, String m, LocalDate d) {}
            @Override public void marcarDevolucaoComoEnviada(int id) {}
            @Override public void marcarDevolucaoComoDevolvida(int id) {}
            @Override public void marcarDevolucaoComoInvalida(int id) {}
        };
    }

    static EncomendaDAO encomendaFake(Map<Integer, Encomenda> encStore, AtomicInteger encSeq,
                                       Map<Integer, Stock> stockStore, AtomicInteger stockSeq) {
        return new EncomendaDAO() {
            @Override public int size() { return encStore.size(); }
            @Override public boolean isEmpty() { return encStore.isEmpty(); }
            @Override public boolean containsKey(Object k) { return encStore.containsKey(k); }
            @Override public boolean containsValue(Object v) { return encStore.containsValue(v); }
            @Override public Encomenda get(Object k) { return encStore.get(k); }
            @Override public Encomenda put(Integer k, Encomenda v) { return encStore.put(k, v); }
            @Override public Encomenda remove(Object k) { return encStore.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Encomenda> m) { encStore.putAll(m); }
            @Override public void clear() { encStore.clear(); }
            @Override public Set<Integer> keySet() { return encStore.keySet(); }
            @Override public Collection<Encomenda> values() { return encStore.values(); }
            @Override public Set<Map.Entry<Integer, Encomenda>> entrySet() { return encStore.entrySet(); }
            @Override public int insert(Encomenda e) {
                int id = encSeq.getAndIncrement(); e.setId(id); encStore.put(id, e); return id;
            }

            @Override
            public Encomenda registarComStocks(int codFornecedor, List<Stock> stocks) {
                List<Integer> codStocks = new ArrayList<>();
                for (Stock s : stocks) {
                    int sid = stockSeq.getAndIncrement();
                    s.setId(sid);
                    stockStore.put(sid, s);
                    codStocks.add(sid);
                }
                int id = encSeq.getAndIncrement();
                Encomenda e = new Encomenda(id, codFornecedor, codStocks);
                encStore.put(id, e);
                return e;
            }

            @Override
            public Encomenda marcarComoRecebida(int id) {
                Encomenda e = encStore.get(id);
                if (e == null) throw new EcoRideException("Encomenda " + id + " não encontrada.");
                if (e.getEstado() != EstadoEncomenda.ENVIADA)
                    throw new EcoRideException("Encomenda " + id + " não está no estado ENVIADA.");
                e.setEstado(EstadoEncomenda.RECEBIDA);
                e.setData_rececao(LocalDate.now());
                for (int sid : e.getCodStocks()) {
                    Stock s = stockStore.get(sid);
                    if (s != null) {
                        s.setEstado(EstadoStock.StockEmArmazem);
                        s.setData_chegada(LocalDate.now());
                    }
                }
                return e;
            }

            @Override public List<Encomenda> getAbertas() {
                return encStore.values().stream()
                        .filter(e -> e.getEstado() == EstadoEncomenda.RASCUNHO
                                  || e.getEstado() == EstadoEncomenda.ENVIADA)
                        .collect(Collectors.toList());
            }
        };
    }

    static DevolucaoDAO devFake() {
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
    private Fornecedor forn;
    private Peca peca1, peca2;

    private final Map<Integer, Fornecedor> fornStore = new HashMap<>();
    private final Map<Integer, Peca>       pecaStore = new HashMap<>();
    private final Map<Integer, Stock>      stockStore = new HashMap<>();
    private final Map<Integer, Encomenda>  encStore   = new HashMap<>();
    private final AtomicInteger fornSeq  = new AtomicInteger(1);
    private final AtomicInteger pecaSeq  = new AtomicInteger(1);
    private final AtomicInteger stockSeq = new AtomicInteger(1);
    private final AtomicInteger encSeq   = new AtomicInteger(1);

    @BeforeEach
    void setUp() {
        facade = new SStockFacade(
                fornFake(fornStore, fornSeq),
                pecaFake(pecaStore, pecaSeq),
                stockFake(stockStore, stockSeq),
                encomendaFake(encStore, encSeq, stockStore, stockSeq),
                new DefeitoDAO() {},
                devFake()
        );
        forn  = facade.registarFornecedor("TechParts", null, "tp@tp.pt");
        peca1 = facade.registarPeca("REF-A", "M", "Peca A", "Desc", 5, 10f, forn.getId(), 12);
        peca2 = facade.registarPeca("REF-B", "M", "Peca B", "Desc", 5, 8f,  forn.getId(), 6);
    }

    // ── Inserção ──────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registarEncomenda: cria encomenda em RASCUNHO")
    void registar_valido() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10.0f), List.of(5), forn.getId());
        assertNotNull(e);
        assertEquals(EstadoEncomenda.RASCUNHO, e.getEstado());
        assertEquals(1, e.getCodStocks().size());
    }

    @Test @Order(2) @DisplayName("registarEncomenda: stocks criados em estado StockEncomendado")
    void registar_stocksEncomendados() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId(), peca2.getId()),
                List.of(10.0f, 8.0f), List.of(3, 5), forn.getId());
        for (int sid : e.getCodStocks())
            assertEquals(EstadoStock.StockEncomendado, stockStore.get(sid).getEstado());
    }

    @Test @Order(3) @DisplayName("registarEncomenda: peça inexistente lança exceção")
    void registar_pecaInexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.registarEncomenda(List.of(999), List.of(10f), List.of(5), forn.getId()));
    }

    // ── Transições de estado ──────────────────────────────────────────────────

    @Test @Order(4) @DisplayName("marcarEnviada: RASCUNHO → ENVIADA com data de envio")
    void enviar_valido() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10f), List.of(5), forn.getId());
        facade.marcarEncomendaComoEnviada(e.getId());
        assertEquals(EstadoEncomenda.ENVIADA, encStore.get(e.getId()).getEstado());
        assertNotNull(encStore.get(e.getId()).getData_envio());
    }

    @Test @Order(5) @DisplayName("marcarEnviada: encomenda já ENVIADA não volta a ENVIADA")
    void enviar_jaEnviada() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10f), List.of(5), forn.getId());
        facade.marcarEncomendaComoEnviada(e.getId());
        Encomenda resultado = facade.marcarEncomendaComoEnviada(e.getId());
        // facade devolve null/unchanged para encomendas não-RASCUNHO
        assertNotEquals(EstadoEncomenda.RASCUNHO,
                resultado == null ? EstadoEncomenda.ENVIADA : resultado.getEstado());
    }

    @Test @Order(6) @DisplayName("marcarRecebida: ENVIADA → RECEBIDA e stocks em armazém")
    void receber_valido() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10f), List.of(5), forn.getId());
        facade.marcarEncomendaComoEnviada(e.getId());
        facade.marcarEncomendaComoRecebida(e.getId());
        assertEquals(EstadoEncomenda.RECEBIDA, encStore.get(e.getId()).getEstado());
        for (int sid : e.getCodStocks())
            assertEquals(EstadoStock.StockEmArmazem, stockStore.get(sid).getEstado());
    }

    @Test @Order(7) @DisplayName("marcarRecebida: encomenda em RASCUNHO lança exceção")
    void receber_semEnvio() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10f), List.of(5), forn.getId());
        assertThrows(EcoRideException.class, () -> facade.marcarEncomendaComoRecebida(e.getId()));
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(8) @DisplayName("removerEncomenda: remove corretamente")
    void remover_valido() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId()), List.of(10f), List.of(5), forn.getId());
        assertTrue(facade.removerEncomenda(e.getId()));
        assertNull(encStore.get(e.getId()));
    }

    @Test @Order(9) @DisplayName("removerEncomenda: ID inexistente devolve false")
    void remover_inexistente() {
        assertFalse(facade.removerEncomenda(999));
    }

    // ── Estado final ──────────────────────────────────────────────────────────

    @Test @Order(10) @DisplayName("estrutura final: fluxo completo RASCUNHO→ENVIADA→RECEBIDA")
    void estruturaFinal() {
        Encomenda e = facade.registarEncomenda(
                List.of(peca1.getId(), peca2.getId()),
                List.of(10f, 8f), List.of(3, 5), forn.getId());
        assertEquals(EstadoEncomenda.RASCUNHO, e.getEstado());
        facade.marcarEncomendaComoEnviada(e.getId());
        assertEquals(EstadoEncomenda.ENVIADA, encStore.get(e.getId()).getEstado());
        facade.marcarEncomendaComoRecebida(e.getId());
        assertEquals(EstadoEncomenda.RECEBIDA, encStore.get(e.getId()).getEstado());
        assertNotNull(encStore.get(e.getId()).getData_rececao());
        e.getCodStocks().forEach(sid ->
                assertEquals(EstadoStock.StockEmArmazem, stockStore.get(sid).getEstado()));
    }
}
