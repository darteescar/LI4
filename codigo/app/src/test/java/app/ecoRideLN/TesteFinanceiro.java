package app.ecoRideLN;

import app.common.EcoRideException;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;
import app.ecoRideLN.sFinanceiro.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Financeiro")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TesteFinanceiro {

    static MovimentoFinanceiroDAO daoFake() {
        return new MovimentoFinanceiroDAO() {
            private final Map<Integer, MovimentoFinanceiro> store = new HashMap<>();
            private final AtomicInteger seq = new AtomicInteger(1);

            @Override public int size()                                   { return store.size(); }
            @Override public boolean isEmpty()                            { return store.isEmpty(); }
            @Override public boolean containsKey(Object k)                { return store.containsKey(k); }
            @Override public boolean containsValue(Object v)              { return store.containsValue(v); }
            @Override public MovimentoFinanceiro get(Object k)            { return store.get(k); }
            @Override public MovimentoFinanceiro put(Integer k, MovimentoFinanceiro v) { return store.put(k, v); }
            @Override public MovimentoFinanceiro remove(Object k)         { return store.remove(k); }
            @Override public void putAll(Map<? extends Integer, ? extends MovimentoFinanceiro> m) { store.putAll(m); }
            @Override public void clear()                                 { store.clear(); }
            @Override public Set<Integer> keySet()                        { return store.keySet(); }
            @Override public Collection<MovimentoFinanceiro> values()     { return store.values(); }
            @Override public Set<Map.Entry<Integer, MovimentoFinanceiro>> entrySet() { return store.entrySet(); }

            @Override
            public int insert(MovimentoFinanceiro m) {
                int id = seq.getAndIncrement();
                m.setId(id);
                store.put(id, m);
                return id;
            }

            @Override
            public void removeByStock(int codStock) {
                store.entrySet().removeIf(e ->
                        e.getValue() instanceof MovimentoPeca mp && mp.getCodStock() == codStock);
            }
        };
    }

    private SFinanceiroFacade facade;

    @BeforeEach
    void setUp() {
        facade = new SFinanceiroFacade(daoFake());
    }

    // ── Inserção ─────────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("registarCompraStock: movimento GastoPecas criado")
    void registarCompraStock_valido() {
        MovimentoFinanceiro m = facade.registarMovimentoCompraStock(1, 50f, "Compra pneus");
        assertNotNull(m);
        assertTrue(m.getId() > 0);
        assertEquals(TipoMovimento.GastoPecas, m.getTipo());
        assertEquals(50f, m.getValor());
        assertInstanceOf(MovimentoPeca.class, m);
    }

    @Test @Order(2) @DisplayName("registarVendaPeca: movimento LucroVendaPecas criado")
    void registarVendaPeca_valido() {
        MovimentoFinanceiro m = facade.registarMovimentoVendaPeca(2, 80f, "Venda peça");
        assertEquals(TipoMovimento.LucroVendaPecas, m.getTipo());
        assertInstanceOf(MovimentoPeca.class, m);
    }

    @Test @Order(3) @DisplayName("registarPagamentoFuncionario: movimento Salario criado")
    void registarPagamentoFuncionario_valido() {
        MovimentoFinanceiro m = facade.registarMovimentoPagamentoFuncionario(5, 1200f, "Salário março");
        assertEquals(TipoMovimento.Salario, m.getTipo());
        assertInstanceOf(MovimentoFuncionario.class, m);
    }

    @Test @Order(4) @DisplayName("registarReparacaoOS: movimento LucroMaoObra criado")
    void registarReparacaoOS_valido() {
        MovimentoFinanceiro m = facade.registarMovimentoReparacaoOS(3, 200f, "Mão de obra OS 1");
        assertEquals(TipoMovimento.LucroMaoObra, m.getTipo());
        assertInstanceOf(MovimentoReparacao.class, m);
    }

    @Test @Order(5) @DisplayName("registar: descrição vazia lança exceção")
    void registar_descricaoVazia() {
        assertThrows(EcoRideException.class, () ->
                facade.registarMovimentoCompraStock(1, 10f, ""));
    }

    @Test @Order(6) @DisplayName("registar: descrição nula lança exceção")
    void registar_descricaoNula() {
        assertThrows(EcoRideException.class, () ->
                facade.registarMovimentoCompraStock(1, 10f, null));
    }

    @Test @Order(7) @DisplayName("registar: valor negativo lança exceção")
    void registar_valorNegativo() {
        assertThrows(EcoRideException.class, () ->
                facade.registarMovimentoCompraStock(1, -5f, "Desc"));
    }

    @Test @Order(8) @DisplayName("registar: valor zero é aceite")
    void registar_valorZero() {
        MovimentoFinanceiro m = facade.registarMovimentoCompraStock(1, 0f, "Gratuito");
        assertEquals(0f, m.getValor());
    }

    // ── Obter / Existir ───────────────────────────────────────────────────────

    @Test @Order(9) @DisplayName("existeMovimento: true para existente, false para inexistente")
    void existeMovimento() {
        MovimentoFinanceiro m = facade.registarMovimentoCompraStock(1, 10f, "Test");
        assertTrue(facade.existeMovimentoFinanceiro(m.getId()));
        assertFalse(facade.existeMovimentoFinanceiro(999));
    }

    @Test @Order(10) @DisplayName("obterMovimentos: lista contém todos os movimentos inseridos")
    void obterMovimentos() {
        facade.registarMovimentoCompraStock(1, 10f, "A");
        facade.registarMovimentoVendaPeca(2, 20f, "B");
        facade.registarMovimentoPagamentoFuncionario(3, 30f, "C");
        assertEquals(3, facade.obterMovimentos().size());
    }

    // ── Filtro ────────────────────────────────────────────────────────────────

    @Test @Order(11) @DisplayName("filtrar: por tipo devolve só movimentos desse tipo")
    void filtrar_porTipo() {
        facade.registarMovimentoCompraStock(1, 10f, "Compra");
        facade.registarMovimentoVendaPeca(2, 20f, "Venda");
        facade.registarMovimentoCompraStock(3, 30f, "Compra2");
        List<MovimentoFinanceiro> gastos = facade.obterMovimentosFinanceirosFiltrados(null, null, TipoMovimento.GastoPecas);
        assertEquals(2, gastos.size());
        assertTrue(gastos.stream().allMatch(m -> m.getTipo() == TipoMovimento.GastoPecas));
    }

    @Test @Order(12) @DisplayName("filtrar: data 'desde' posterior a 'ate' lança exceção")
    void filtrar_datasInvalidas() {
        LocalDate desde = LocalDate.now().plusDays(1);
        LocalDate ate   = LocalDate.now();
        assertThrows(EcoRideException.class, () ->
                facade.obterMovimentosFinanceirosFiltrados(desde, ate, null));
    }

    @Test @Order(13) @DisplayName("filtrar: sem filtros devolve todos os movimentos")
    void filtrar_semFiltros() {
        facade.registarMovimentoCompraStock(1, 5f, "A");
        facade.registarMovimentoVendaPeca(1, 5f, "B");
        assertEquals(2, facade.obterMovimentosFinanceirosFiltrados(null, null, null).size());
    }

    // ── Remoção ───────────────────────────────────────────────────────────────

    @Test @Order(14) @DisplayName("remover: movimento existente é removido")
    void remover_valido() {
        MovimentoFinanceiro m = facade.registarMovimentoCompraStock(1, 10f, "Del");
        assertTrue(facade.removerMovimentoFinanceiro(m.getId()));
        assertFalse(facade.existeMovimentoFinanceiro(m.getId()));
    }

    @Test @Order(15) @DisplayName("remover: ID inexistente devolve false")
    void remover_inexistente() {
        assertFalse(facade.removerMovimentoFinanceiro(999));
    }

    @Test @Order(16) @DisplayName("removerPorStock: remove todos os MovimentoPeca com esse codStock")
    void removerPorStock() {
        facade.registarMovimentoCompraStock(7, 10f, "Stock 7");
        facade.registarMovimentoCompraStock(7, 20f, "Stock 7 B");
        facade.registarMovimentoCompraStock(8, 30f, "Stock 8");
        assertEquals(3, facade.obterMovimentos().size());
        facade.removerMovimentosFinanceirosPorStock(7);
        List<MovimentoFinanceiro> restantes = facade.obterMovimentos();
        assertEquals(1, restantes.size());
        assertEquals(8, ((MovimentoPeca) restantes.get(0)).getCodStock());
    }

    // ── Análise financeira ────────────────────────────────────────────────────

    @Test @Order(17) @DisplayName("calcularAnalise: receitas, despesas e saldo calculados corretamente")
    void calcularAnalise_valido() {
        facade.registarMovimentoCompraStock(1, 100f, "Compra");     // despesa GastoPecas
        facade.registarMovimentoPagamentoFuncionario(1, 500f, "Salário"); // despesa Salario
        facade.registarMovimentoVendaPeca(1, 80f, "Venda");          // receita LucroVendaPecas
        facade.registarMovimentoReparacaoOS(1, 200f, "Mão obra");    // receita LucroMaoObra

        AnaliseFinanceira analise = facade.calcularAnaliseFinanceira(facade.obterMovimentos());
        assertEquals(280f, analise.getReceitas(), 0.01f);
        assertEquals(600f, analise.getDespesas(), 0.01f);
        assertEquals(-320f, analise.getSaldo(), 0.01f);
        assertEquals(4, analise.getMovimentos(), 0.01f);
    }

    @Test @Order(18) @DisplayName("calcularAnalise: lista vazia resulta em zeros")
    void calcularAnalise_listaVazia() {
        AnaliseFinanceira analise = facade.calcularAnaliseFinanceira(List.of());
        assertEquals(0f, analise.getReceitas());
        assertEquals(0f, analise.getDespesas());
        assertEquals(0f, analise.getSaldo());
    }

    // ── Estrutura final ───────────────────────────────────────────────────────

    @Test @Order(19) @DisplayName("estruturaFinal: registar vários tipos, filtrar e calcular análise")
    void estruturaFinal() {
        facade.registarMovimentoCompraStock(1, 200f, "Compra peças");
        facade.registarMovimentoReparacaoOS(2, 350f, "OS reparação");
        facade.registarMovimentoPagamentoFuncionario(3, 1000f, "Salário");
        facade.registarMovimentoVendaPeca(4, 150f, "Venda extra");

        assertEquals(4, facade.obterMovimentos().size());

        List<MovimentoFinanceiro> salarios = facade.obterMovimentosFinanceirosFiltrados(
                null, null, TipoMovimento.Salario);
        assertEquals(1, salarios.size());

        AnaliseFinanceira analise = facade.calcularAnaliseFinanceira(facade.obterMovimentos());
        assertEquals(500f, analise.getReceitas(), 0.01f);   // 350 + 150
        assertEquals(1200f, analise.getDespesas(), 0.01f);  // 200 + 1000
        assertEquals(-700f, analise.getSaldo(), 0.01f);

        facade.removerMovimentoFinanceiro(salarios.get(0).getId());
        assertEquals(3, facade.obterMovimentos().size());
    }
}
