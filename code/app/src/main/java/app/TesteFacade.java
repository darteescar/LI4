package app;

import app.ecoRideLN.EcoRideLN;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sOrdensServico.*;
import app.ecoRideLN.sStock.*;
import app.ecoRideLN.sClientes.*;
import app.ecoRideLN.sFuncionarios.Funcionario;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TesteFacade {

    static IEcoRideLN ln = new EcoRideLN();
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EcoRide Facade - Testes de Integração");
        System.out.println("========================================\n");

        testarFuncionariosEUtilizadores();
        testarClientesETrotinetes();
        testarFornecedoresPecasEStock();
        testarReparacoes();
        testarFluxoOS();
        testarDefeitos();
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

    // ---- IDs globais usados entre testes ----
    static int idFuncGerente, idFuncMecanico, idFuncSecretaria, idFuncGestorStock;
    static int idUtilGerente, idUtilMecanico, idUtilSecretaria, idUtilGestorStock;
    static int idCliente, idTrotinete;
    static int idFornecedor, idPeca, idStock;
    static int idReparacao;
    static int idOS;

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
            boolean auth = ln.autenticar(idUtilMecanico, "pass123");
            if (!auth) throw new RuntimeException("autenticação falhou");
            ok("Autenticar utilizador");
        } catch (Exception e) { fail("Autenticar utilizador", e); }

        try {
            Cargo c = ln.obterCargoUtilizador(idUtilGerente);
            if (c != Cargo.Gerente) throw new RuntimeException("cargo errado: " + c);
            ok("Obter cargo utilizador");
        } catch (Exception e) { fail("Obter cargo utilizador", e); }
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

        // Registar mais stock para poder reportar defeito
        int idStockDefeito;
        try {
            Stock s = ln.registarStock_PecaNormal(idPeca, 12.50f, LocalDate.now(), 5);
            idStockDefeito = s.getId();
            ok("Registar stock adicional para defeito");
        } catch (Exception e) { fail("Registar stock adicional", e); return; }

        final int idStockDefeito2 = idStockDefeito;

        try {
            List<Defeito> defeitos = ln.registarDefeito(List.of(idStockDefeito2), "Peça rachada", idFuncGestorStock);
            if (defeitos.isEmpty()) throw new RuntimeException("lista vazia");
            Stock s = ln.obterStock(idStockDefeito2);
            if (s.getEstado() != EstadoStock.StockComPossivelDefeito)
                throw new RuntimeException("estado errado: " + s.getEstado());
            ok("Reportar defeito (stock passa a StockComPossivelDefeito)");
        } catch (Exception e) { fail("Reportar defeito", e); }

        try {
            List<Defeito> defeitos = ln.obterDefeitos();
            Defeito d = defeitos.stream()
                .filter(x -> x.getCodStock() == idStockDefeito2)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("defeito não encontrado"));

            Devolucao dev = ln.confirmarDefeitoComDevolucao(d.getId(), "Defeito confirmado", LocalDate.now());
            if (dev == null) throw new RuntimeException("devolução nula");
            Stock s = ln.obterStock(idStockDefeito2);
            if (s.getEstado() != EstadoStock.StockPendenteDeDevolucao)
                throw new RuntimeException("estado errado: " + s.getEstado());
            ok("Confirmar defeito com devolução (stock: StockPendenteDeDevolucao)");
        } catch (Exception e) { fail("Confirmar defeito com devolução", e); }
    }

    static void testarMovimentosFinanceiros() {
        secao("Movimentos Financeiros");

        try {
            List<MovimentoFinanceiro> movs = ln.obterMovimentosFinanceiros();
            if (movs.isEmpty()) throw new RuntimeException("nenhum movimento registado");
            ok("Existem movimentos financeiros registados (" + movs.size() + " no total)");
        } catch (Exception e) { fail("Obter movimentos financeiros", e); }

        try {
            var analise = ln.calcularAnaliseFinanceira(ln.obterMovimentosFinanceiros());
            if (analise == null) throw new RuntimeException("análise nula");
            ok("Calcular análise financeira");
        } catch (Exception e) { fail("Calcular análise financeira", e); }
    }
}
