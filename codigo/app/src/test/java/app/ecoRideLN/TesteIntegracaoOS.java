package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import app.ecoRideCD.sStock.*;
import app.ecoRideLN.sAutenticacao.*;
import app.ecoRideLN.sClientes.*;
import app.ecoRideLN.sFinanceiro.*;
import app.ecoRideLN.sFuncionarios.*;
import app.ecoRideLN.sNotificacoes.*;
import app.ecoRideLN.sOrdensServico.*;
import app.ecoRideLN.sReparacoes.*;
import app.ecoRideLN.sStock.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para os fluxos de Ordem de Serviço.
 *
 * Testa através do EcoRideController (facade principal), ligando em memória:
 *   - SOrdensServicoFacade (com DAO falso)
 *   - SStockFacade        (com DAOs falsos, incluindo FIFO real)
 *   - SReparacoesFacade   (com DAO falso)
 *   - Stubs mínimos para Autenticação, Notificações, Clientes e Financeiro
 *
 * Cenários cobertos:
 *   1. Ciclo de vida completo (PendenteDiagnostico → Paga)
 *   2. Fluxo de rejeição de orçamento e eliminação
 *   3. Fluxo de espera por peças e retoma
 *   4. Validação de responsabilidade do mecânico (diagnóstico, conserto, aguardarPeças)
 *   5. Validação de custo do conserto vs. orçamento aprovado
 *   6. Validação de checklist incompleta
 *   7. Transições de estado válidas e inválidas (podeTransicionar)
 *   8. Consumo FIFO de stock no conserto
 */
@DisplayName("Integração — Ordens de Serviço")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteIntegracaoOS {

    // ── Stubs mínimos para subsistemas não testados ────────────────────────────

    private static final ISAutenticacao AUTH_STUB = new ISAutenticacao() {
        @Override public Utilizador registarUtilizador(String p, int idF, Cargo c, String id) { return null; }
        @Override public Utilizador atualizarUtilizador(int id, int idF, Cargo c, String ident) { return null; }
        @Override public Utilizador obterUtilizador(int id) { return null; }
        @Override public boolean existeUtilizador(int id) { return false; }
        @Override public boolean removerUtilizador(int id) { return false; }
        @Override public List<Utilizador> obterUtilizadores() { return List.of(); }
        @Override public List<Integer> obterUtilizadoresPorCargo(Cargo... cargo) { return List.of(); }
        @Override public boolean autenticar(int id, String pwd) { return false; }
        @Override public boolean atualizarPalavraPasseUtilizador(int id, String old, String novo) { return false; }
        @Override public Utilizador obterUtilizadorPorIdentificador(String ident) { return null; }
        @Override public int obterIdUserPorIdFuncionario(int idFunc) { return 1; }
    };

    private static final ISNotificacoes NOTIF_STUB = new ISNotificacoes() {
        @Override public void registarNotificacaoOS(String desc, int rem, List<Integer> dests, int idOS) {}
        @Override public void registarNotificacaoStock(String desc, int rem, List<Integer> dests, int idPeca) {}
        @Override public boolean removerNotificacao(int id, int idUser) { return false; }
        @Override public List<Notificacao> obterNotificacoesPorDestinatario(int id) { return List.of(); }
        @Override public boolean sinalizarNotificacao_comoTratada(int id, int idUser) { return false; }
        @Override public boolean sinalizarNotificacao_comoLida(int id, int idUser) { return false; }
    };

    private static final ISClientes CLIENTES_STUB = new ISClientes() {
        @Override public Cliente registarCliente(String n, String e, String t, String nif) { return null; }
        @Override public Cliente atualizarCliente(int id, String n, String e, String t, String nif) { return null; }
        @Override public Cliente obterCliente(int id) { return new Cliente(id, "Cliente Teste", "t@t.pt", "912345678", "123456789"); }
        @Override public boolean existeCliente(int id) { return true; }
        @Override public boolean removerCliente(int id) { return false; }
        @Override public List<Cliente> obterClientes() { return List.of(); }
        @Override public Trotinete registarTrotinete(int idC, String mo, String ma, String ns, String tm) { return null; }
        @Override public Trotinete atualizarTrotinete(int id, int idC, String mo, String ma, String ns, String tm) { return null; }
        @Override public Trotinete obterTrotinete(int id) { return null; }
        @Override public boolean existeTrotinete(int id) { return false; }
        @Override public boolean removerTrotinete(int id) { return false; }
        @Override public List<Trotinete> obterTrotinetes() { return List.of(); }
        @Override public List<Trotinete> obterTrotinetes_Cliente(int id) { return List.of(); }
    };

    private static final ISFinanceiro FINANCEIRO_STUB = new ISFinanceiro() {
        @Override public MovimentoFinanceiro registarMovimentoCompraStock(int id, float v, String d) { return null; }
        @Override public MovimentoFinanceiro registarMovimentoVendaPeca(int id, float v, String d) { return null; }
        @Override public MovimentoFinanceiro registarMovimentoPagamentoFuncionario(int id, float v, String d) { return null; }
        @Override public MovimentoFinanceiro registarMovimentoReparacaoOS(int id, float v, String d) { return null; }
        @Override public List<MovimentoFinanceiro> obterMovimentos() { return List.of(); }
        @Override public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(java.time.LocalDate d, java.time.LocalDate a, TipoMovimento t) { return List.of(); }
        @Override public boolean existeMovimentoFinanceiro(int id) { return false; }
        @Override public boolean removerMovimentoFinanceiro(int id) { return false; }
        @Override public void removerMovimentosFinanceirosPorStock(int id) {}
        @Override public AnaliseFinanceira calcularAnaliseFinanceira(List<MovimentoFinanceiro> m) { return null; }
    };

    private static final ISFuncionarios FUNC_STUB = new ISFuncionarios() {
        @Override public Funcionario registarFuncionario(String n, String t, String e, LocalDate d, String niss, String nif, String nus, String iban, float sh, float sl, float sb, int he, String np, String r, String l, String cp) { return null; }
        @Override public Funcionario atualizarFuncionario(int id, String n, String t, String e, LocalDate d, String niss, String nif, String nus, String iban, float sh, float sl, float sb, int he, String np, String r, String l, String cp) { return null; }
        @Override public Funcionario obterFuncionario(int id) { return null; }
        @Override public boolean existeFuncionario(int id) { return false; }
        @Override public boolean removerFuncionario(int id) { return false; }
        @Override public List<Funcionario> obterFuncionarios() { return List.of(); }
        @Override public void adicionarHorasExtra(int id, int horas) {}
        @Override public float registarPagamentoFuncionario(int id) { return 0f; }
    };

    // ── DAO falsos em memória ──────────────────────────────────────────────────

    static OrdemServicoDAO osDaoFake(Map<Integer, OrdemServico> store, AtomicInteger seq) {
        return new OrdemServicoDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public OrdemServico get(Object k) { return store.get(k); }
            @Override public OrdemServico put(Integer k, OrdemServico v) { return store.put(k, v); }
            @Override public OrdemServico remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends OrdemServico> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<OrdemServico> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, OrdemServico>> entrySet() { return store.entrySet(); }
            @Override public int insert(OrdemServico os) {
                int id = seq.getAndIncrement(); os.setId(id); store.put(id, os); return id;
            }
            @Override public List<OrdemServico> getOSsAtivas() {
                return store.values().stream()
                        .filter(os -> os.getEstado() != EstadoOS.Paga && os.getEstado() != EstadoOS.Eliminada)
                        .collect(Collectors.toList());
            }
            @Override public List<OrdemServico> getAvailableOSs() {
                return store.values().stream()
                        .filter(os -> os.getEstado() == EstadoOS.PendenteDiagnostico)
                        .collect(Collectors.toList());
            }
            @Override public List<OrdemServico> getOSDoTrotinete(int idT) {
                return store.values().stream()
                        .filter(os -> os.getRegisto().getCodTrotinete() == idT)
                        .collect(Collectors.toList());
            }
            @Override public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde,
                    LocalDateTime ate, Integer idCliente, Integer idFunc) {
                return store.values().stream()
                        .filter(os -> estado == null || os.getEstado() == estado)
                        .collect(Collectors.toList());
            }
        };
    }

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
     * StockDAO com FIFO real: consome stocks ordenados por data_chegada ASC, id ASC.
     * Partilha stockStore com o DevolucaoDAO (via devolucaoStore passado por referência).
     */
    static StockDAO stockDaoFifoFake(Map<Integer, Stock> stockStore, AtomicInteger stockSeq,
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
                        .filter(s -> s.getEstado() == EstadoStock.StockEmArmazem
                                  || s.getEstado() == EstadoStock.StockEncomendado)
                        .collect(Collectors.toList());
            }
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
            @Override public Devolucao registarDevolucao(int codStock, String motivo, LocalDate data) { return null; }
            @Override public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) { return null; }
            @Override public void resolverDefeitoComSplit(int idDefeito, int qty, String motivo, LocalDate data) {}
            @Override public void marcarDevolucaoComoEnviada(int id) {}
            @Override public void marcarDevolucaoComoDevolvida(int id) {}
            @Override public void marcarDevolucaoComoInvalida(int id) {}
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

    static ReparacaoDAO repDaoFake(Map<Integer, Reparacao> store, AtomicInteger seq) {
        return new ReparacaoDAO() {
            @Override public int size() { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Reparacao get(Object k) { return store.get(k); }
            @Override public Reparacao put(Integer k, Reparacao v) { return store.put(k, v); }
            @Override public Reparacao remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Reparacao> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Reparacao> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Reparacao>> entrySet() { return store.entrySet(); }
            @Override public int insert(Reparacao r) {
                int id = seq.getAndIncrement(); r.setId(id); store.put(id, r); return id;
            }
        };
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private EcoRideController ctrl;
    private SOrdensServicoFacade osFacade;
    private SStockFacade stockFacade;
    private SReparacoesFacade repFacade;

    private int pecaId;
    private int repId;

    private Map<Integer, Stock>    stockStore;

    @BeforeEach
    void setUp() {
        Map<Integer, OrdemServico> osStore    = new HashMap<>();
        Map<Integer, Fornecedor>   fornStore  = new HashMap<>();
        Map<Integer, Peca>         pecaStore  = new HashMap<>();
        stockStore                             = new HashMap<>();
        Map<Integer, Defeito>      defStore   = new HashMap<>();
        Map<Integer, Reparacao>    repStore   = new HashMap<>();

        AtomicInteger osSeq    = new AtomicInteger(1);
        AtomicInteger fornSeq  = new AtomicInteger(1);
        AtomicInteger pecaSeq  = new AtomicInteger(1);
        AtomicInteger stockSeq = new AtomicInteger(1);
        AtomicInteger defSeq   = new AtomicInteger(1);
        AtomicInteger repSeq   = new AtomicInteger(1);

        osFacade = new SOrdensServicoFacade(osDaoFake(osStore, osSeq));

        stockFacade = new SStockFacade(
                fornecedorDaoFake(fornStore, fornSeq),
                pecaDaoFake(pecaStore, pecaSeq),
                stockDaoFifoFake(stockStore, stockSeq, defStore, defSeq),
                new EncomendaDAO() {},
                defeitoDaoFake(defStore),
                new DevolucaoDAO() {
                    @Override public List<Devolucao> getPendentes() { return List.of(); }
                }
        );

        repFacade = new SReparacoesFacade(repDaoFake(repStore, repSeq));

        ctrl = new EcoRideController(
                NOTIF_STUB, AUTH_STUB, CLIENTES_STUB, FINANCEIRO_STUB,
                FUNC_STUB, osFacade, stockFacade, repFacade);

        // Pré-popular: fornecedor → peca (50€) → stock (20 unidades) + reparação (100€)
        Fornecedor forn = stockFacade.registarFornecedor("Fornecedor Teste", "212345678", null);
        Peca peca = stockFacade.registarPeca("REF-T01", "Marca", "Peça Teste", "Desc", 0, 50.0f, forn.getId(), 12);
        pecaId = peca.getId();
        stockFacade.registarStock(pecaId, 10.0f, LocalDate.now(), 20);

        Reparacao rep = repFacade.registarReparacao("Revisão Geral", "Desc", 100.0f, true);
        repId = rep.getId();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private int criarOSAtribuida(int mecanicoId) {
        OrdemServico os = ctrl.registarOS(1, 1, "Verificação geral", List.of("Carregador"), mecanicoId);
        ctrl.atribuirOS(os.getId(), mecanicoId);
        return os.getId();
    }

    private void diagnosticar(int osId, int pecaQtd, int mecanicoId) {
        // peca 1x50 + rep 1x100 = 150 de orçamento
        ctrl.registarDiagnosticoOS(osId, Map.of(pecaId, pecaQtd), List.of(repId), "Diagnóstico de teste", mecanicoId);
    }

    private CheckList checklistCompleta() {
        CheckList cl = new CheckList();
        cl.setLuzes(true); cl.setPneus(true); cl.setAceleracao(true);
        cl.setTravagem(true); cl.setVisor(true); cl.setTeste_pratico(true);
        return cl;
    }

    // ── Testes ────────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("Ciclo de vida completo: PendenteDiagnostico → Paga")
    void cicloVida_completo_ate_pagamento() {
        int osId = criarOSAtribuida(5);
        assertEquals(EstadoOS.PendenteDiagnostico, ctrl.obterOS(osId).getEstado());

        diagnosticar(osId, 1, 5);
        assertEquals(EstadoOS.PendenteAprovacaoOrcamento, ctrl.obterOS(osId).getEstado());
        assertEquals(150.0f, ctrl.obterOS(osId).getDiagnostico().getOrcamento(), 0.01f);

        ctrl.aprovarOrcamentoOS(osId, 0);
        assertEquals(EstadoOS.PendenteReparacao, ctrl.obterOS(osId).getEstado());
        assertTrue(ctrl.obterOS(osId).getDiagnostico().isAprovado());

        ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 1), List.of(repId), 5, checklistCompleta());
        assertEquals(EstadoOS.PendentePagamento, ctrl.obterOS(osId).getEstado());
        assertNotNull(ctrl.obterOS(osId).getConserto());

        ctrl.registarNotificacaoPagamentoOS(osId, 5);
        assertEquals(EstadoOS.ClienteNotificado, ctrl.obterOS(osId).getEstado());

        ctrl.registarPagamentoOS(osId, Metodo_Pagamento.MBWAY);
        assertEquals(EstadoOS.Paga, ctrl.obterOS(osId).getEstado());
    }

    @Test @Order(2) @DisplayName("Fluxo de rejeição: PendenteAprovacaoOrcamento → OrcamentoNaoAprovado → Eliminada")
    void fluxo_rejeicao_orcamento_e_eliminacao() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5);
        assertEquals(EstadoOS.PendenteAprovacaoOrcamento, ctrl.obterOS(osId).getEstado());

        ctrl.rejeitarOrcamentoOS(osId, 0);
        assertEquals(EstadoOS.OrcamentoNaoAprovado, ctrl.obterOS(osId).getEstado());

        ctrl.cancelarOS(osId);
        assertEquals(EstadoOS.Eliminada, ctrl.obterOS(osId).getEstado());
    }

    @Test @Order(3) @DisplayName("Fluxo de espera por peças: PendenteReparacao → AguardarPecas → PendenteReparacao")
    void fluxo_aguardar_pecas_e_retomar() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5);
        ctrl.aprovarOrcamentoOS(osId, 0);
        assertEquals(EstadoOS.PendenteReparacao, ctrl.obterOS(osId).getEstado());

        ctrl.aguardarPecas(osId, 5);
        assertEquals(EstadoOS.AguardarPecas, ctrl.obterOS(osId).getEstado());

        // Mecânico retoma quando peças chegam (via facade interna, sem wrapper no controller)
        osFacade.pecasRecebidasOS(osId);
        assertEquals(EstadoOS.PendenteReparacao, ctrl.obterOS(osId).getEstado());
    }

    @Test @Order(4) @DisplayName("Mecânico errado no diagnóstico lança exceção")
    void mecanico_errado_no_diagnostico_lanca_excecao() {
        int osId = criarOSAtribuida(5); // mecânico 5 é o responsável

        assertThrows(EcoRideException.class, () ->
                diagnosticar(osId, 1, 7)); // mecânico 7 não é responsável
    }

    @Test @Order(5) @DisplayName("Mecânico errado no conserto lança exceção")
    void mecanico_errado_no_conserto_lanca_excecao() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5);
        ctrl.aprovarOrcamentoOS(osId, 0);

        // O custo (50+100=150) não excede o orçamento (150), mas o mecânico 7 não é responsável
        assertThrows(EcoRideException.class, () ->
                ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 1), List.of(repId), 7, checklistCompleta()));
    }

    @Test @Order(6) @DisplayName("Mecânico errado ao marcar aguardar peças lança exceção")
    void mecanico_errado_aguardar_pecas_lanca_excecao() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5);
        ctrl.aprovarOrcamentoOS(osId, 0);

        assertThrows(EcoRideException.class, () ->
                ctrl.aguardarPecas(osId, 7)); // mecânico 7 não é responsável
    }

    @Test @Order(7) @DisplayName("Conserto com custo igual ao orçamento é válido")
    void conserto_custo_igual_ao_orcamento_e_valido() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5); // orcamento = 1*50 + 100 = 150
        ctrl.aprovarOrcamentoOS(osId, 0);

        // Conserto com exatamente 1 peca + 1 rep = 150 = orcamento
        assertDoesNotThrow(() ->
                ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 1), List.of(repId), 5, checklistCompleta()));
        assertEquals(EstadoOS.PendentePagamento, ctrl.obterOS(osId).getEstado());
    }

    @Test @Order(8) @DisplayName("Conserto com custo acima do orçamento lança exceção")
    void conserto_custo_excede_orcamento_lanca_excecao() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5); // orcamento = 1*50 + 100 = 150
        ctrl.aprovarOrcamentoOS(osId, 0);

        // Conserto com 2 pecas + 1 rep = 2*50 + 100 = 200 > 150+0.01
        EcoRideException ex = assertThrows(EcoRideException.class, () ->
                ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 2), List.of(repId), 5, checklistCompleta()));
        assertTrue(ex.getMessage().contains("excede"));
    }

    @Test @Order(9) @DisplayName("Conserto com checklist incompleta lança exceção")
    void conserto_checklist_incompleta_lanca_excecao() {
        int osId = criarOSAtribuida(5);
        diagnosticar(osId, 1, 5);
        ctrl.aprovarOrcamentoOS(osId, 0);

        CheckList incompleta = new CheckList(); // todos os campos false
        EcoRideException ex = assertThrows(EcoRideException.class, () ->
                ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 1), List.of(repId), 5, incompleta));
        assertTrue(ex.getMessage().contains("Checklist"));
    }

    @Test @Order(10) @DisplayName("Todas as transições válidas de EstadoOS são reconhecidas")
    void todas_as_transicoes_validas_sao_reconhecidas() {
        // PendenteDiagnostico
        assertTrue(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento));
        assertTrue(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.Eliminada));
        assertFalse(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.PendenteReparacao));
        assertFalse(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.Paga));

        // PendenteAprovacaoOrcamento
        assertTrue(EstadoOS.PendenteAprovacaoOrcamento.podeTransicionar(EstadoOS.PendenteReparacao));
        assertTrue(EstadoOS.PendenteAprovacaoOrcamento.podeTransicionar(EstadoOS.OrcamentoNaoAprovado));
        assertTrue(EstadoOS.PendenteAprovacaoOrcamento.podeTransicionar(EstadoOS.Eliminada));
        assertFalse(EstadoOS.PendenteAprovacaoOrcamento.podeTransicionar(EstadoOS.Paga));

        // OrcamentoNaoAprovado — só pode ir para Eliminada
        assertTrue(EstadoOS.OrcamentoNaoAprovado.podeTransicionar(EstadoOS.Eliminada));
        assertFalse(EstadoOS.OrcamentoNaoAprovado.podeTransicionar(EstadoOS.PendenteReparacao));

        // PendenteReparacao
        assertTrue(EstadoOS.PendenteReparacao.podeTransicionar(EstadoOS.AguardarPecas));
        assertTrue(EstadoOS.PendenteReparacao.podeTransicionar(EstadoOS.PendentePagamento));
        assertTrue(EstadoOS.PendenteReparacao.podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento));
        assertTrue(EstadoOS.PendenteReparacao.podeTransicionar(EstadoOS.Eliminada));
        assertFalse(EstadoOS.PendenteReparacao.podeTransicionar(EstadoOS.Paga));

        // AguardarPecas
        assertTrue(EstadoOS.AguardarPecas.podeTransicionar(EstadoOS.PendenteReparacao));
        assertTrue(EstadoOS.AguardarPecas.podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento));
        assertTrue(EstadoOS.AguardarPecas.podeTransicionar(EstadoOS.PendentePagamento));
        assertTrue(EstadoOS.AguardarPecas.podeTransicionar(EstadoOS.Eliminada));

        // ClienteNotificado
        assertTrue(EstadoOS.ClienteNotificado.podeTransicionar(EstadoOS.PendentePagamento));
        assertTrue(EstadoOS.ClienteNotificado.podeTransicionar(EstadoOS.Paga));
        assertTrue(EstadoOS.ClienteNotificado.podeTransicionar(EstadoOS.Eliminada));
    }

    @Test @Order(11) @DisplayName("Estados terminais (Paga e Eliminada) não admitem transições")
    void estados_terminais_nao_transicionam() {
        for (EstadoOS destino : EstadoOS.values()) {
            assertFalse(EstadoOS.Paga.podeTransicionar(destino),
                    "Paga não deveria transicionar para " + destino);
            assertFalse(EstadoOS.Eliminada.podeTransicionar(destino),
                    "Eliminada não deveria transicionar para " + destino);
        }
    }

    @Test @Order(12) @DisplayName("Transição inválida: saltar estados falha na facade")
    void transicao_invalida_salta_estado_falha() {
        int osId = criarOSAtribuida(5);
        // PendenteDiagnostico → PendentePagamento é inválido
        assertFalse(osFacade.alterarEstadoOS(osId, EstadoOS.PendentePagamento));
        assertEquals(EstadoOS.PendenteDiagnostico, ctrl.obterOS(osId).getEstado());
    }

    @Test @Order(13) @DisplayName("FIFO no conserto: stock mais antigo é consumido primeiro")
    void conserto_consome_stock_fifo_dois_lotes() {
        // Dois lotes com datas diferentes: lote1 mais antigo (qty=3), lote2 mais recente (qty=10)
        // O stock registado no setUp tem qty=20 com data=today. Criamos dois adicionais com datas específicas.
        stockStore.clear(); // limpar o stock do setUp para controlar as datas

        LocalDate jan = LocalDate.of(2024, 1, 1);
        LocalDate fev = LocalDate.of(2024, 2, 1);

        Stock lote1 = new Stock(0, 10.0f, pecaId, jan, 3, LocalDate.now().plusMonths(12));
        Stock lote2 = new Stock(0, 10.0f, pecaId, fev, 10, LocalDate.now().plusMonths(12));
        stockFacade.registarStock(pecaId, 10.0f, jan, 3);
        stockFacade.registarStock(pecaId, 10.0f, fev, 10);

        // Diagnostico: 5 unidades da peca (5*50=250) + sem reparações → orcamento=250
        int osId = criarOSAtribuida(5);
        ctrl.registarDiagnosticoOS(osId, Map.of(pecaId, 5), List.of(), "Teste FIFO", 5);
        ctrl.aprovarOrcamentoOS(osId, 0);

        // Conserto: consome 5 unidades → deve consumir lote1 completo (3) + lote2 parcial (2)
        ctrl.registarConsertoOS(osId, Map.of(String.valueOf(pecaId), 5), List.of(), 5, checklistCompleta());

        // Verificar que os stocks foram consumidos em FIFO
        List<Stock> stocksPeca = stockStore.values().stream()
                .filter(s -> s.getCodPeca() == pecaId)
                .sorted(Comparator.comparing(Stock::getData_chegada))
                .collect(Collectors.toList());

        assertEquals(2, stocksPeca.size());
        // Lote 1 (mais antigo) deve estar totalmente consumido
        assertEquals(EstadoStock.StockUsadoConserto, stocksPeca.get(0).getEstado());
        assertEquals(0, stocksPeca.get(0).getQuantidade());
        // Lote 2 (mais recente) deve ter 8 restantes (10 - 2)
        assertEquals(EstadoStock.StockEmArmazem, stocksPeca.get(1).getEstado());
        assertEquals(8, stocksPeca.get(1).getQuantidade());

        // O conserto deve registar os dois stockIds com as quantidades corretas
        Map<Integer, Integer> stocksUsados = ctrl.obterOS(osId).getConserto().getStocksUsados();
        int totalUsado = stocksUsados.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(5, totalUsado);
    }
}
