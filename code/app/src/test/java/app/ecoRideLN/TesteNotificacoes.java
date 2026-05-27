package app.ecoRideLN;

import app.ecoRideCD.sNotificacoes.NotificacoesDAO;
import app.ecoRideLN.sNotificacoes.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Notificações")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteNotificacoes {

    static NotificacoesDAO daoFake() {
        return new NotificacoesDAO() {
            private final Map<Integer, Notificacao> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()                           { return store.size(); }
            @Override public boolean isEmpty()                    { return store.isEmpty(); }
            @Override public boolean containsKey(Object k)        { return store.containsKey(k); }
            @Override public boolean containsValue(Object v)      { return store.containsValue(v); }
            @Override public Notificacao get(Object k)            { return store.get(k); }
            @Override public Notificacao put(Integer k, Notificacao v) { return store.put(k, v); }
            @Override public Notificacao remove(Object k)         { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Notificacao> m) { store.putAll(m); }
            @Override public void clear()                         { store.clear(); }
            @Override public Set<Integer> keySet()                { return store.keySet(); }
            @Override public Collection<Notificacao> values()     { return store.values(); }
            @Override public Set<Map.Entry<Integer, Notificacao>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Notificacao n) {
                int id = seq.getAndIncrement();
                n.setId(id);
                store.put(id, n);
                return id;
            }

            @Override
            public List<Notificacao> getByDestinatario(int idDest) {
                return store.values().stream()
                        .filter(n -> n.getId_destinatario() == idDest)
                        .collect(Collectors.toList());
            }
        };
    }

    private SNotificacoesFacade facade;

    @BeforeEach
    void setUp() {
        facade = new SNotificacoesFacade(daoFake());
    }

    // ── Inserção — NotificacaoOS ──────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registarOS: notificação OS criada para cada destinatário")
    void registarOS_valido() {
        facade.registarNotificacaoOS("OS 1 pronta", 1, List.of(10, 11, 12), 42);
        List<Notificacao> notifs = facade.obterNotificacoesPorDestinatario(10);
        assertEquals(1, notifs.size());
        assertInstanceOf(NotificacaoOS.class, notifs.get(0));
        assertEquals(42, ((NotificacaoOS) notifs.get(0)).getId_os());
    }

    @Test @Order(2) @DisplayName("registarOS: múltiplos destinatários geram N notificações")
    void registarOS_multiplosDestinatarios() {
        facade.registarNotificacaoOS("Msg", 1, List.of(5, 6, 7), 1);
        assertEquals(1, facade.obterNotificacoesPorDestinatario(5).size());
        assertEquals(1, facade.obterNotificacoesPorDestinatario(6).size());
        assertEquals(1, facade.obterNotificacoesPorDestinatario(7).size());
    }

    @Test @Order(3) @DisplayName("registarOS: descrição vazia lança exceção")
    void registarOS_descricaoVazia() {
        assertThrows(IllegalArgumentException.class, () ->
                facade.registarNotificacaoOS("", 1, List.of(10), 1));
    }

    @Test @Order(4) @DisplayName("registarOS: lista de destinatários vazia lança exceção")
    void registarOS_semDestinatarios() {
        assertThrows(IllegalArgumentException.class, () ->
                facade.registarNotificacaoOS("Msg", 1, List.of(), 1));
    }

    // ── Inserção — NotificacaoStock ───────────────────────────────────────────

    @Test @Order(5) @DisplayName("registarStock: notificação Stock criada com id_peca correto")
    void registarStock_valido() {
        facade.registarNotificacaoStock("Stock baixo", 2, List.of(20), 99);
        List<Notificacao> notifs = facade.obterNotificacoesPorDestinatario(20);
        assertEquals(1, notifs.size());
        assertInstanceOf(NotificacaoStock.class, notifs.get(0));
        assertEquals(99, ((NotificacaoStock) notifs.get(0)).getId_peca());
    }

    @Test @Order(6) @DisplayName("registarStock: descrição nula lança exceção")
    void registarStock_descricaoNula() {
        assertThrows(IllegalArgumentException.class, () ->
                facade.registarNotificacaoStock(null, 1, List.of(10), 5));
    }

    // ── Estado ────────────────────────────────────────────────────────────────

    @Test @Order(7) @DisplayName("nova notificação começa em estado NAOLIDA")
    void estadoInicial_naolida() {
        facade.registarNotificacaoOS("Nova", 1, List.of(30), 1);
        Notificacao n = facade.obterNotificacoesPorDestinatario(30).get(0);
        assertEquals(EstadoNotificacao.NAOLIDA, n.getEstado());
    }

    @Test @Order(8) @DisplayName("sinalizarComoLida: estado muda para LIDA")
    void sinalizarLida() {
        facade.registarNotificacaoOS("Ler", 1, List.of(31), 1);
        Notificacao n = facade.obterNotificacoesPorDestinatario(31).get(0);
        assertTrue(facade.sinalizarNotificacao_comoLida(n.getId(), 31));
        assertEquals(EstadoNotificacao.LIDA, facade.obterNotificacoesPorDestinatario(31).get(0).getEstado());
    }

    @Test @Order(9) @DisplayName("sinalizarComoTratada: estado muda para TRATADA")
    void sinalizarTratada() {
        facade.registarNotificacaoOS("Tratar", 1, List.of(32), 2);
        Notificacao n = facade.obterNotificacoesPorDestinatario(32).get(0);
        assertTrue(facade.sinalizarNotificacao_comoTratada(n.getId(), 32));
        assertEquals(EstadoNotificacao.TRATADA, facade.obterNotificacoesPorDestinatario(32).get(0).getEstado());
    }

    @Test @Order(10) @DisplayName("sinalizarLida: utilizador errado devolve false")
    void sinalizarLida_utilizadorErrado() {
        facade.registarNotificacaoOS("Msg", 1, List.of(40), 1);
        Notificacao n = facade.obterNotificacoesPorDestinatario(40).get(0);
        assertFalse(facade.sinalizarNotificacao_comoLida(n.getId(), 99));
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(11) @DisplayName("remover: notificação do próprio destinatário é removida")
    void remover_valido() {
        facade.registarNotificacaoOS("Del", 1, List.of(50), 3);
        Notificacao n = facade.obterNotificacoesPorDestinatario(50).get(0);
        assertTrue(facade.removerNotificacao(n.getId(), 50));
        assertTrue(facade.obterNotificacoesPorDestinatario(50).isEmpty());
    }

    @Test @Order(12) @DisplayName("remover: utilizador errado não remove")
    void remover_utilizadorErrado() {
        facade.registarNotificacaoOS("Del2", 1, List.of(51), 4);
        Notificacao n = facade.obterNotificacoesPorDestinatario(51).get(0);
        assertFalse(facade.removerNotificacao(n.getId(), 99));
        assertEquals(1, facade.obterNotificacoesPorDestinatario(51).size());
    }

    // ── Estrutura final ───────────────────────────────────────────────────────

    @Test @Order(13) @DisplayName("estruturaFinal: criar, ler, tratar e remover notificações")
    void estruturaFinal() {
        facade.registarNotificacaoOS("OS completa", 1, List.of(60, 61), 10);
        facade.registarNotificacaoStock("Stock mínimo", 2, List.of(60), 5);

        List<Notificacao> notifs60 = facade.obterNotificacoesPorDestinatario(60);
        assertEquals(2, notifs60.size());

        Notificacao os = notifs60.stream().filter(n -> n instanceof NotificacaoOS).findFirst().orElseThrow();
        facade.sinalizarNotificacao_comoLida(os.getId(), 60);
        assertEquals(EstadoNotificacao.LIDA, facade.obterNotificacoesPorDestinatario(60).stream()
                .filter(n -> n.getId() == os.getId()).findFirst().orElseThrow().getEstado());

        facade.sinalizarNotificacao_comoTratada(os.getId(), 60);
        assertEquals(EstadoNotificacao.TRATADA, facade.obterNotificacoesPorDestinatario(60).stream()
                .filter(n -> n.getId() == os.getId()).findFirst().orElseThrow().getEstado());

        facade.removerNotificacao(os.getId(), 60);
        assertEquals(1, facade.obterNotificacoesPorDestinatario(60).size());
    }
}
