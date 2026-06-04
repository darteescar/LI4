package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.SAutenticacaoFacade;
import app.ecoRideLN.sAutenticacao.Utilizador;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Autenticação")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteAutenticacao {

    static UtilizadorDAO daoFake() {
        return new UtilizadorDAO() {
            private final Map<Integer, Utilizador> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()                          { return store.size(); }
            @Override public boolean isEmpty()                   { return store.isEmpty(); }
            @Override public boolean containsKey(Object k)       { return store.containsKey(k); }
            @Override public boolean containsValue(Object v)     { return store.containsValue(v); }
            @Override public Utilizador get(Object k)            { return store.get(k); }
            @Override public Utilizador put(Integer k, Utilizador v) { return store.put(k, v); }
            @Override public Utilizador remove(Object k)         { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Utilizador> m) { store.putAll(m); }
            @Override public void clear()                        { store.clear(); }
            @Override public Set<Integer> keySet()               { return store.keySet(); }
            @Override public Collection<Utilizador> values()     { return store.values(); }
            @Override public Set<Map.Entry<Integer, Utilizador>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Utilizador u) {
                int id = seq.getAndIncrement();
                u.setId(id);
                store.put(id, u);
                return id;
            }

            @Override
            public boolean existeIdentificador(String identificador) {
                return store.values().stream().anyMatch(u -> u.getIdentificador().equals(identificador));
            }

            @Override
            public Utilizador getByIdentificador(String identificador) {
                return store.values().stream()
                        .filter(u -> u.getIdentificador().equals(identificador))
                        .findFirst().orElse(null);
            }

            @Override
            public void updatePassword(int id, String novaPassword) {
                Utilizador u = store.get(id);
                if (u != null) u.setPassword(novaPassword);
            }

            @Override
            public int getIdByIdFuncionario(int idFuncionario) {
                return store.values().stream()
                        .filter(u -> u.getIdFuncionario() == idFuncionario)
                        .mapToInt(Utilizador::getId)
                        .findFirst().orElse(-1);
            }
        };
    }

    private SAutenticacaoFacade facade;

    @BeforeEach
    void setUp() {
        facade = new SAutenticacaoFacade(daoFake());
    }

    private Utilizador criarUtilizador() {
        return facade.registarUtilizador("pass123", 1, Cargo.Mecanico, "mecanico1");
    }

    // ── Inserção ─────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registar: utilizador válido é criado com ID positivo")
    void registar_valido() {
        Utilizador u = criarUtilizador();
        assertNotNull(u);
        assertTrue(u.getId() > 0);
        assertEquals("mecanico1", u.getIdentificador());
        assertEquals(Cargo.Mecanico, u.getCargo());
    }

    @Test @Order(2) @DisplayName("registar: password vazia lança exceção")
    void registar_passwordVazia() {
        assertThrows(EcoRideException.class, () ->
                facade.registarUtilizador("", 1, Cargo.Mecanico, "user1"));
    }

    @Test @Order(3) @DisplayName("registar: cargo nulo lança exceção")
    void registar_cargoNulo() {
        assertThrows(EcoRideException.class, () ->
                facade.registarUtilizador("pass", 1, null, "user1"));
    }

    @Test @Order(4) @DisplayName("registar: identificador vazio lança exceção")
    void registar_identificadorVazio() {
        assertThrows(EcoRideException.class, () ->
                facade.registarUtilizador("pass", 1, Cargo.Gerente, ""));
    }

    @Test @Order(5) @DisplayName("registar: identificador duplicado lança exceção")
    void registar_identificadorDuplicado() {
        criarUtilizador();
        assertThrows(EcoRideException.class, () ->
                facade.registarUtilizador("outrapass", 2, Cargo.Gerente, "mecanico1"));
    }

    @Test @Order(6) @DisplayName("registar: todos os cargos são aceites")
    void registar_todosCargos() {
        int i = 10;
        for (Cargo c : Cargo.values()) {
            Utilizador u = facade.registarUtilizador("pass", i, c, "user" + i);
            assertEquals(c, u.getCargo());
            i++;
        }
    }

    // ── Obter / Existir ───────────────────────────────────────────────────────

    @Test @Order(7) @DisplayName("obter: utilizador existente é devolvido")
    void obter_existente() {
        Utilizador u = criarUtilizador();
        Utilizador obtido = facade.obterUtilizador(u.getId());
        assertEquals(u.getId(), obtido.getId());
    }

    @Test @Order(8) @DisplayName("obter: ID inexistente lança exceção")
    void obter_inexistente() {
        assertThrows(EcoRideException.class, () -> facade.obterUtilizador(999));
    }

    @Test @Order(9) @DisplayName("existeUtilizador: true para existente, false para inexistente")
    void existeUtilizador() {
        Utilizador u = criarUtilizador();
        assertTrue(facade.existeUtilizador(u.getId()));
        assertFalse(facade.existeUtilizador(999));
    }

    // ── Atualização ───────────────────────────────────────────────────────────

    @Test @Order(10) @DisplayName("atualizar: cargo e identificador são alterados")
    void atualizar_valido() {
        Utilizador u = criarUtilizador();
        facade.atualizarUtilizador(u.getId(), 1, Cargo.Gerente, "gerente1");
        Utilizador atualizado = facade.obterUtilizador(u.getId());
        assertEquals(Cargo.Gerente, atualizado.getCargo());
        assertEquals("gerente1", atualizado.getIdentificador());
    }

    @Test @Order(11) @DisplayName("atualizar: ID inexistente lança exceção")
    void atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarUtilizador(999, 1, Cargo.Gerente, "x"));
    }

    @Test @Order(12) @DisplayName("atualizar: identificador duplicado lança exceção")
    void atualizar_identificadorDuplicado() {
        Utilizador u1 = criarUtilizador();
        Utilizador u2 = facade.registarUtilizador("pass2", 2, Cargo.Gerente, "gerente2");
        assertThrows(EcoRideException.class, () ->
                facade.atualizarUtilizador(u2.getId(), 2, Cargo.Gerente, u1.getIdentificador()));
    }

    // ── Password ──────────────────────────────────────────────────────────────

    @Test @Order(13) @DisplayName("autenticar: password correta devolve true")
    void autenticar_correto() {
        Utilizador u = criarUtilizador();
        assertTrue(facade.autenticar(u.getId(), "pass123"));
    }

    @Test @Order(14) @DisplayName("autenticar: password errada devolve false")
    void autenticar_errado() {
        Utilizador u = criarUtilizador();
        assertFalse(facade.autenticar(u.getId(), "errada"));
    }

    @Test @Order(15) @DisplayName("atualizarPassword: password antiga incorreta lança exceção")
    void atualizarPassword_antigaErrada() {
        Utilizador u = criarUtilizador();
        assertThrows(EcoRideException.class, () ->
                facade.atualizarPalavraPasseUtilizador(u.getId(), "errada", "nova123"));
    }

    @Test @Order(16) @DisplayName("atualizarPassword: password válida é alterada com sucesso")
    void atualizarPassword_valida() {
        Utilizador u = criarUtilizador();
        assertTrue(facade.atualizarPalavraPasseUtilizador(u.getId(), "pass123", "nova123"));
        assertTrue(facade.autenticar(u.getId(), "nova123"));
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(17) @DisplayName("remover: utilizador existente é removido")
    void remover_valido() {
        Utilizador u = criarUtilizador();
        assertTrue(facade.removerUtilizador(u.getId()));
        assertFalse(facade.existeUtilizador(u.getId()));
    }

    @Test @Order(18) @DisplayName("remover: ID inexistente lança exceção")
    void remover_inexistente() {
        assertThrows(EcoRideException.class, () -> facade.removerUtilizador(999));
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    @Test @Order(19) @DisplayName("obterPorCargo: devolve apenas utilizadores com o cargo indicado")
    void obterPorCargo() {
        facade.registarUtilizador("p1", 1, Cargo.Mecanico, "mec1");
        facade.registarUtilizador("p2", 2, Cargo.Gerente, "ger1");
        facade.registarUtilizador("p3", 3, Cargo.Mecanico, "mec2");
        List<Integer> mecanicos = facade.obterUtilizadoresPorCargo(Cargo.Mecanico);
        assertEquals(2, mecanicos.size());
    }

    @Test @Order(20) @DisplayName("estruturaFinal: fluxo completo registar → atualizar → alterar password → remover")
    void estruturaFinal() {
        Utilizador u = facade.registarUtilizador("pass1", 5, Cargo.GestorStock, "gestor5");
        assertTrue(u.getId() > 0);

        facade.atualizarUtilizador(u.getId(), 5, Cargo.Secretaria, "secretaria5");
        assertEquals(Cargo.Secretaria, facade.obterUtilizador(u.getId()).getCargo());

        facade.atualizarPalavraPasseUtilizador(u.getId(), "pass1", "novaPass");
        assertTrue(facade.autenticar(u.getId(), "novaPass"));

        facade.removerUtilizador(u.getId());
        assertFalse(facade.existeUtilizador(u.getId()));
    }
}
