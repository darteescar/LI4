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
 * Testes de integração para os fluxos de Stock.
 *
 * Testa através de SStockFacade com DAOs em memória que implementam
 * a lógica real de negócio (FIFO, transições de Defeito e Devolução).
 *
 * Cenários cobertos:
 *   1. Consumo FIFO: stock mais antigo é consumido primeiro (vários lotes)
 *   2. FIFO: lote totalmente esgotado transita para StockUsadoConserto
 *   3. FIFO: lote parcialmente consumido permanece StockEmArmazem
 *   4. FIFO: consumo sequencial de três lotes com datas diferentes
 *   5. FIFO: stock insuficiente lança exceção
 *   6. Defeito: bloqueia todos os stocks da peça afetada (StockComPossivelDefeito)
 *   7. Defeito: não afeta stocks de outras peças
 *   8. Defeito descartado: repõe stock em StockEmArmazem
 *   9. Confirmar defeito: cria Devolução em StockPendenteDeDevolucao
 *  10. Devolução aceite pelo fornecedor: ciclo completo (Pendente → Enviada → Devolvida)
 *  11. Devolução recusada pelo fornecedor: ciclo completo (Pendente → Enviada → Inválida)
 *  12. Devolução direta de stock em armazém: cria Devolução pendente
 */
@DisplayName("Integração — Stock")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteIntegracaoStock {

    // ── DAOs em memória com lógica de negócio real ─────────────────────────────

    static FornecedorDAO fornecedorDaoFake(Map<Integer, Fornecedor> store, AtomicInteger seq) {
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

    static PecaDAO pecaDaoFake(Map<Integer, Peca> store, AtomicInteger seq) {
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

    /**
     * StockDAO com lógica de negócio completa:
     * - FIFO real com ordenação por data_chegada ASC, id ASC
     * - registarDefeito: bloqueia todos os stocks StockEmArmazem da peça
     * - registarDevolucao: cria devolução e muda estado do stock
     * - confirmarDefeitoComDevolucao: remove defeito, cria devolução
     * - marcarDevolucaoComoEnviada/Devolvida/Invalida: transições de estado
     */
    static StockDAO stockDaoFake(Map<Integer, Stock> stockStore, AtomicInteger stockSeq,
                                  Map<Integer, Defeito> defStore, AtomicInteger defSeq,
                                  Map<Integer, Devolucao> devStore, AtomicInteger devSeq) {
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
                        .filter(s -> s.getEstado() == EstadoStock.StockEmArmazem
                                  || s.getEstado() == EstadoStock.StockEncomendado)
                        .collect(Collectors.toList());
            }

            // ── FIFO ──────────────────────────────────────────────────────────
            @Override public Map<Integer, Integer> atribuirFIFO(int codPeca, int quantidade) {
                List<Stock> candidatos = stockStore.values().stream()
                        .filter(s -> s.getCodPeca() == codPeca
                                  && s.getEstado() == EstadoStock.StockEmArmazem
                                  && s.getQuantidade() > 0)
                        .sorted(Comparator.comparing(Stock::getData_chegada,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparingInt(Stock::getId))
                        .collect(Collectors.toList());
                Map<Integer, Integer> resultado = new LinkedHashMap<>();
                int restante = quantidade;
                for (Stock s : candidatos) {
                    if (restante <= 0) break;
                    int consumir = Math.min(s.getQuantidade(), restante);
                    resultado.put(s.getId(), consumir);
                    s.setQuantidade(s.getQuantidade() - consumir);
                    if (s.getQuantidade() == 0) s.setEstado(EstadoStock.StockUsadoConserto);
                    restante -= consumir;
                }
                if (restante > 0)
                    throw new EcoRideException("Stock insuficiente para a peça " + codPeca
                            + ". Faltam " + restante + " unidades.");
                return resultado;
            }

            // ── Defeito ───────────────────────────────────────────────────────
            @Override public List<Defeito> registarDefeito(int codPeca, String motivo, int idFunc) {
                List<Defeito> criados = new ArrayList<>();
                for (Stock s : getByPecaId(codPeca)) {
                    if (s.getEstado() == EstadoStock.StockEmArmazem) {
                        int id = defSeq.getAndIncrement();
                        Defeito d = new Defeito(id, s.getId(), motivo, idFunc, EstadoStock.StockEmArmazem);
                        defStore.put(id, d);
                        s.setEstado(EstadoStock.StockComPossivelDefeito);
                        criados.add(d);
                    }
                }
                if (criados.isEmpty())
                    throw new EcoRideException("Não foram encontrados stocks disponíveis para a peça " + codPeca + ".");
                return criados;
            }

            @Override public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) {
                Defeito d = defStore.get(idDefeito);
                if (d == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
                defStore.remove(idDefeito);
                Stock s = stockStore.get(d.getCodStock());
                if (s != null) s.setEstado(EstadoStock.StockPendenteDeDevolucao);
                int devId = devSeq.getAndIncrement();
                Devolucao dev = new Devolucao(devId, data, motivo, d.getCodStock());
                devStore.put(devId, dev);
                return dev;
            }

            @Override public void resolverDefeitoComSplit(int idDefeito, int qtdDefeituosa, String motivo, LocalDate data) {
                Defeito defeito = defStore.get(idDefeito);
                if (defeito == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
                Stock original = stockStore.get(defeito.getCodStock());
                if (original == null) throw new EcoRideException("Stock do defeito não encontrado.");
                if (qtdDefeituosa <= 0 || qtdDefeituosa > original.getQuantidade())
                    throw new EcoRideException("Quantidade inválida.");
                defStore.remove(idDefeito);
                if (qtdDefeituosa == original.getQuantidade()) {
                    original.setEstado(EstadoStock.StockPendenteDeDevolucao);
                    int devId = devSeq.getAndIncrement();
                    devStore.put(devId, new Devolucao(devId, data, motivo, original.getId()));
                } else {
                    original.setQuantidade(original.getQuantidade() - qtdDefeituosa);
                    original.setEstado(defeito.getEstadoAnterior());
                    int newId = stockSeq.getAndIncrement();
                    Stock novoStock = new Stock(newId, original.getPreco_compra(), original.getCodPeca(),
                            original.getData_chegada(), qtdDefeituosa,
                            EstadoStock.StockPendenteDeDevolucao, original.getGarantia());
                    stockStore.put(newId, novoStock);
                    int devId = devSeq.getAndIncrement();
                    devStore.put(devId, new Devolucao(devId, data, motivo, newId));
                }
            }

            // ── Devolução ─────────────────────────────────────────────────────
            @Override public Devolucao registarDevolucao(int codStock, String motivo, LocalDate data) {
                Stock s = stockStore.get(codStock);
                if (s == null) throw new EcoRideException("Stock " + codStock + " não encontrado.");
                if (s.getEstado() != EstadoStock.StockComPossivelDefeito
                        && s.getEstado() != EstadoStock.StockEmArmazem)
                    throw new EcoRideException("Stock " + codStock + " não está disponível para devolução.");
                s.setEstado(EstadoStock.StockPendenteDeDevolucao);
                int devId = devSeq.getAndIncrement();
                Devolucao dev = new Devolucao(devId, data, motivo, codStock);
                devStore.put(devId, dev);
                return dev;
            }

            @Override public void marcarDevolucaoComoEnviada(int idDevolucao) {
                Devolucao dev = devStore.get(idDevolucao);
                if (dev == null) throw new EcoRideException("Devolução " + idDevolucao + " não encontrada.");
                dev.setEstado(EstadoDevolucao.Enviada);
                Stock s = stockStore.get(dev.getCodStock());
                if (s != null) s.setEstado(EstadoStock.StockEnviadoParaFornecedor);
            }

            @Override public void marcarDevolucaoComoDevolvida(int idDevolucao) {
                Devolucao dev = devStore.get(idDevolucao);
                if (dev == null) throw new EcoRideException("Devolução " + idDevolucao + " não encontrada.");
                dev.setEstado(EstadoDevolucao.Devolvida);
                Stock s = stockStore.get(dev.getCodStock());
                if (s != null) s.setEstado(EstadoStock.StockDevolvidoFornecedor);
            }

            @Override public void marcarDevolucaoComoInvalida(int idDevolucao) {
                Devolucao dev = devStore.get(idDevolucao);
                if (dev == null) throw new EcoRideException("Devolução " + idDevolucao + " não encontrada.");
                dev.setEstado(EstadoDevolucao.Invalida);
                Stock s = stockStore.get(dev.getCodStock());
                if (s != null) s.setEstado(EstadoStock.StockinvalidoParaDevolucao);
            }
        };
    }

    static DefeitoDAO defeitoDaoFake(Map<Integer, Defeito> store) {
        return new DefeitoDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Defeito get(Object k) { return store.get(k); }
            @Override public Defeito put(Integer k, Defeito v) { return store.put(k, v); }
            @Override public Defeito remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Defeito> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Defeito> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Defeito>> entrySet() { return store.entrySet(); }
            @Override public int insert(Defeito d) { store.put(d.getId(), d); return d.getId(); }
        };
    }

    static DevolucaoDAO devolucaoDaoFake(Map<Integer, Devolucao> store) {
        return new DevolucaoDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Devolucao get(Object k) { return store.get(k); }
            @Override public Devolucao put(Integer k, Devolucao v) { return store.put(k, v); }
            @Override public Devolucao remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Devolucao> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Devolucao> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Devolucao>> entrySet() { return store.entrySet(); }
            @Override public List<Devolucao> getPendentes() {
                return store.values().stream()
                        .filter(d -> d.getEstado() == EstadoDevolucao.StockPendenteDeDevolucao)
                        .collect(Collectors.toList());
            }
        };
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private SStockFacade facade;
    private Fornecedor fornecedor;
    private Peca peca;
    private Peca outraPeca;

    private Map<Integer, Stock>    stockStore;
    private Map<Integer, Defeito>  defStore;
    private Map<Integer, Devolucao> devStore;

    @BeforeEach
    void setUp() {
        Map<Integer, Fornecedor> fornStore = new HashMap<>();
        Map<Integer, Peca>       pecaStore = new HashMap<>();
        stockStore = new HashMap<>();
        defStore   = new HashMap<>();
        devStore   = new HashMap<>();

        AtomicInteger fornSeq  = new AtomicInteger(1);
        AtomicInteger pecaSeq  = new AtomicInteger(1);
        AtomicInteger stockSeq = new AtomicInteger(1);
        AtomicInteger defSeq   = new AtomicInteger(1);
        AtomicInteger devSeq   = new AtomicInteger(1);

        facade = new SStockFacade(
                fornecedorDaoFake(fornStore, fornSeq),
                pecaDaoFake(pecaStore, pecaSeq),
                stockDaoFake(stockStore, stockSeq, defStore, defSeq, devStore, devSeq),
                new EncomendaDAO() {},
                defeitoDaoFake(defStore),
                devolucaoDaoFake(devStore)
        );

        fornecedor = facade.registarFornecedor("Fornecedor Integração", "212345678", null);
        peca       = facade.registarPeca("REF-INT-01", "Marca", "Peça Principal", "Desc", 0, 30.0f, fornecedor.getId(), 12);
        outraPeca  = facade.registarPeca("REF-INT-02", "Marca", "Peça Secundária", "Desc", 0, 20.0f, fornecedor.getId(), 6);
    }

    // ── Testes FIFO ───────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("FIFO: stock mais antigo é consumido antes do mais recente")
    void fifo_consome_lote_mais_antigo_primeiro() {
        LocalDate jan = LocalDate.of(2024, 1, 1);
        LocalDate mar = LocalDate.of(2024, 3, 1);

        facade.registarStock(peca.getId(), 10.0f, mar, 5); // chegou depois
        facade.registarStock(peca.getId(), 10.0f, jan, 3); // chegou antes

        // Consome 3 unidades: deverá consumir o lote de janeiro completo
        Map<Integer, Integer> resultado = facade.atribuirStocksFIFO(peca.getId(), 3);

        assertEquals(1, resultado.size(), "Deve ter consumido de exatamente um lote");
        int qtdConsumida = resultado.values().iterator().next();
        assertEquals(3, qtdConsumida);

        // O lote de janeiro deve estar totalmente esgotado
        boolean loteJanEsgotado = stockStore.values().stream()
                .filter(s -> s.getCodPeca() == peca.getId() && s.getData_chegada().equals(jan))
                .allMatch(s -> s.getEstado() == EstadoStock.StockUsadoConserto && s.getQuantidade() == 0);
        assertTrue(loteJanEsgotado, "O lote mais antigo deve estar esgotado");

        // O lote de março deve estar intacto
        boolean loteMarIntacto = stockStore.values().stream()
                .filter(s -> s.getCodPeca() == peca.getId() && s.getData_chegada().equals(mar))
                .allMatch(s -> s.getEstado() == EstadoStock.StockEmArmazem && s.getQuantidade() == 5);
        assertTrue(loteMarIntacto, "O lote mais recente não deve ter sido tocado");
    }

    @Test @Order(2) @DisplayName("FIFO: lote totalmente consumido transita para StockUsadoConserto")
    void fifo_lote_esgotado_transita_para_usado_conserto() {
        Stock s = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 4);

        facade.atribuirStocksFIFO(peca.getId(), 4); // consome tudo

        assertEquals(EstadoStock.StockUsadoConserto, stockStore.get(s.getId()).getEstado());
        assertEquals(0, stockStore.get(s.getId()).getQuantidade());
    }

    @Test @Order(3) @DisplayName("FIFO: lote parcialmente consumido permanece StockEmArmazem")
    void fifo_lote_parcialmente_consumido_permanece_em_armazem() {
        Stock s = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 10);

        Map<Integer, Integer> resultado = facade.atribuirStocksFIFO(peca.getId(), 3);

        assertEquals(EstadoStock.StockEmArmazem, stockStore.get(s.getId()).getEstado(),
                "Stock parcialmente consumido deve permanecer em armazém");
        assertEquals(7, stockStore.get(s.getId()).getQuantidade(),
                "Devem restar 7 unidades (10 - 3)");
        assertEquals(3, resultado.get(s.getId()), "Devem ter sido atribuídas 3 unidades");
    }

    @Test @Order(4) @DisplayName("FIFO: consumo sequencial atravessa três lotes por ordem de chegada")
    void fifo_tres_lotes_consumo_sequencial() {
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 2, 1);
        LocalDate d3 = LocalDate.of(2024, 3, 1);

        Stock s1 = facade.registarStock(peca.getId(), 10.0f, d1, 2);
        Stock s2 = facade.registarStock(peca.getId(), 10.0f, d2, 2);
        Stock s3 = facade.registarStock(peca.getId(), 10.0f, d3, 5);

        // Consome 5 unidades: 2 do lote 1, 2 do lote 2, 1 do lote 3
        Map<Integer, Integer> resultado = facade.atribuirStocksFIFO(peca.getId(), 5);

        assertEquals(2, resultado.get(s1.getId()), "Lote 1 deve fornecer 2 unidades");
        assertEquals(2, resultado.get(s2.getId()), "Lote 2 deve fornecer 2 unidades");
        assertEquals(1, resultado.get(s3.getId()), "Lote 3 deve fornecer 1 unidade");

        assertEquals(EstadoStock.StockUsadoConserto, stockStore.get(s1.getId()).getEstado());
        assertEquals(EstadoStock.StockUsadoConserto, stockStore.get(s2.getId()).getEstado());
        assertEquals(EstadoStock.StockEmArmazem,     stockStore.get(s3.getId()).getEstado());
        assertEquals(4, stockStore.get(s3.getId()).getQuantidade(), "Lote 3 deve ter 4 restantes");
    }

    @Test @Order(5) @DisplayName("FIFO: stock insuficiente lança EcoRideException")
    void fifo_stock_insuficiente_lanca_excecao() {
        facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 3); // apenas 3 disponíveis

        assertThrows(EcoRideException.class, () ->
                facade.atribuirStocksFIFO(peca.getId(), 5)); // pede 5
    }

    // ── Testes de Defeito ─────────────────────────────────────────────────────

    @Test @Order(6) @DisplayName("Defeito: bloqueia todos os stocks StockEmArmazem da peça afetada")
    void defeito_bloqueia_todos_os_stocks_da_peca() {
        Stock s1 = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        Stock s2 = facade.registarStock(peca.getId(), 12.0f, LocalDate.now().minusDays(10), 3);

        List<Defeito> defeitos = facade.registarDefeito(peca.getId(), "Peça defeituosa detetada em QC", 1);

        assertEquals(2, defeitos.size(), "Devem ter sido criados defeitos para ambos os stocks");
        assertEquals(EstadoStock.StockComPossivelDefeito, stockStore.get(s1.getId()).getEstado());
        assertEquals(EstadoStock.StockComPossivelDefeito, stockStore.get(s2.getId()).getEstado());
    }

    @Test @Order(7) @DisplayName("Defeito: não altera stocks de outras peças")
    void defeito_nao_afeta_stocks_de_outra_peca() {
        facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        Stock outro = facade.registarStock(outraPeca.getId(), 8.0f, LocalDate.now(), 10);

        facade.registarDefeito(peca.getId(), "Defeito apenas na peça principal", 1);

        assertEquals(EstadoStock.StockEmArmazem, stockStore.get(outro.getId()).getEstado(),
                "O stock da outra peça não deve ser afetado");
    }

    @Test @Order(8) @DisplayName("Defeito descartado: repõe stock em StockEmArmazem")
    void defeito_descartado_repoe_stock_em_armazem() {
        facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        List<Defeito> defeitos = facade.registarDefeito(peca.getId(), "Suspeita de defeito", 1);
        assertEquals(1, defeitos.size());

        int idDefeito = defeitos.get(0).getId();
        int idStock   = defeitos.get(0).getCodStock();

        facade.descartarDefeito(idDefeito);

        assertEquals(EstadoStock.StockEmArmazem, stockStore.get(idStock).getEstado(),
                "Após descartar o defeito, o stock deve voltar a StockEmArmazem");
        assertFalse(defStore.containsKey(idDefeito), "O registo de defeito deve ser removido");
    }

    @Test @Order(9) @DisplayName("Confirmar defeito: cria Devolução em estado StockPendenteDeDevolucao")
    void confirmar_defeito_cria_devolucao_pendente() {
        facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        List<Defeito> defeitos = facade.registarDefeito(peca.getId(), "Confirmado defeito de fábrica", 1);
        int idDefeito = defeitos.get(0).getId();
        int idStock   = defeitos.get(0).getCodStock();

        LocalDate dataConfirmacao = LocalDate.now();
        Devolucao dev = facade.confirmarDefeitoComDevolucao(idDefeito, "Defeito confirmado após inspeção", dataConfirmacao);

        assertNotNull(dev, "A devolução criada não deve ser nula");
        assertEquals(EstadoDevolucao.StockPendenteDeDevolucao, dev.getEstado());
        assertEquals(idStock, dev.getCodStock());

        assertEquals(EstadoStock.StockPendenteDeDevolucao, stockStore.get(idStock).getEstado(),
                "O stock deve transitar para StockPendenteDeDevolucao");
        assertFalse(defStore.containsKey(idDefeito), "O defeito deve ser eliminado após confirmação");
    }

    // ── Testes de Devolução ───────────────────────────────────────────────────

    @Test @Order(10) @DisplayName("Devolução aceite: ciclo Pendente → Enviada → Devolvida")
    void devolucao_aceite_pelo_fornecedor_ciclo_completo() {
        Stock s = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        List<Defeito> defeitos = facade.registarDefeito(peca.getId(), "Peça com defeito", 1);
        Devolucao dev = facade.confirmarDefeitoComDevolucao(defeitos.get(0).getId(), "Devolução por defeito", LocalDate.now());

        // Estado inicial: Pendente
        assertEquals(EstadoDevolucao.StockPendenteDeDevolucao, devStore.get(dev.getId()).getEstado());
        assertEquals(EstadoStock.StockPendenteDeDevolucao, stockStore.get(s.getId()).getEstado());

        // Envio ao fornecedor
        facade.marcarDevolucaoComoEnviada(dev.getId());
        assertEquals(EstadoDevolucao.Enviada, devStore.get(dev.getId()).getEstado());
        assertEquals(EstadoStock.StockEnviadoParaFornecedor, stockStore.get(s.getId()).getEstado());

        // Fornecedor aceita a devolução
        facade.marcarDevolucaoComoDevolvida(dev.getId());
        assertEquals(EstadoDevolucao.Devolvida, devStore.get(dev.getId()).getEstado());
        assertEquals(EstadoStock.StockDevolvidoFornecedor, stockStore.get(s.getId()).getEstado());
    }

    @Test @Order(11) @DisplayName("Devolução recusada: ciclo Pendente → Enviada → Inválida")
    void devolucao_recusada_pelo_fornecedor_ciclo_completo() {
        Stock s = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 5);
        List<Defeito> defeitos = facade.registarDefeito(peca.getId(), "Peça com suspeita de defeito", 2);
        Devolucao dev = facade.confirmarDefeitoComDevolucao(defeitos.get(0).getId(), "Enviado para análise", LocalDate.now());

        // Envio ao fornecedor
        facade.marcarDevolucaoComoEnviada(dev.getId());
        assertEquals(EstadoDevolucao.Enviada, devStore.get(dev.getId()).getEstado());

        // Fornecedor recusa a devolução
        facade.marcarDevolucaoComoInvalida(dev.getId());
        assertEquals(EstadoDevolucao.Invalida, devStore.get(dev.getId()).getEstado());
        assertEquals(EstadoStock.StockinvalidoParaDevolucao, stockStore.get(s.getId()).getEstado());
    }

    @Test @Order(12) @DisplayName("Devolução direta de stock em armazém cria registo Pendente")
    void devolucao_direta_de_stock_em_armazem_cria_pendente() {
        Stock s = facade.registarStock(peca.getId(), 10.0f, LocalDate.now(), 8);
        assertEquals(EstadoStock.StockEmArmazem, stockStore.get(s.getId()).getEstado());

        Devolucao dev = facade.registarDevolucao(s.getId(), "Encomenda incorreta — devolução direta", LocalDate.now());

        assertNotNull(dev);
        assertEquals(EstadoDevolucao.StockPendenteDeDevolucao, dev.getEstado());
        assertEquals(s.getId(), dev.getCodStock());

        assertEquals(EstadoStock.StockPendenteDeDevolucao, stockStore.get(s.getId()).getEstado(),
                "O stock deve passar a StockPendenteDeDevolucao após registo da devolução direta");

        List<Devolucao> pendentes = facade.obterDevolucoesPendentes();
        assertTrue(pendentes.stream().anyMatch(d -> d.getId() == dev.getId()),
                "A devolução deve constar na lista de pendentes");
    }
}
