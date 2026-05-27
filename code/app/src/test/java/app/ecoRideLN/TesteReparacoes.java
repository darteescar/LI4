package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reparações")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteReparacoes {

    static ReparacaoDAO daoFake() {
        return new ReparacaoDAO() {
            private final Map<Integer, Reparacao> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()                         { return store.size(); }
            @Override public boolean isEmpty()                  { return store.isEmpty(); }
            @Override public boolean containsKey(Object k)      { return store.containsKey(k); }
            @Override public boolean containsValue(Object v)    { return store.containsValue(v); }
            @Override public Reparacao get(Object k)            { return store.get(k); }
            @Override public Reparacao put(Integer k, Reparacao v) { return store.put(k, v); }
            @Override public Reparacao remove(Object k)         { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Reparacao> m) { store.putAll(m); }
            @Override public void clear()                       { store.clear(); }
            @Override public Set<Integer> keySet()              { return store.keySet(); }
            @Override public Collection<Reparacao> values()     { return store.values(); }
            @Override public Set<Map.Entry<Integer, Reparacao>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Reparacao r) {
                int id = seq.getAndIncrement();
                r.setId(id);
                store.put(id, r);
                return id;
            }
        };
    }

    private SReparacoesFacade facade;

    @BeforeEach
    void setUp() {
        facade = new SReparacoesFacade(daoFake());
    }

    private Reparacao criarReparacao() {
        return facade.registarReparacao("Troca de pneu", "Substituição do pneu dianteiro", 25.0f, true);
    }

    // ── Inserção ─────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registar: reparação válida é criada com ID positivo")
    void registar_valido() {
        Reparacao r = criarReparacao();
        assertNotNull(r);
        assertTrue(r.getId() > 0);
        assertEquals("Troca de pneu", r.getNomenclatura());
        assertEquals(25.0f, r.getPreco());
        assertTrue(r.isDisponivel());
    }

    @Test @Order(2) @DisplayName("registar: nomenclatura vazia lança exceção")
    void registar_nomenclaturaVazia() {
        assertThrows(EcoRideException.class, () ->
                facade.registarReparacao("", "Desc", 10f, true));
    }

    @Test @Order(3) @DisplayName("registar: nomenclatura nula lança exceção")
    void registar_nomenclaturaNula() {
        assertThrows(EcoRideException.class, () ->
                facade.registarReparacao(null, "Desc", 10f, true));
    }

    @Test @Order(4) @DisplayName("registar: descrição vazia lança exceção")
    void registar_descricaoVazia() {
        assertThrows(EcoRideException.class, () ->
                facade.registarReparacao("Nomen", "", 10f, true));
    }

    @Test @Order(5) @DisplayName("registar: preço negativo lança exceção")
    void registar_precoNegativo() {
        assertThrows(EcoRideException.class, () ->
                facade.registarReparacao("Nomen", "Desc", -1f, true));
    }

    @Test @Order(6) @DisplayName("registar: preço zero é aceite")
    void registar_precoZero() {
        Reparacao r = facade.registarReparacao("Limpeza", "Limpeza geral", 0f, true);
        assertEquals(0f, r.getPreco());
    }

    @Test @Order(7) @DisplayName("registar: reparação inativa é criada corretamente")
    void registar_inativa() {
        Reparacao r = facade.registarReparacao("Revisão", "Revisão completa", 50f, false);
        assertFalse(r.isDisponivel());
    }

    // ── Obter / Existir ───────────────────────────────────────────────────────

    @Test @Order(8) @DisplayName("obter: reparação existente é devolvida")
    void obter_existente() {
        Reparacao r = criarReparacao();
        Reparacao obtida = facade.obterReparacao(r.getId());
        assertNotNull(obtida);
        assertEquals(r.getId(), obtida.getId());
    }

    @Test @Order(9) @DisplayName("obter: ID inexistente devolve null")
    void obter_inexistente() {
        assertNull(facade.obterReparacao(999));
    }

    @Test @Order(10) @DisplayName("existeReparacao: true para existente, false para inexistente")
    void existeReparacao() {
        Reparacao r = criarReparacao();
        assertTrue(facade.existeReparacao(r.getId()));
        assertFalse(facade.existeReparacao(999));
    }

    @Test @Order(11) @DisplayName("obterDisponiveis: devolve apenas reparações disponíveis")
    void obterDisponiveis() {
        facade.registarReparacao("R1", "Desc", 10f, true);
        facade.registarReparacao("R2", "Desc", 20f, false);
        facade.registarReparacao("R3", "Desc", 30f, true);
        List<Reparacao> disponiveis = facade.obterReparacoesDisponiveis();
        assertEquals(2, disponiveis.size());
        assertTrue(disponiveis.stream().allMatch(Reparacao::isDisponivel));
    }

    // ── Atualização ───────────────────────────────────────────────────────────

    @Test @Order(12) @DisplayName("atualizar: campos são alterados corretamente")
    void atualizar_valido() {
        Reparacao r = criarReparacao();
        facade.atualizarReparacao(r.getId(), "Nova nomen", "Nova desc", 99f, false);
        Reparacao atualizada = facade.obterReparacao(r.getId());
        assertEquals("Nova nomen", atualizada.getNomenclatura());
        assertEquals("Nova desc", atualizada.getDescricao());
        assertEquals(99f, atualizada.getPreco());
        assertFalse(atualizada.isDisponivel());
    }

    @Test @Order(13) @DisplayName("atualizar: ID inexistente lança exceção")
    void atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarReparacao(999, "N", "D", 10f, true));
    }

    @Test @Order(14) @DisplayName("atualizar: nomenclatura vazia lança exceção")
    void atualizar_nomenclaturaVazia() {
        Reparacao r = criarReparacao();
        assertThrows(EcoRideException.class, () ->
                facade.atualizarReparacao(r.getId(), "", "Desc", 10f, true));
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(15) @DisplayName("remover: reparação existente é removida")
    void remover_valido() {
        Reparacao r = criarReparacao();
        assertTrue(facade.removerReparacao(r.getId()));
        assertNull(facade.obterReparacao(r.getId()));
    }

    @Test @Order(16) @DisplayName("remover: ID inexistente devolve false")
    void remover_inexistente() {
        assertFalse(facade.removerReparacao(999));
    }

    // ── Estrutura final ───────────────────────────────────────────────────────

    @Test @Order(17) @DisplayName("estruturaFinal: fluxo completo registar → listar → atualizar → remover")
    void estruturaFinal() {
        Reparacao r1 = facade.registarReparacao("Pastilhas", "Troca pastilhas", 40f, true);
        Reparacao r2 = facade.registarReparacao("Bateria", "Substituição bateria", 80f, false);

        assertEquals(2, facade.obterReparacoes().size());
        assertEquals(1, facade.obterReparacoesDisponiveis().size());

        facade.atualizarReparacao(r2.getId(), "Bateria Pro", "Bateria premium", 120f, true);
        assertEquals(2, facade.obterReparacoesDisponiveis().size());

        facade.removerReparacao(r1.getId());
        assertEquals(1, facade.obterReparacoes().size());
        assertFalse(facade.existeReparacao(r1.getId()));
    }
}
