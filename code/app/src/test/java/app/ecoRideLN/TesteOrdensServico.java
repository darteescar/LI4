package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideLN.sOrdensServico.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para SOrdensServicoFacade.
 * Usa OrdemServicoDAO em memória (HashMap) injetado via construtor package-protected.
 */
@DisplayName("Ordens de Serviço")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteOrdensServico {

    // ── DAO falso em memória ──────────────────────────────────────────────────

    static OrdemServicoDAO osDaoFake() {
        return new OrdemServicoDAO() {
            private final Map<Integer, OrdemServico> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

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

            @Override
            public int insert(OrdemServico os) {
                int id = seq.getAndIncrement();
                os.setId(id);
                store.put(id, os);
                return id;
            }

            @Override
            public List<OrdemServico> getOSsAtivas() {
                return store.values().stream()
                        .filter(os -> os.getEstado() != EstadoOS.Paga
                                   && os.getEstado() != EstadoOS.Eliminada)
                        .collect(Collectors.toList());
            }

            @Override
            public List<OrdemServico> getAvailableOSs() {
                return store.values().stream()
                        .filter(os -> os.getEstado() == EstadoOS.PendenteDiagnostico)
                        .collect(Collectors.toList());
            }

            @Override
            public List<OrdemServico> getOSDoTrotinete(int idTrotinete) {
                return store.values().stream()
                        .filter(os -> os.getRegisto().getCodTrotinete() == idTrotinete)
                        .collect(Collectors.toList());
            }

            @Override
            public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde,
                    LocalDateTime ate, Integer idCliente, Integer idFunc) {
                return store.values().stream()
                        .filter(os -> estado == null || os.getEstado() == estado)
                        .collect(Collectors.toList());
            }
        };
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private SOrdensServicoFacade facade;
    private Registo registoValido;

    @BeforeEach
    void setUp() {
        facade = new SOrdensServicoFacade(osDaoFake());
        registoValido = new Registo("Verificação geral", LocalDateTime.now(), 1, 1, 1, List.of("Carregador"));
    }

    private OrdemServico criarOS() { return facade.registarOS(registoValido); }

    // ── Criação ───────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registar: OS válida começa em PendenteDiagnostico")
    void registar_valido() {
        OrdemServico os = criarOS();
        assertNotNull(os);
        assertTrue(os.getId() > 0);
        assertEquals(EstadoOS.PendenteDiagnostico, os.getEstado());
        assertNull(os.getCodMecanico());
    }

    @Test @Order(2) @DisplayName("registar: ID de cliente inválido lança exceção")
    void registar_clienteInvalido() {
        Registo r = new Registo("Desc", LocalDateTime.now(), 1, 0, 1, List.of());
        assertThrows(EcoRideException.class, () -> facade.registarOS(r));
    }

    @Test @Order(3) @DisplayName("registar: ID de trotinete inválido lança exceção")
    void registar_trotineteInvalido() {
        Registo r = new Registo("Desc", LocalDateTime.now(), 0, 1, 1, List.of());
        assertThrows(EcoRideException.class, () -> facade.registarOS(r));
    }

    @Test @Order(4) @DisplayName("registar: descrição vazia lança exceção")
    void registar_descricaoVazia() {
        Registo r = new Registo("", LocalDateTime.now(), 1, 1, 1, List.of());
        assertThrows(EcoRideException.class, () -> facade.registarOS(r));
    }

    // ── Atribuição ────────────────────────────────────────────────────────────

    @Test @Order(5) @DisplayName("atribuir: mecânico atribuído corretamente")
    void atribuir_mecanico() {
        OrdemServico os = criarOS();
        assertTrue(facade.atribuirOS(os.getId(), 42));
        assertEquals(42, facade.obterOS(os.getId()).getCodMecanico());
    }

    @Test @Order(6) @DisplayName("atribuir: OS Eliminada lança exceção")
    void atribuir_osEliminada() {
        OrdemServico os = criarOS();
        facade.eliminarOS(os.getId());
        assertThrows(EcoRideException.class, () -> facade.atribuirOS(os.getId(), 42));
    }

    // ── Máquina de estados ────────────────────────────────────────────────────

    @Test @Order(7) @DisplayName("EstadoOS: PendenteDiagnostico → PendenteAprovacaoOrcamento é válido")
    void estado_pendenteParaAprovacao() {
        assertTrue(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento));
    }

    @Test @Order(8) @DisplayName("EstadoOS: PendenteDiagnostico → Paga é inválido")
    void estado_pendenteNaoPodeIrParaPaga() {
        assertFalse(EstadoOS.PendenteDiagnostico.podeTransicionar(EstadoOS.Paga));
    }

    @Test @Order(9) @DisplayName("EstadoOS: Paga não pode transicionar para nenhum estado")
    void estado_pagaTerminal() {
        for (EstadoOS destino : EstadoOS.values())
            assertFalse(EstadoOS.Paga.podeTransicionar(destino));
    }

    @Test @Order(10) @DisplayName("EstadoOS: Eliminada não pode transicionar para nenhum estado")
    void estado_eliminadaTerminal() {
        for (EstadoOS destino : EstadoOS.values())
            assertFalse(EstadoOS.Eliminada.podeTransicionar(destino));
    }

    @Test @Order(11) @DisplayName("eliminarOS: avança para Eliminada")
    void eliminar_valido() {
        OrdemServico os = criarOS();
        assertTrue(facade.eliminarOS(os.getId()));
        assertEquals(EstadoOS.Eliminada, facade.obterOS(os.getId()).getEstado());
    }

    // ── Diagnóstico ───────────────────────────────────────────────────────────

    @Test @Order(12) @DisplayName("registarDiagnostico: cria diagnóstico e avança estado")
    void diagnostico_registar() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 5);
        Diagnostico d = facade.registarDiagnosticoOS(os.getId(), Map.of(1, 2), List.of(1), 150f, "Verificação", 5);
        assertNotNull(d);
        assertEquals(EstadoOS.PendenteAprovacaoOrcamento, facade.obterOS(os.getId()).getEstado());
        assertEquals(150f, d.getOrcamento());
    }

    @Test @Order(13) @DisplayName("registarDiagnostico: dados nulos lançam exceção")
    void diagnostico_dadosNulos() {
        OrdemServico os = criarOS();
        assertThrows(EcoRideException.class, () ->
                facade.registarDiagnosticoOS(os.getId(), null, null, 100f, "Desc", 1));
    }

    @Test @Order(14) @DisplayName("registarDiagnostico: OS inexistente lança exceção")
    void diagnostico_osInexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.registarDiagnosticoOS(999, Map.of(), List.of(), 100f, "Desc", 1));
    }

    @Test @Order(15) @DisplayName("aprovarOrcamento: aprovação avança para PendenteReparacao")
    void orcamento_aprovar() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 5);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 100f, "OK", 5);
        assertTrue(facade.aprovarOrcamentoOS(os.getId()));
        assertEquals(EstadoOS.PendenteReparacao, facade.obterOS(os.getId()).getEstado());
        assertTrue(facade.obterOS(os.getId()).getDiagnostico().isAprovado());
    }

    // ── Conserto ─────────────────────────────────────────────────────────────

    @Test @Order(16) @DisplayName("registarConserto: cria conserto e avança para PendentePagamento")
    void conserto_registar() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 200f, "Diagnóstico", 7);
        facade.aprovarOrcamentoOS(os.getId());
        Conserto c = facade.registarConsertoOS(os.getId(), Map.of(1, 1), List.of(1), 200f, 7);
        assertNotNull(c);
        assertEquals(EstadoOS.PendentePagamento, facade.obterOS(os.getId()).getEstado());
    }

    @Test @Order(17) @DisplayName("registarConserto: sem orçamento aprovado lança exceção")
    void conserto_semOrcamentoAprovado() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 200f, "Diagnóstico", 7);
        assertThrows(EcoRideException.class, () ->
                facade.registarConsertoOS(os.getId(), Map.of(), List.of(), 200f, 7));
    }

    @Test @Order(18) @DisplayName("registarConserto: mecânico errado lança exceção")
    void conserto_mecanicoErrado() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 200f, "Diagnóstico", 7);
        facade.aprovarOrcamentoOS(os.getId());
        assertThrows(EcoRideException.class, () ->
                facade.registarConsertoOS(os.getId(), Map.of(), List.of(), 200f, 99));
    }

    // ── Pagamento ─────────────────────────────────────────────────────────────

    @Test @Order(19) @DisplayName("notificarCliente: PendentePagamento → ClienteNotificado")
    void notificar_valido() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 100f, "Desc", 7);
        facade.aprovarOrcamentoOS(os.getId());
        facade.registarConsertoOS(os.getId(), Map.of(), List.of(), 100f, 7);
        assertTrue(facade.registarNotificacaoPagamentoOS(os.getId()));
        assertEquals(EstadoOS.ClienteNotificado, facade.obterOS(os.getId()).getEstado());
    }

    @Test @Order(20) @DisplayName("registarPagamento: ClienteNotificado → Paga")
    void pagamento_valido() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 100f, "Desc", 7);
        facade.aprovarOrcamentoOS(os.getId());
        facade.registarConsertoOS(os.getId(), Map.of(), List.of(), 100f, 7);
        facade.registarNotificacaoPagamentoOS(os.getId());
        Pagamento pag = new Pagamento(Metodo_Pagamento.NUMERARIO, LocalDateTime.now());
        assertTrue(facade.registarPagamentoOS(os.getId(), pag));
        assertEquals(EstadoOS.Paga, facade.obterOS(os.getId()).getEstado());
    }

    @Test @Order(21) @DisplayName("registarPagamento: sem notificação prévia lança exceção")
    void pagamento_semNotificacao() {
        OrdemServico os = criarOS();
        facade.atribuirOS(os.getId(), 7);
        facade.registarDiagnosticoOS(os.getId(), Map.of(), List.of(), 100f, "Desc", 7);
        facade.aprovarOrcamentoOS(os.getId());
        facade.registarConsertoOS(os.getId(), Map.of(), List.of(), 100f, 7);
        Pagamento pag = new Pagamento(Metodo_Pagamento.MULTIBANCO, LocalDateTime.now());
        assertThrows(EcoRideException.class, () -> facade.registarPagamentoOS(os.getId(), pag));
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(22) @DisplayName("remover: OS existente é removida")
    void remover_valido() {
        OrdemServico os = criarOS();
        assertTrue(facade.removerOS(os.getId()));
        assertNull(facade.obterOS(os.getId()));
    }

    @Test @Order(23) @DisplayName("remover: ID inexistente devolve false")
    void remover_inexistente() {
        assertFalse(facade.removerOS(999));
    }

    // ── Estado final ──────────────────────────────────────────────────────────

    @Test @Order(24) @DisplayName("estrutura final: fluxo completo de uma OS até Paga")
    void estruturaFinal() {
        OrdemServico os = criarOS();
        assertEquals(EstadoOS.PendenteDiagnostico, os.getEstado());

        facade.atribuirOS(os.getId(), 3);
        facade.registarDiagnosticoOS(os.getId(), Map.of(1, 1), List.of(1), 300f, "Disco partido", 3);
        assertEquals(EstadoOS.PendenteAprovacaoOrcamento, facade.obterOS(os.getId()).getEstado());

        facade.aprovarOrcamentoOS(os.getId());
        assertEquals(EstadoOS.PendenteReparacao, facade.obterOS(os.getId()).getEstado());

        facade.registarConsertoOS(os.getId(), Map.of(1, 1), List.of(1), 300f, 3);
        assertEquals(EstadoOS.PendentePagamento, facade.obterOS(os.getId()).getEstado());

        facade.registarNotificacaoPagamentoOS(os.getId());
        assertEquals(EstadoOS.ClienteNotificado, facade.obterOS(os.getId()).getEstado());

        facade.registarPagamentoOS(os.getId(), new Pagamento(Metodo_Pagamento.MBWAY, LocalDateTime.now()));
        assertEquals(EstadoOS.Paga, facade.obterOS(os.getId()).getEstado());
    }
}
