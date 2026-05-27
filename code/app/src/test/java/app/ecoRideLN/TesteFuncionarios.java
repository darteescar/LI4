package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sFuncionarios.FuncionarioDAO;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sFuncionarios.SFuncionariosFacade;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para SFuncionariosFacade.
 * Usa FuncionarioDAO em memória (HashMap) injetado via construtor package-protected.
 */
@DisplayName("Funcionários")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteFuncionarios {

    // ── DAO falso em memória ──────────────────────────────────────────────────

    static FuncionarioDAO daoFake() {
        return new FuncionarioDAO() {
            private final Map<Integer, Funcionario> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()    { return store.size(); }
            @Override public boolean isEmpty() { return store.isEmpty(); }
            @Override public boolean containsKey(Object k) { return store.containsKey(k); }
            @Override public boolean containsValue(Object v) { return store.containsValue(v); }
            @Override public Funcionario get(Object k) { return store.get(k); }
            @Override public Funcionario put(Integer k, Funcionario v) { return store.put(k, v); }
            @Override public Funcionario remove(Object k) { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends Funcionario> m) { store.putAll(m); }
            @Override public void clear() { store.clear(); }
            @Override public Set<Integer> keySet() { return store.keySet(); }
            @Override public Collection<Funcionario> values() { return store.values(); }
            @Override public Set<Map.Entry<Integer, Funcionario>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(Funcionario f) {
                int id = seq.getAndIncrement();
                f.setId(id);
                store.put(id, f);
                return id;
            }
        };
    }

    // ── Dados de fixture ──────────────────────────────────────────────────────

    private SFuncionariosFacade facade;

    private static final String NOME  = "Ana Ferreira";
    private static final String TEL   = "912345678";
    private static final String EMAIL = "ana@empresa.pt";
    private static final LocalDate DN = LocalDate.of(1990, 5, 15);
    private static final String NISS  = "12345678901";
    private static final String NIF   = "123456789";
    private static final String NUS   = "123456789";
    private static final String IBAN  = "PT50000201231234567890154";
    private static final float  SH    = 15.0f;
    private static final float  SL    = 1200.0f;
    private static final float  SB    = 1500.0f;
    private static final String PORTA = "12B";
    private static final String RUA   = "Rua das Flores";
    private static final String LOCAL = "Braga";
    private static final String CP    = "4710-057";

    @BeforeEach
    void setUp() { facade = new SFuncionariosFacade(daoFake()); }

    private Funcionario registarValido() {
        return facade.registarFuncionario(NOME, TEL, EMAIL, DN, NISS, NIF, NUS, IBAN,
                SH, SL, SB, 0, PORTA, RUA, LOCAL, CP);
    }

    // ── Inserção ──────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registar: funcionário válido criado com sucesso")
    void registar_valido() {
        Funcionario f = registarValido();
        assertNotNull(f);
        assertEquals(NOME, f.getNome());
        assertEquals(EMAIL, f.getEmail());
        assertEquals(NIF, f.getNIF());
        assertTrue(f.getId() > 0);
    }

    @Test @Order(2) @DisplayName("registar: dois funcionários têm IDs distintos")
    void registar_idsDistintos() {
        Funcionario f1 = registarValido();
        Funcionario f2 = facade.registarFuncionario("Bruno", "919999999", "b@emp.pt", DN,
                "11111111111", "111111111", "111111111", "PT50000201231234567890155",
                SH, SL, SB, 0, "1", RUA, LOCAL, CP);
        assertNotEquals(f1.getId(), f2.getId());
    }

    @Test @Order(3) @DisplayName("registar: email inválido lança EcoRideException")
    void registar_emailInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, "semArroba", DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(4) @DisplayName("registar: telemóvel com 8 dígitos lança exceção")
    void registar_telInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, "91234567", EMAIL, DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(5) @DisplayName("registar: NIF com 8 dígitos lança exceção")
    void registar_nifInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        NISS, "12345678", NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(6) @DisplayName("registar: NUS com 8 dígitos lança exceção")
    void registar_nusInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        NISS, NIF, "12345678", IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(7) @DisplayName("registar: NISS com 10 dígitos lança exceção")
    void registar_nissInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        "1234567890", NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(8) @DisplayName("registar: IBAN com prefixo minúsculo lança exceção")
    void registar_ibanInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        NISS, NIF, NUS, "pt50000201231234567890154", SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(9) @DisplayName("registar: salário zero lança exceção")
    void registar_salarioZero() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        NISS, NIF, NUS, IBAN, 0f, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(10) @DisplayName("registar: código postal inválido lança exceção")
    void registar_cpInvalido() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, "4710057"));
    }

    @Test @Order(11) @DisplayName("registar: data de nascimento nula lança exceção")
    void registar_dataNula() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario(NOME, TEL, EMAIL, null,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(12) @DisplayName("registar: nome vazio lança exceção")
    void registar_nomeVazio() {
        assertThrows(EcoRideException.class, () ->
                facade.registarFuncionario("", TEL, EMAIL, DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    // ── Atualização ───────────────────────────────────────────────────────────

    @Test @Order(13) @DisplayName("atualizar: dados válidos atualizam corretamente")
    void atualizar_valido() {
        Funcionario f = registarValido();
        Funcionario atualizado = facade.atualizarFuncionario(f.getId(), "Ana Costa",
                TEL, EMAIL, DN, NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP);
        assertEquals("Ana Costa", atualizado.getNome());
        assertEquals(f.getId(), atualizado.getId());
    }

    @Test @Order(14) @DisplayName("atualizar: ID inexistente lança EcoRideException")
    void atualizar_inexistente() {
        assertThrows(EcoRideException.class, () ->
                facade.atualizarFuncionario(999, NOME, TEL, EMAIL, DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
    }

    @Test @Order(15) @DisplayName("atualizar: email inválido preserva dados originais")
    void atualizar_emailInvalido() {
        Funcionario f = registarValido();
        assertThrows(EcoRideException.class, () ->
                facade.atualizarFuncionario(f.getId(), NOME, TEL, "invalido", DN,
                        NISS, NIF, NUS, IBAN, SH, SL, SB, 0, PORTA, RUA, LOCAL, CP));
        assertEquals(EMAIL, facade.obterFuncionario(f.getId()).getEmail());
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(16) @DisplayName("remover: funcionário existente é removido")
    void remover_existente() {
        Funcionario f = registarValido();
        assertTrue(facade.removerFuncionario(f.getId()));
        assertNull(facade.obterFuncionario(f.getId()));
    }

    @Test @Order(17) @DisplayName("remover: ID inexistente devolve false")
    void remover_inexistente() {
        assertFalse(facade.removerFuncionario(999));
    }

    // ── Horas extra ───────────────────────────────────────────────────────────

    @Test @Order(18) @DisplayName("adicionarHorasExtra: acumula corretamente")
    void horasExtra_acumula() {
        Funcionario f = registarValido();
        facade.adicionarHorasExtra(f.getId(), 5);
        facade.adicionarHorasExtra(f.getId(), 3);
        assertEquals(8, facade.obterFuncionario(f.getId()).getHoras_extra());
    }

    @Test @Order(19) @DisplayName("adicionarHorasExtra: valor negativo lança exceção")
    void horasExtra_negativo() {
        Funcionario f = registarValido();
        assertThrows(EcoRideException.class, () -> facade.adicionarHorasExtra(f.getId(), -1));
    }

    // ── Estado final ──────────────────────────────────────────────────────────

    @Test @Order(20) @DisplayName("estrutura final: listar devolve todos os funcionários registados")
    void estruturaFinal() {
        registarValido();
        facade.registarFuncionario("Bruno", "919999999", "b@emp.pt", DN,
                "11111111111", "111111111", "111111111", "PT50000201231234567890155",
                SH, SL, SB, 0, "1", RUA, LOCAL, CP);
        assertEquals(2, facade.obterFuncionarios().size());
    }
}
