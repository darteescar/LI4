package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.SClientesFacade;
import app.ecoRideLN.sClientes.Trotinete;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para SClientesFacade.
 * Usa ClienteDAO e TrotineteDAO em memória (HashMap) injetados via construtor package-protected.
 */
@DisplayName("Clientes e Trotinetes")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteClientes {

    // ── DAOs falsos em memória ────────────────────────────────────────────────

    static ClienteDAO clienteDaoFake() {
        return new ClienteDAO() {
            private final Map<Integer, Cliente> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()    { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Cliente get(Object k) { return store.get(k); }
            @Override public Cliente put(Integer k, Cliente v) { return store.put(k, v); }
            @Override public Cliente remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Cliente> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Cliente> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Cliente>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Cliente c) {
                int id = seq.getAndIncrement();
                c.setId(id);
                store.put(id, c);
                return id;
            }
        };
    }

    static TrotineteDAO trotineteDAOFake() {
        return new TrotineteDAO() {
            private final Map<Integer, Trotinete> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()    { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Trotinete get(Object k) { return store.get(k); }
            @Override public Trotinete put(Integer k, Trotinete v) { return store.put(k, v); }
            @Override public Trotinete remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Trotinete> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Trotinete> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Trotinete>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Trotinete t) {
                int id = seq.getAndIncrement();
                t.setId(id);
                store.put(id, t);
                return id;
            }
        };
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private SClientesFacade facade;

    @BeforeEach
    void setUp() { facade = new SClientesFacade(clienteDaoFake(), trotineteDAOFake()); }

    private Cliente registarCliente() {
        return facade.registarCliente("João Silva", "joao@exemplo.pt", "912345678", "123456789");
    }

    // ── Cliente: inserção ─────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registarCliente: dados válidos criam cliente")
    void cliente_registar_valido() {
        Cliente c = registarCliente();
        assertNotNull(c);
        assertEquals("João Silva", c.getNome());
        assertTrue(c.getId() > 0);
    }

    @Test @Order(2) @DisplayName("registarCliente: email inválido lança exceção")
    void cliente_registar_emailInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarCliente("João", "emailInvalido", "912345678", "123456789"));
    }

    @Test @Order(3) @DisplayName("registarCliente: NIF com 8 dígitos lança exceção")
    void cliente_registar_nifInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarCliente("João", "j@j.pt", "912345678", "12345678"));
    }

    @Test @Order(4) @DisplayName("registarCliente: telemóvel com letras lança exceção")
    void cliente_registar_telInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarCliente("João", "j@j.pt", "91234A678", "123456789"));
    }

    @Test @Order(5) @DisplayName("registarCliente: nome vazio lança exceção")
    void cliente_registar_nomeVazio() {
        assertThrows(EcoRideException.class, () ->
                facade.registarCliente("", "j@j.pt", "912345678", "123456789"));
    }

    // ── Cliente: atualização ──────────────────────────────────────────────────

    @Test @Order(6) @DisplayName("atualizarCliente: atualiza campos corretamente")
    void cliente_atualizar_valido() {
        Cliente c = registarCliente();
        facade.atualizarCliente(c.getId(), "João Costa", "jcosta@exemplo.pt", "912345678", "123456789");
        Cliente atualizado = facade.obterCliente(c.getId());
        assertEquals("João Costa", atualizado.getNome());
        assertEquals("jcosta@exemplo.pt", atualizado.getEmail());
    }

    @Test @Order(7) @DisplayName("atualizarCliente: ID inexistente lança exceção")
    void cliente_atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarCliente(999, "Nome", "a@b.pt", "912345678", "123456789"));
    }

    @Test @Order(8) @DisplayName("atualizarCliente: email inválido preserva dados originais")
    void cliente_atualizar_emailInvalido() {
        Cliente c = registarCliente();
        assertThrows(EcoRideException.class, () ->
                facade.atualizarCliente(c.getId(), "João", "invalido", "912345678", "123456789"));
        assertEquals("joao@exemplo.pt", facade.obterCliente(c.getId()).getEmail());
    }

    // ── Cliente: remoção ──────────────────────────────────────────────────────

    @Test @Order(9) @DisplayName("removerCliente: remove corretamente")
    void cliente_remover() {
        Cliente c = registarCliente();
        assertTrue(facade.removerCliente(c.getId()));
        assertNull(facade.obterCliente(c.getId()));
    }

    @Test @Order(10) @DisplayName("removerCliente: ID inexistente devolve false")
    void cliente_remover_inexistente() {
        assertFalse(facade.removerCliente(999));
    }

    // ── Trotinete: inserção ───────────────────────────────────────────────────

    @Test @Order(11) @DisplayName("registarTrotinete: cria e associa ao cliente")
    void trotinete_registar_valido() {
        Cliente c = registarCliente();
        Trotinete t = facade.registarTrotinete(c.getId(), "City Rider", "Xiaomi", "SN001", "Elétrico");
        assertNotNull(t);
        assertTrue(t.getId() > 0);
        assertEquals(c.getId(), t.getCod_cliente());
    }

    @Test @Order(12) @DisplayName("registarTrotinete: cliente inexistente lança exceção")
    void trotinete_registar_clienteInexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.registarTrotinete(999, "City", "Xiaomi", "SN001", "Elétrico"));
    }

    @Test @Order(13) @DisplayName("registarTrotinete: modelo vazio lança exceção")
    void trotinete_registar_modeloVazio() {
        Cliente c = registarCliente();
        assertThrows(EcoRideException.class, () ->
                facade.registarTrotinete(c.getId(), "", "Xiaomi", "SN001", "Elétrico"));
    }

    @Test @Order(14) @DisplayName("registarTrotinete: número de série vazio lança exceção")
    void trotinete_registar_numSerieVazio() {
        Cliente c = registarCliente();
        assertThrows(EcoRideException.class, () ->
                facade.registarTrotinete(c.getId(), "City", "Xiaomi", "", "Elétrico"));
    }

    // ── Trotinete: atualização ────────────────────────────────────────────────

    @Test @Order(15) @DisplayName("atualizarTrotinete: dados válidos atualizam corretamente")
    void trotinete_atualizar_valido() {
        Cliente c = registarCliente();
        Trotinete t = facade.registarTrotinete(c.getId(), "City", "Xiaomi", "SN001", "Elétrico");
        facade.atualizarTrotinete(t.getId(), c.getId(), "Urban Pro", "Segway", "SN002", "Elétrico");
        assertEquals("Urban Pro", facade.obterTrotinete(t.getId()).getModelo());
        assertEquals("Segway", facade.obterTrotinete(t.getId()).getMarca());
    }

    @Test @Order(16) @DisplayName("atualizarTrotinete: ID inexistente lança exceção")
    void trotinete_atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarTrotinete(999, 1, "City", "Xiaomi", "SN001", "Elétrico"));
    }

    // ── Trotinete: remoção ────────────────────────────────────────────────────

    @Test @Order(17) @DisplayName("removerTrotinete: remove corretamente")
    void trotinete_remover() {
        Cliente c = registarCliente();
        Trotinete t = facade.registarTrotinete(c.getId(), "City", "Xiaomi", "SN001", "Elétrico");
        assertTrue(facade.removerTrotinete(t.getId()));
        assertNull(facade.obterTrotinete(t.getId()));
    }

    // ── Estado final ──────────────────────────────────────────────────────────

    @Test @Order(18) @DisplayName("estrutura final: listar clientes devolve todos os registados")
    void estruturaFinal() {
        registarCliente();
        facade.registarCliente("Maria", "m@m.pt", "919999999", "987654321");
        assertEquals(2, facade.obterClientes().size());
    }
}
