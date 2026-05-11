

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.EcoRideLN;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Diagnostico;
import app.ecoRideLN.sOrdensServico.EstadoOS;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Defeito;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.EstadoDevolucao;
import app.ecoRideLN.sStock.EstadoEncomenda;
import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;

public class TesteFacade {

    static IEcoRideLN ln = new EcoRideLN();
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EcoRide Facade - Testes de Integração");
        System.out.println("========================================\n");

        limparBD();
        testarFuncionariosEUtilizadores();
        testarClientesETrotinetes();
        testarFornecedoresPecasEStock();
        testarReparacoes();
        testarFluxoOS();
        testarDefeitos();
        testarFluxoEncomenda();
        testarDefeitosAvancados();
        testarFluxoDevolucao();
        testarPagamentoFuncionario();
        testarMovimentosFinanceiros();

        System.out.println("\n========================================");
        System.out.printf("  Resultado: %d passaram, %d falharam%n", passed, failed);
        System.out.println("========================================");
    }

    // ---- Utilitários ----

    static void ok(String nome) {
        System.out.println("  [OK] " + nome);
        passed++;
    }

    static void fail(String nome, Exception e) {
        System.out.println("  [FAIL] " + nome + " -> " + e.getMessage());
        failed++;
    }

    static void secao(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
    }

    static void limparBD() {
        String[] tabelas = {
            "Notificacao",
            "Conserto_PecaUsada",
            "Conserto_Reparacao",
            "Conserto",
            "Diagnostico_PecaOrcamento",
            "Diagnostico_Reparacao",
            "Diagnostico",
            "Fotografia",
            "OrdemServico_Acessorio",
            "OrdemServico",
            "Defeito",
            "Encomenda_EntradaStock",
            "Encomenda",
            "Devolucao",
            "Stock",
            "MovimentoFinanceiro",
            "Peca",
            "Fornecedor",
            "Utilizador",
            "Trotinete",
            "Cliente",
            "Funcionario",
            "Reparacao"
        };
        try (Connection conn = ConnectionFactory.get(); Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            for (String tabela : tabelas) {
                st.execute("DELETE FROM " + tabela);
            }
            st.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("[limparBD] Base de dados limpa.\n");
        } catch (Exception e) {
            System.out.println("[limparBD] ERRO ao limpar BD: " + e.getMessage());
        }
    }

    // ---- IDs globais usados entre testes ----
    static int idFuncGerente, idFuncMecanico, idFuncSecretaria, idFuncGestorStock;
    static int idUtilGerente, idUtilMecanico, idUtilSecretaria, idUtilGestorStock;
    static int idCliente, idTrotinete;
    static int idFornecedor, idPeca, idStock;
    static int idReparacao;
    static int idOS;
    static int idDevolucao;             // criado em testarDefeitos, usado em testarFluxoDevolucao
    static int idDevolucaoInvalida;     // criado em testarDefeitosAvancados, marcado inválida
    static int idEncomenda;
    static int idStockEncomendado;      // primeiro stock da encomenda

    // =========================================================

    static void testarFuncionariosEUtilizadores() {
        secao("Funcionários e Utilizadores");

        try {
            Funcionario fGerente = ln.registarFuncionario("Ana Gerente","910000001","ana@ecoride.pt",
                LocalDate.of(1980,1,1),"NISS1","NIF1","NUS1","IBAN1",20,1500,2000,0,"1","Rua A","Lisboa","1000-001");
            idFuncGerente = fGerente.getId();
            idUtilGerente = ln.registarUtilizador("pass123", idFuncGerente, Cargo.Gerente, "ana").getId();
            ok("Registar Gerente");
        } catch (Exception e) { fail("Registar Gerente", e); }

        try {
            Funcionario fMec = ln.registarFuncionario("Bruno Mecanico","910000002","bruno@ecoride.pt",
                LocalDate.of(1990,5,15),"NISS2","NIF2","NUS2","IBAN2",15,1200,1600,0,"2","Rua B","Porto","4000-001");
            idFuncMecanico = fMec.getId();
            idUtilMecanico = ln.registarUtilizador("pass123", idFuncMecanico, Cargo.Mecanico, "bruno").getId();
            ok("Registar Mecânico");
        } catch (Exception e) { fail("Registar Mecânico", e); }

        try {
            Funcionario fSec = ln.registarFuncionario("Carla Secretaria","910000003","carla@ecoride.pt",
                LocalDate.of(1985,3,20),"NISS3","NIF3","NUS3","IBAN3",12,1000,1300,0,"3","Rua C","Braga","4700-001");
            idFuncSecretaria = fSec.getId();
            idUtilSecretaria = ln.registarUtilizador("pass123", idFuncSecretaria, Cargo.Secretaria, "carla").getId();
            ok("Registar Secretária");
        } catch (Exception e) { fail("Registar Secretária", e); }

        try {
            Funcionario fGS = ln.registarFuncionario("Diogo GestorStock","910000004","diogo@ecoride.pt",
                LocalDate.of(1988,7,10),"NISS4","NIF4","NUS4","IBAN4",13,1100,1400,0,"4","Rua D","Coimbra","3000-001");
            idFuncGestorStock = fGS.getId();
            idUtilGestorStock = ln.registarUtilizador("pass123", idFuncGestorStock, Cargo.GestorStock, "diogo").getId();
            ok("Registar Gestor de Stock");
        } catch (Exception e) { fail("Registar Gestor de Stock", e); }

        try {
            if (!ln.autenticar(idUtilMecanico, "pass123"))   throw new RuntimeException("mecânico falhou");
            if (!ln.autenticar(idUtilSecretaria, "pass123")) throw new RuntimeException("secretária falhou");
            if (!ln.autenticar(idUtilGestorStock, "pass123"))throw new RuntimeException("gestor stock falhou");
            ok("Autenticar mecânico, secretária e gestor de stock");
        } catch (Exception e) { fail("Autenticar utilizadores", e); }

        try {
            Cargo c = ln.obterCargoUtilizador(idUtilGerente);
            if (c != Cargo.Gerente) throw new RuntimeException("cargo errado: " + c);
            if (ln.obterCargoUtilizador(idUtilSecretaria) != Cargo.Secretaria)
                throw new RuntimeException("cargo da secretária errado");
            if (ln.obterCargoUtilizador(idUtilGestorStock) != Cargo.GestorStock)
                throw new RuntimeException("cargo do gestor de stock errado");
            ok("Obter cargo dos utilizadores");
        } catch (Exception e) { fail("Obter cargo utilizador", e); }

        // Identificador duplicado deve ser rejeitado
        try {
            ln.registarUtilizador("outrapass", idFuncGerente, Cargo.Gerente, "ana");
            fail("Identificador duplicado deve lançar exceção", new RuntimeException("nenhuma exceção lançada"));
        } catch (app.common.EcoRideException e) {
            ok("Identificador duplicado rejeitado");
        } catch (Exception e) { fail("Identificador duplicado rejeitado", e); }
    }

    static void testarClientesETrotinetes() {
        secao("Clientes e Trotinetes");

        try {
            Cliente cl = ln.registarCliente("Eduardo Silva","eduardo@email.pt","912345678","123456789");
            idCliente = cl.getId();
            ok("Registar cliente");
        } catch (Exception e) { fail("Registar cliente", e); }

        try {
            Trotinete t = ln.registarTrotinete(idCliente,"Model X","Xiaomi","SN-001","Brushless");
            idTrotinete = t.getId();
            ok("Registar trotinete");
        } catch (Exception e) { fail("Registar trotinete", e); }

        try {
            Cliente cl2 = ln.obterCliente(idCliente);
            if (!cl2.getNome().equals("Eduardo Silva")) throw new RuntimeException("nome errado");
            ok("Obter cliente");
        } catch (Exception e) { fail("Obter cliente", e); }
    }

    static void testarFornecedoresPecasEStock() {
        secao("Fornecedores, Peças e Stock");

        try {
            Fornecedor f = ln.registarFornecedor("Fornecedor ABC","910000099","abc@fornecedor.pt");
            idFornecedor = f.getId();
            ok("Registar fornecedor");
        } catch (Exception e) { fail("Registar fornecedor", e); }

        try {
            Peca p = ln.registarPeca("REF-001","Travão Dianteiro","Travão dianteiro reforçado",2,29.99f,idFornecedor);
            idPeca = p.getId();
            ok("Registar peça");
        } catch (Exception e) { fail("Registar peça", e); }

        try {
            Stock s = ln.registarStock_PecaNormal(idPeca, 12.50f, LocalDate.now(), 10);
            idStock = s.getId();
            if (s.getQuantidade() != 10) throw new RuntimeException("quantidade errada: " + s.getQuantidade());
            ok("Registar stock (10 unidades)");
        } catch (Exception e) { fail("Registar stock", e); }

        try {
            Stock s = ln.obterStock(idStock);
            if (s.getEstado() != EstadoStock.StockEmArmazem)
                throw new RuntimeException("estado errado: " + s.getEstado());
            ok("Stock em armazém após registo");
        } catch (Exception e) { fail("Stock em armazém após registo", e); }
    }

    static void testarReparacoes() {
        secao("Reparações");

        try {
            Reparacao r = ln.registarReparacao("Substituição de travão","Substituição do travão dianteiro",45.00f,true);
            idReparacao = r.getId();
            ok("Registar reparação");
        } catch (Exception e) { fail("Registar reparação", e); }

        try {
            List<Reparacao> disponiveis = ln.obterReparacoesDisponiveis();
            if (disponiveis.isEmpty()) throw new RuntimeException("lista vazia");
            ok("Obter reparações disponíveis");
        } catch (Exception e) { fail("Obter reparações disponíveis", e); }
    }

    static void testarFluxoOS() {
        secao("Fluxo completo de Ordem de Serviço");

        // 1. Criar OS
        try {
            OrdemServico os = ln.registarOS(idCliente, idTrotinete,
                "Travão dianteiro com ruído", List.of("Carregador"), List.of(), idFuncSecretaria);
            idOS = os.getId();
            if (os.getEstado() != EstadoOS.PendenteDiagnostico)
                throw new RuntimeException("estado inicial errado: " + os.getEstado());
            ok("Registar OS (estado: PendenteDiagnostico)");
        } catch (Exception e) { fail("Registar OS", e); }

        // 2. Atribuir mecânico
        try {
            boolean r = ln.atribuirOS(idOS, idFuncMecanico);
            if (!r) throw new RuntimeException("retornou false");
            ok("Atribuir mecânico à OS");
        } catch (Exception e) { fail("Atribuir mecânico à OS", e); }

        // 3. Registar diagnóstico
        try {
            Reparacao rep = ln.obterReparacao(idReparacao);
            Diagnostico d = ln.registarDiagnosticoOS(
                idOS,
                Map.of(idPeca, 2),
                List.of(rep),
                "Travão desgastado, necessita substituição",
                idFuncMecanico
            );
            if (d == null) throw new RuntimeException("diagnóstico nulo");
            OrdemServico os = ln.obterOS(idOS);
            if (os.getEstado() != EstadoOS.PendenteAprovacaoOrcamento)
                throw new RuntimeException("estado errado: " + os.getEstado());
            ok("Registar diagnóstico (estado: PendenteAprovacaoOrcamento)");
        } catch (Exception e) { fail("Registar diagnóstico", e); }

        // 4. Aprovar orçamento
        try {
            boolean r = ln.aprovarOrcamentoOS(idOS);
            if (!r) throw new RuntimeException("retornou false");
            OrdemServico os = ln.obterOS(idOS);
            if (os.getEstado() != EstadoOS.PendenteReparacao)
                throw new RuntimeException("estado errado: " + os.getEstado());
            ok("Aprovar orçamento (estado: PendenteReparacao)");
        } catch (Exception e) { fail("Aprovar orçamento", e); }

        // 5. Verificar notificação ao mecânico
        try {
            List<app.ecoRideLN.sNotificacoes.Notificacao> nots =
                ln.obterNotificacoesPorDestinatario(idUtilMecanico);
            boolean temNotif = nots.stream().anyMatch(n -> n.getDescricao().contains("aprovado"));
            if (!temNotif) throw new RuntimeException("notificação de aprovação não encontrada");
            ok("Mecânico recebeu notificação de orçamento aprovado");
        } catch (Exception e) { fail("Notificação ao mecânico (aprovação)", e); }

        // 6. Registar conserto (usa 2 unidades do stock via FIFO)
        try {
            CheckList cl = new CheckList();
            cl.validarChecklist();
            Reparacao rep = ln.obterReparacao(idReparacao);
            Conserto c = ln.registarConsertoOS(
                idOS,
                Map.of(idPeca, 2),
                List.of(rep),
                idFuncMecanico,
                cl
            );
            if (c == null) throw new RuntimeException("conserto nulo");
            OrdemServico os = ln.obterOS(idOS);
            if (os.getEstado() != EstadoOS.PendentePagamento)
                throw new RuntimeException("estado errado: " + os.getEstado());
            ok("Registar conserto (estado: PendentePagamento)");
        } catch (Exception e) { fail("Registar conserto", e); }

        // 7. Verificar FIFO: stock deve ter 8 unidades restantes
        try {
            Stock s = ln.obterStock(idStock);
            if (s.getQuantidade() != 8)
                throw new RuntimeException("quantidade esperada 8, obtida " + s.getQuantidade());
            ok("FIFO: stock decrementado de 10 para 8 após consumo de 2");
        } catch (Exception e) { fail("FIFO: verificação de quantidade", e); }

        // 8. Registar pagamento
        try {
            boolean r = ln.registarPagamentoOS(idOS, Metodo_Pagamento.MULTIBANCO);
            if (!r) throw new RuntimeException("retornou false");
            OrdemServico os = ln.obterOS(idOS);
            if (os.getEstado() != EstadoOS.Paga)
                throw new RuntimeException("estado errado: " + os.getEstado());
            ok("Registar pagamento (estado: Paga)");
        } catch (Exception e) { fail("Registar pagamento", e); }

        // 9. Notificações geradas para gerente + secretária no diagnóstico e conserto
        try {
            List<app.ecoRideLN.sNotificacoes.Notificacao> nots =
                ln.obterNotificacoesPorDestinatario(idUtilGerente);
            if (nots.size() < 2) throw new RuntimeException("esperadas >=2 notificações, obtidas " + nots.size());
            ok("Gerente recebeu notificações de diagnóstico e conserto");
        } catch (Exception e) { fail("Notificações ao gerente", e); }
    }

    static void testarDefeitos() {
        secao("Defeitos");

        int idStockDefeito;
        try {
            Stock s = ln.registarStock_PecaNormal(idPeca, 12.50f, LocalDate.now(), 5);
            idStockDefeito = s.getId();
            ok("Registar stock adicional para defeito (5 unidades)");
        } catch (Exception e) { fail("Registar stock adicional", e); return; }

        final int idStockRef = idStockDefeito;

        try {
            List<Defeito> defeitos = ln.registarDefeito(List.of(idStockRef), "Peça rachada", idFuncGestorStock);
            if (defeitos.isEmpty()) throw new RuntimeException("lista vazia");
            Stock s = ln.obterStock(idStockRef);
            if (s.getEstado() != EstadoStock.StockComPossivelDefeito)
                throw new RuntimeException("estado errado: " + s.getEstado());
            ok("Reportar defeito (stock → StockComPossivelDefeito)");
        } catch (Exception e) { fail("Reportar defeito", e); }

        try {
            List<Defeito> defeitos = ln.obterDefeitos();
            Defeito d = defeitos.stream()
                .filter(x -> x.getCodStock() == idStockRef)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("defeito não encontrado"));

            Devolucao dev = ln.confirmarDefeitoComDevolucao(d.getId(), "Defeito confirmado", LocalDate.now());
            if (dev == null) throw new RuntimeException("devolução nula");
            idDevolucao = dev.getId(); // guardar para testarFluxoDevolucao
            Stock s = ln.obterStock(idStockRef);
            if (s.getEstado() != EstadoStock.StockPendenteDeDevolucao)
                throw new RuntimeException("estado errado: " + s.getEstado());
            ok("Confirmar defeito com devolução (stock → StockPendenteDeDevolucao)");
        } catch (Exception e) { fail("Confirmar defeito com devolução", e); }
    }

    // =========================================================
    // Encomenda
    // =========================================================

    static void testarFluxoEncomenda() {
        secao("Fluxo de Encomenda");

        // 1. Criar encomenda em RASCUNHO (5 unidades de idPeca a 8€)
        try {
            Encomenda e = ln.registarEncomenda(
                List.of(idPeca),
                List.of(8.0f),
                List.of(5),
                idFornecedor
            );
            idEncomenda = e.getId();
            if (e.getEstado() != EstadoEncomenda.RASCUNHO)
                throw new RuntimeException("estado inicial errado: " + e.getEstado());
            if (e.getCodStocks().isEmpty())
                throw new RuntimeException("sem stocks associados");
            idStockEncomendado = e.getCodStocks().get(0);
            ok("Criar encomenda (estado: RASCUNHO)");
        } catch (Exception e) { fail("Criar encomenda", e); return; }

        // 2. Verificar que o stock está em StockEncomendado
        try {
            Stock s = ln.obterStock(idStockEncomendado);
            if (s.getEstado() != EstadoStock.StockEncomendado)
                throw new RuntimeException("estado esperado StockEncomendado, obtido: " + s.getEstado());
            ok("Stock da encomenda em StockEncomendado");
        } catch (Exception e) { fail("Stock da encomenda em StockEncomendado", e); }

        // 3. Marcar como enviada
        try {
            Encomenda e = ln.marcarEncomendaComoEnviada(idEncomenda);
            if (e.getEstado() != EstadoEncomenda.ENVIADA)
                throw new RuntimeException("estado errado: " + e.getEstado());
            if (e.getData_envio() == null)
                throw new RuntimeException("data_envio nula");
            ok("Marcar encomenda como enviada (estado: ENVIADA)");
        } catch (Exception e) { fail("Marcar encomenda como enviada", e); }

        // 4. Segunda chamada a marcarComoEnviada deve ser ignorada (só transita de RASCUNHO)
        try {
            Encomenda e = ln.marcarEncomendaComoEnviada(idEncomenda);
            if (e.getEstado() != EstadoEncomenda.ENVIADA)
                throw new RuntimeException("estado mudou indevidamente: " + e.getEstado());
            ok("Segunda tentativa de envio é ignorada (idempotente)");
        } catch (Exception e) { fail("Segunda tentativa de envio", e); }

        // 5. Registar quantos movimentos existem antes de receber
        int movimentosAntes = ln.obterMovimentosFinanceiros().size();

        // 6. Marcar como recebida (peça com preco_venda < 70 → listas vazias)
        try {
            Encomenda e = ln.marcarEncomendaComoRecebida(idEncomenda, List.of(), List.of());
            if (e.getEstado() != EstadoEncomenda.RECEBIDA)
                throw new RuntimeException("estado errado: " + e.getEstado());
            if (e.getData_rececao() == null)
                throw new RuntimeException("data_rececao nula");
            ok("Marcar encomenda como recebida (estado: RECEBIDA)");
        } catch (Exception e) { fail("Marcar encomenda como recebida", e); return; }

        // 7. Verificar que o stock transitou para StockEmArmazem
        try {
            Stock s = ln.obterStock(idStockEncomendado);
            if (s.getEstado() != EstadoStock.StockEmArmazem)
                throw new RuntimeException("estado esperado StockEmArmazem, obtido: " + s.getEstado());
            ok("Stock da encomenda transitou para StockEmArmazem");
        } catch (Exception e) { fail("Stock da encomenda em StockEmArmazem", e); }

        // 8. Verificar que foi criado um movimento financeiro GastoPecas
        try {
            int movimentosDepois = ln.obterMovimentosFinanceiros().size();
            if (movimentosDepois <= movimentosAntes)
                throw new RuntimeException("nenhum movimento criado ao receber encomenda");
            boolean temGasto = ln.obterMovimentosFinanceiros().stream()
                .skip(movimentosAntes)
                .anyMatch(m -> m.getTipo() == TipoMovimento.GastoPecas && m.getValor() > 0);
            if (!temGasto)
                throw new RuntimeException("movimento GastoPecas positivo não encontrado");
            ok("Movimento GastoPecas criado ao receber encomenda");
        } catch (Exception e) { fail("Movimento financeiro da encomenda", e); }

        // 9. Tentar marcar como recebida uma encomenda já recebida — deve lançar exceção
        try {
            ln.marcarEncomendaComoRecebida(idEncomenda, List.of(), List.of());
            fail("Receber encomenda já recebida deve lançar exceção", new RuntimeException("nenhuma exceção lançada"));
        } catch (app.common.EcoRideException ex) {
            ok("Receber encomenda já recebida lança EcoRideException");
        } catch (Exception e) { fail("Receber encomenda já recebida", e); }
    }

    // =========================================================
    // Defeitos avançados
    // =========================================================

    static void testarDefeitosAvancados() {
        secao("Defeitos Avançados");

        // Cenário A: defeito reportado e depois descartado (stock volta ao estado original)
        int idStockA;
        try {
            Stock s = ln.registarStock_PecaNormal(idPeca, 10.0f, LocalDate.now(), 3);
            idStockA = s.getId();
            ok("Cenário A: registar stock (3 unidades) para defeito a descartar");
        } catch (Exception e) { fail("Cenário A: registar stock", e); return; }

        final int idStockARef = idStockA;

        try {
            List<Defeito> defeitos = ln.registarDefeito(List.of(idStockARef), "Suspeita de defeito", idFuncGestorStock);
            if (defeitos.isEmpty()) throw new RuntimeException("lista vazia");
            if (ln.obterStock(idStockARef).getEstado() != EstadoStock.StockComPossivelDefeito)
                throw new RuntimeException("estado errado: " + ln.obterStock(idStockARef).getEstado());
            ok("Cenário A: defeito reportado (stock → StockComPossivelDefeito)");
        } catch (Exception e) { fail("Cenário A: reportar defeito", e); }

        try {
            Defeito d = ln.obterDefeitos().stream()
                .filter(x -> x.getCodStock() == idStockARef)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("defeito não encontrado"));
            EstadoStock estadoOriginal = d.getEstadoAnterior();

            ln.descartarDefeito(d.getId());

            if (ln.obterStock(idStockARef).getEstado() != estadoOriginal)
                throw new RuntimeException("stock não voltou ao estado original: " + ln.obterStock(idStockARef).getEstado());
            boolean defeitoRemovido = ln.obterDefeitos().stream().noneMatch(x -> x.getCodStock() == idStockARef);
            if (!defeitoRemovido) throw new RuntimeException("defeito não foi removido após descartar");
            ok("Cenário A: defeito descartado (stock voltou a " + estadoOriginal + ")");
        } catch (Exception e) { fail("Cenário A: descartar defeito", e); }

        // Cenário B: múltiplos stocks com defeito em simultâneo
        int idStockB1, idStockB2;
        try {
            idStockB1 = ln.registarStock_PecaNormal(idPeca, 10.0f, LocalDate.now(), 2).getId();
            idStockB2 = ln.registarStock_PecaNormal(idPeca, 10.0f, LocalDate.now(), 4).getId();
            ok("Cenário B: registar dois stocks para defeito simultâneo");
        } catch (Exception e) { fail("Cenário B: registar stocks", e); return; }

        final int idStockB1Ref = idStockB1;
        final int idStockB2Ref = idStockB2;

        try {
            List<Defeito> defeitos = ln.registarDefeito(
                List.of(idStockB1Ref, idStockB2Ref), "Lote com possível defeito", idFuncGestorStock);
            if (defeitos.size() != 2)
                throw new RuntimeException("esperados 2 defeitos, obtidos " + defeitos.size());
            if (ln.obterStock(idStockB1Ref).getEstado() != EstadoStock.StockComPossivelDefeito)
                throw new RuntimeException("stock B1 no estado errado");
            if (ln.obterStock(idStockB2Ref).getEstado() != EstadoStock.StockComPossivelDefeito)
                throw new RuntimeException("stock B2 no estado errado");
            ok("Cenário B: dois stocks com defeito simultâneo");
        } catch (Exception e) { fail("Cenário B: defeito simultâneo", e); }

        // Confirmar B1 com devolução (guardada para testarFluxoDevolucao como caminho "Invalida")
        try {
            Defeito d = ln.obterDefeitos().stream()
                .filter(x -> x.getCodStock() == idStockB1Ref)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("defeito B1 não encontrado"));
            Devolucao dev = ln.confirmarDefeitoComDevolucao(d.getId(), "Confirmado no lote", LocalDate.now());
            idDevolucaoInvalida = dev.getId();
            ok("Cenário B: defeito B1 confirmado com devolução (id=" + idDevolucaoInvalida + ")");
        } catch (Exception e) { fail("Cenário B: confirmar defeito B1", e); }

        // Descartar B2
        try {
            Defeito d = ln.obterDefeitos().stream()
                .filter(x -> x.getCodStock() == idStockB2Ref)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("defeito B2 não encontrado"));
            ln.descartarDefeito(d.getId());
            if (ln.obterStock(idStockB2Ref).getEstado() != EstadoStock.StockEmArmazem)
                throw new RuntimeException("stock B2 não voltou ao armazém");
            ok("Cenário B: defeito B2 descartado (stock B2 → StockEmArmazem)");
        } catch (Exception e) { fail("Cenário B: descartar defeito B2", e); }

        // Cenário C: tentar reportar defeito num stock em estado inválido (StockPendenteDeDevolucao)
        try {
            ln.registarDefeito(List.of(idStockB1Ref), "Defeito em stock pendente", idFuncGestorStock);
            fail("Cenário C: defeito em estado inválido deve lançar exceção",
                new RuntimeException("nenhuma exceção lançada"));
        } catch (app.common.EcoRideException ex) {
            ok("Cenário C: defeito em StockPendenteDeDevolucao lança EcoRideException");
        } catch (Exception e) { fail("Cenário C: defeito em estado inválido", e); }
    }

    // =========================================================
    // Fluxo de Devolução
    // =========================================================

    static void testarFluxoDevolucao() {
        secao("Fluxo de Devolução");

        if (idDevolucao == 0) {
            System.out.println("  [SKIP] idDevolucao não disponível (testarDefeitos falhou)");
            return;
        }

        // Caminho A: StockPendenteDeDevolucao → Enviada → Devolvida (com reembolso financeiro)

        try {
            Devolucao dev = ln.obterDevolucao(idDevolucao);
            if (dev.getEstado() != EstadoDevolucao.StockPendenteDeDevolucao)
                throw new RuntimeException("estado inicial errado: " + dev.getEstado());
            ok("Devolução A em StockPendenteDeDevolucao");
        } catch (Exception e) { fail("Estado inicial devolução A", e); }

        try {
            int codStock = ln.obterDevolucao(idDevolucao).getCodStock();
            ln.marcarDevolucaoComoEnviada(idDevolucao);
            if (ln.obterStock(codStock).getEstado() != EstadoStock.StockEnviadoParaFornecedor)
                throw new RuntimeException("stock no estado errado: " + ln.obterStock(codStock).getEstado());
            if (ln.obterDevolucao(idDevolucao).getEstado() != EstadoDevolucao.Enviada)
                throw new RuntimeException("estado da devolução errado: "
                    + ln.obterDevolucao(idDevolucao).getEstado()
                    + " (esperado: Enviada)");
            ok("Devolução A enviada (stock → StockEnviadoParaFornecedor, devolução → Enviada)");
        } catch (Exception e) { fail("Marcar devolução A como enviada", e); }

        int movimentosAntes = ln.obterMovimentosFinanceiros().size();

        try {
            int codStock = ln.obterDevolucao(idDevolucao).getCodStock();
            ln.marcarDevolucaoComoDevolvida(idDevolucao);
            if (ln.obterStock(codStock).getEstado() != EstadoStock.StockDevolvidoFornecedor)
                throw new RuntimeException("stock no estado errado: " + ln.obterStock(codStock).getEstado());
            if (ln.obterDevolucao(idDevolucao).getEstado() != EstadoDevolucao.Devolvida)
                throw new RuntimeException("estado da devolução errado: " + ln.obterDevolucao(idDevolucao).getEstado());
            ok("Devolução A devolvida (stock → StockDevolvidoFornecedor, devolução → Devolvida)");
        } catch (Exception e) { fail("Marcar devolução A como devolvida", e); }

        try {
            List<MovimentoFinanceiro> movimentos = ln.obterMovimentosFinanceiros();
            if (movimentos.size() <= movimentosAntes)
                throw new RuntimeException("nenhum movimento criado ao devolver");
            MovimentoFinanceiro reembolso = movimentos.stream()
                .filter(m -> m.getTipo() == TipoMovimento.GastoPecas && m.getValor() < 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("movimento de reembolso (GastoPecas negativo) não encontrado"));
            ok("Reembolso criado (GastoPecas negativo: " + reembolso.getValor() + "€)");
        } catch (Exception e) { fail("Movimento de reembolso", e); }

        // Caminho B: StockPendenteDeDevolucao → Inválida (stock volta a StockEmArmazem)

        if (idDevolucaoInvalida == 0) {
            System.out.println("  [SKIP] idDevolucaoInvalida não disponível (testarDefeitosAvancados falhou)");
            return;
        }

        try {
            if (ln.obterDevolucao(idDevolucaoInvalida).getEstado() != EstadoDevolucao.StockPendenteDeDevolucao)
                throw new RuntimeException("estado inicial errado: " + ln.obterDevolucao(idDevolucaoInvalida).getEstado());
            ok("Devolução B em StockPendenteDeDevolucao");
        } catch (Exception e) { fail("Estado inicial devolução B", e); }

        try {
            int codStock = ln.obterDevolucao(idDevolucaoInvalida).getCodStock();
            ln.marcarDevolucaoComoInvalida(idDevolucaoInvalida);
            if (ln.obterStock(codStock).getEstado() != EstadoStock.StockEmArmazem)
                throw new RuntimeException("stock no estado errado: " + ln.obterStock(codStock).getEstado());
            if (ln.obterDevolucao(idDevolucaoInvalida).getEstado() != EstadoDevolucao.Invalida)
                throw new RuntimeException("estado da devolução errado: " + ln.obterDevolucao(idDevolucaoInvalida).getEstado());
            ok("Devolução B inválida (stock → StockEmArmazem, devolução → Invalida)");
        } catch (Exception e) { fail("Marcar devolução B como inválida", e); }
    }

    // =========================================================
    // Pagamento de funcionário
    // =========================================================

    static void testarPagamentoFuncionario() {
        secao("Pagamento de Funcionário");

        int movimentosAntes = ln.obterMovimentosFinanceiros().size();

        try {
            boolean r = ln.registarPagamentoFuncionario(idFuncMecanico);
            if (!r) throw new RuntimeException("retornou false");
            ok("Registar pagamento ao mecânico");
        } catch (Exception e) { fail("Registar pagamento ao mecânico", e); return; }

        try {
            boolean temSalario = ln.obterMovimentosFinanceiros().stream()
                .skip(movimentosAntes)
                .anyMatch(m -> m.getTipo() == TipoMovimento.Salario && m.getValor() > 0);
            if (!temSalario) throw new RuntimeException("movimento Salario positivo não encontrado");
            ok("Movimento Salario criado após pagamento");
        } catch (Exception e) { fail("Movimento Salario", e); }

        try {
            List<MovimentoFinanceiro> salarios = ln.obterMovimentosFinanceirosFiltrados(
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), TipoMovimento.Salario);
            if (salarios.isEmpty()) throw new RuntimeException("filtro retornou lista vazia");
            ok("Filtro por tipo Salario retorna " + salarios.size() + " resultado(s)");
        } catch (Exception e) { fail("Filtro por tipo Salario", e); }
    }

    // =========================================================
    // Movimentos Financeiros — verificação final
    // =========================================================

    static void testarMovimentosFinanceiros() {
        secao("Movimentos Financeiros — Verificação Final");

        List<MovimentoFinanceiro> todos;
        try {
            todos = ln.obterMovimentosFinanceiros();
            if (todos.isEmpty()) throw new RuntimeException("nenhum movimento registado");
            ok("Total de movimentos registados: " + todos.size());
        } catch (Exception e) { fail("Obter movimentos financeiros", e); return; }

        // Verificar existência de cada tipo
        for (TipoMovimento tipo : TipoMovimento.values()) {
            try {
                long count = todos.stream().filter(m -> m.getTipo() == tipo).count();
                if (count == 0) throw new RuntimeException("nenhum movimento do tipo " + tipo);
                ok("Tipo " + tipo + ": " + count + " movimento(s)");
            } catch (Exception e) { fail("Tipo " + tipo, e); }
        }

        // GastoPecas deve ter positivos (compras) e negativos (reembolsos)
        try {
            boolean temPositivo = todos.stream()
                .anyMatch(m -> m.getTipo() == TipoMovimento.GastoPecas && m.getValor() > 0);
            boolean temNegativo = todos.stream()
                .anyMatch(m -> m.getTipo() == TipoMovimento.GastoPecas && m.getValor() < 0);
            if (!temPositivo) throw new RuntimeException("sem GastoPecas positivo (compras)");
            if (!temNegativo) throw new RuntimeException("sem GastoPecas negativo (reembolsos)");
            ok("GastoPecas tem compras (positivo) e reembolsos (negativo)");
        } catch (Exception e) { fail("GastoPecas positivo e negativo", e); }

        // Análise financeira
        try {
            var analise = ln.calcularAnaliseFinanceira(todos);
            if (analise == null) throw new RuntimeException("análise nula");
            ok("Calcular análise financeira");
        } catch (Exception e) { fail("Calcular análise financeira", e); }

        // Filtro por intervalo de datas
        try {
            List<MovimentoFinanceiro> filtrados = ln.obterMovimentosFinanceirosFiltrados(
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), null);
            if (filtrados.isEmpty()) throw new RuntimeException("filtro por data retornou lista vazia");
            ok("Filtro por intervalo de datas retorna " + filtrados.size() + " movimento(s)");
        } catch (Exception e) { fail("Filtro por intervalo de datas", e); }

        // Filtro por tipo GastoPecas
        try {
            List<MovimentoFinanceiro> gastos = ln.obterMovimentosFinanceirosFiltrados(
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), TipoMovimento.GastoPecas);
            if (gastos.isEmpty()) throw new RuntimeException("nenhum GastoPecas encontrado");
            ok("Filtro GastoPecas retorna " + gastos.size() + " movimento(s)");
        } catch (Exception e) { fail("Filtro GastoPecas", e); }

        // Filtro por tipo LucroMaoObra
        try {
            List<MovimentoFinanceiro> lucros = ln.obterMovimentosFinanceirosFiltrados(
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), TipoMovimento.LucroMaoObra);
            if (lucros.isEmpty()) throw new RuntimeException("nenhum LucroMaoObra encontrado");
            ok("Filtro LucroMaoObra retorna " + lucros.size() + " movimento(s)");
        } catch (Exception e) { fail("Filtro LucroMaoObra", e); }
    }
}
