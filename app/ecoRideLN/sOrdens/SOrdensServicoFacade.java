package app.ecoRideLN.sOrdens;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sOrdens.FotografiaDAO;
import app.ecoRideCD.sOrdens.OrdemServicoDAO;
import app.ecoRideLN.sAutenticacao.ISAutenticacao;
import app.ecoRideLN.sAutenticacao.Utilizador;
import app.ecoRideLN.sClientes.ISClientes;
import app.ecoRideLN.sFinanceiro.ISFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.ecoRideLN.sFuncionarios.ISFuncionarios;
import app.ecoRideLN.sNotificacoes.ISNotificacoes;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.ISStock;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.PecasDoOrcamento;
import app.ecoRideLN.sStock.SStockFacade;
import app.ecoRideLN.sStock.Stock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SOrdensServicoFacade implements ISOrdens {

    private final OrdemServicoDAO ordensServicoDAO = OrdemServicoDAO.getInstance();
    private final FotografiaDAO fotografiasDAO = FotografiaDAO.getInstance();

    private final ISClientes sClientes;
    private final ISFuncionarios sFuncionarios;
    private final ISReparacoes sReparacoes;
    private final ISStock sStock;
    private final ISFinanceiro sFinanceiro;
    private final ISNotificacoes sNotificacoes;
    private final ISAutenticacao sAutenticacao;

    public SOrdensServicoFacade(ISClientes sClientes, ISFuncionarios sFuncionarios,
                                ISReparacoes sReparacoes, ISStock sStock,
                                ISFinanceiro sFinanceiro, ISNotificacoes sNotificacoes,
                                ISAutenticacao sAutenticacao) {
        this.sClientes = sClientes;
        this.sFuncionarios = sFuncionarios;
        this.sReparacoes = sReparacoes;
        this.sStock = sStock;
        this.sFinanceiro = sFinanceiro;
        this.sNotificacoes = sNotificacoes;
        this.sAutenticacao = sAutenticacao;
    }

    private int idUtilizadorPorFuncionario(int idFuncionario) {
        Utilizador u = sAutenticacao.obterUtilizadorPorIdFuncionario(idFuncionario)
                .orElseThrow(() -> new EcoRideException(
                        "Funcionário " + idFuncionario + " não tem utilizador associado."));
        return u.getId();
    }

    // ---- State machine ----

    private static final Map<EstadoOS, Set<EstadoOS>> TRANSICOES = new HashMap<>();

    static {
        TRANSICOES.put(EstadoOS.PENDENTE_DIAGNOSTICO, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.PENDENTE_APROVACAO_ORCAMENTO, EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.PENDENTE_APROVACAO_ORCAMENTO, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.PENDENTE_REPARACAO, EstadoOS.ORCAMENTO_NAO_APROVADO, EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.PENDENTE_REPARACAO, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.PENDENTE_PAGAMENTO, EstadoOS.AGUARDAR_PECAS, EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.AGUARDAR_PECAS, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.PENDENTE_REPARACAO, EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.PENDENTE_PAGAMENTO, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.PAGA, EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.ORCAMENTO_NAO_APROVADO, new HashSet<>(java.util.Arrays.asList(
                EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.PAGA, new HashSet<>(java.util.Arrays.asList(EstadoOS.ELIMINADA)));
        TRANSICOES.put(EstadoOS.ELIMINADA, new HashSet<>());
    }

    private void validarTransicao(EstadoOS atual, EstadoOS novo) {
        Set<EstadoOS> permitidos = TRANSICOES.get(atual);
        if (permitidos == null || !permitidos.contains(novo))
            throw new EcoRideException("Transição de estado não permitida: " + atual + " → " + novo);
    }

    // ---- Helpers ----

    private OrdemServico obterOuFalhar(int idOS) {
        return ordensServicoDAO.obterPorId(idOS)
                .orElseThrow(() -> new EcoRideException("Ordem de serviço não encontrada."));
    }

    // ---- Registo ----

    @Override
    public OrdemServico registarOS(int idCliente, int idTrotinete, String descricao, int idResponsavel) {
        return registarOSExtras(idCliente, idTrotinete, descricao, idResponsavel, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public OrdemServico registarOSExtras(int idCliente, int idTrotinete, String descricao, int idResponsavel,
                                          List<String> acessorios, List<Fotografia> fotos) {
        Validacoes.naoVazio(descricao, "Descrição");
        if (!sClientes.existeCliente(idCliente))
            throw new EcoRideException("Cliente não encontrado.");
        if (!sClientes.existeTrotinete(idTrotinete))
            throw new EcoRideException("Trotinete não encontrada.");
        if (!sFuncionarios.existeFuncionario(idResponsavel))
            throw new EcoRideException("Funcionário responsável não encontrado.");
        if (acessorios == null) acessorios = new ArrayList<>();
        if (fotos == null) fotos = new ArrayList<>();
        for (Fotografia f : fotos) {
            if (!validarFotografia(f))
                throw new EcoRideException("Fotografia inválida.");
        }

        int id = ordensServicoDAO.generateNewId();
        OrdemServico os = new OrdemServico(id, descricao, idCliente, idTrotinete,
                LocalDateTime.now(), idResponsavel);
        os.getAcessorios().addAll(acessorios);
        ordensServicoDAO.put(os.getId(), os);

        for (Fotografia f : fotos) {
            int idF = fotografiasDAO.generateNewId();
            f.setId(idF);
            fotografiasDAO.putWithOS(f, os.getId());
            os.getCodFotografias().add(idF);
        }
        return os;
    }

    @Override public Optional<OrdemServico> obterDadosOS(int idOS) { return ordensServicoDAO.obterPorId(idOS); }
    @Override public boolean existeOS(int idOS) { return ordensServicoDAO.containsKey(idOS); }

    @Override
    public void removerOS(int idOS) {
        OrdemServico os = obterOuFalhar(idOS);
        validarTransicao(os.getEstado(), EstadoOS.ELIMINADA);
        os.setEstado(EstadoOS.ELIMINADA);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarDescricaoOS(int idOS, String descricao) {
        Validacoes.naoVazio(descricao, "Descrição");
        OrdemServico os = obterOuFalhar(idOS);
        os.setDescricao(descricao);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarAcessoriosOS(int idOS, List<String> acessorios) {
        OrdemServico os = obterOuFalhar(idOS);
        os.getAcessorios().clear();
        if (acessorios != null) os.getAcessorios().addAll(acessorios);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarFotografiasOS(int idOS, List<Fotografia> fotos) {
        OrdemServico os = obterOuFalhar(idOS);
        // remove existing
        for (int idF : new ArrayList<>(os.getCodFotografias()))
            fotografiasDAO.remove(idF);
        os.getCodFotografias().clear();
        // add new
        if (fotos != null) {
            for (Fotografia f : fotos) {
                if (!validarFotografia(f))
                    throw new EcoRideException("Fotografia inválida.");
                int idF = fotografiasDAO.generateNewId();
                f.setId(idF);
                fotografiasDAO.putWithOS(f, os.getId());
                os.getCodFotografias().add(idF);
            }
        }
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarEstadoOS(int idOS, EstadoOS novoEstado) {
        Validacoes.naoNulo(novoEstado, "Novo estado");
        OrdemServico os = obterOuFalhar(idOS);
        validarTransicao(os.getEstado(), novoEstado);
        os.setEstado(novoEstado);
        ordensServicoDAO.put(os.getId(), os);

        if (novoEstado == EstadoOS.PENDENTE_PAGAMENTO) {
            // Notificação interna: mecânico do conserto → secretária responsável da OS
            int remetente = os.getConserto() != null
                    ? idUtilizadorPorFuncionario(os.getConserto().getCodMecanico())
                    : idUtilizadorPorFuncionario(os.getCodResponsavel());
            int destinatario = idUtilizadorPorFuncionario(os.getCodResponsavel());
            sNotificacoes.criarNotificacao(remetente, destinatario,
                    "OS #" + os.getId() + " concluída e pendente de pagamento.");
        }
    }

    @Override
    public void alterarDataCriacaoOS(int idOS, LocalDateTime data) {
        Validacoes.naoNulo(data, "Data");
        OrdemServico os = obterOuFalhar(idOS);
        os.setData_criacao(data);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarFuncionarioResponsavelOS(int idOS, int codResponsavel) {
        if (!sFuncionarios.existeFuncionario(codResponsavel))
            throw new EcoRideException("Funcionário não encontrado.");
        OrdemServico os = obterOuFalhar(idOS);
        os.setCodResponsavel(codResponsavel);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarClienteOS(int idOS, int idCliente) {
        if (!sClientes.existeCliente(idCliente))
            throw new EcoRideException("Cliente não encontrado.");
        OrdemServico os = obterOuFalhar(idOS);
        os.setCodCliente(idCliente);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void alterarTrotineteOS(int idOS, int idTrotinete) {
        if (!sClientes.existeTrotinete(idTrotinete))
            throw new EcoRideException("Trotinete não encontrada.");
        OrdemServico os = obterOuFalhar(idOS);
        os.setCodTrotinete(idTrotinete);
        ordensServicoDAO.put(os.getId(), os);
    }

    // ---- Diagnóstico ----

    @Override
    public void registarDiagnosticoOS(int idOS, int idMecanico, String descricao,
                                       List<Integer> codReparacoes, List<PecasDoOrcamento> listaPecas) {
        Validacoes.naoVazio(descricao, "Descrição do diagnóstico");
        if (!sFuncionarios.existeFuncionario(idMecanico))
            throw new EcoRideException("Mecânico não encontrado.");
        if (codReparacoes == null) codReparacoes = new ArrayList<>();
        if (listaPecas == null) listaPecas = new ArrayList<>();

        for (Integer idR : codReparacoes) {
            if (!sReparacoes.reparacaoDisponivel(idR))
                throw new EcoRideException("Reparação " + idR + " indisponível.");
        }
        for (PecasDoOrcamento qp : listaPecas) {
            if (!sStock.existePecaPorId(qp.getCodPeca()))
                throw new EcoRideException("Peça " + qp.getCodPeca() + " não encontrada.");
            Validacoes.inteiroPositivo(qp.getQuantidade(), "Quantidade da peça");
        }

        OrdemServico os = obterOuFalhar(idOS);
        if (os.getEstado() != EstadoOS.PENDENTE_DIAGNOSTICO)
            throw new EcoRideException("Diagnóstico só pode ser registado em estado PENDENTE_DIAGNOSTICO.");

        float orcamento = calcularOrcamento(codReparacoes, listaPecas);
        Diagnostico d = new Diagnostico(descricao, orcamento, idMecanico);
        d.getCod_reparacoes().addAll(codReparacoes);
        d.getListaPecas().addAll(listaPecas);
        os.setDiagnostico(d);

        os.setEstado(EstadoOS.PENDENTE_APROVACAO_ORCAMENTO);
        ordensServicoDAO.put(os.getId(), os);

        // Notificação interna: mecânico → secretária responsável da OS
        int remetente = idUtilizadorPorFuncionario(idMecanico);
        int destinatario = idUtilizadorPorFuncionario(os.getCodResponsavel());
        sNotificacoes.criarNotificacao(remetente, destinatario,
                "Orçamento da OS #" + os.getId() + " disponível: " + orcamento + "€.");
    }

    @Override
    public List<Integer> obterReparacoesDiagnosticoOS(int idOS) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getDiagnostico() == null)
            throw new EcoRideException("OS sem diagnóstico.");
        return new ArrayList<>(os.getDiagnostico().getCod_reparacoes());
    }

    @Override
    public List<PecasDoOrcamento> obterPecasQuantidadeDiagnosticoOS(int idOS) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getDiagnostico() == null)
            throw new EcoRideException("OS sem diagnóstico.");
        return new ArrayList<>(os.getDiagnostico().getListaPecas());
    }

    @Override
    public float calcularOrcamento(List<Integer> codReparacoes, List<PecasDoOrcamento> listaPecas) {
        float total = 0;
        if (codReparacoes != null) {
            for (Integer idR : codReparacoes) {
                Reparacao r = sReparacoes.obterDadosReparacao(idR)
                        .orElseThrow(() -> new EcoRideException("Reparação não encontrada."));
                total += r.getPreco();
            }
        }
        if (listaPecas != null) {
            for (PecasDoOrcamento qp : listaPecas) {
                Peca p = sStock.obterDadosPeca(qp.getCodPeca())
                        .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
                total += p.getPreco_venda() * qp.getQuantidade();
            }
        }
        return total;
    }

    @Override
    public boolean pecasDiagnosticoDisponiveisReparacao(int idOS) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getDiagnostico() == null) return false;
        for (PecasDoOrcamento qp : os.getDiagnostico().getListaPecas()) {
            if (sStock.obterQuantidadeStockPorPecaId(qp.getCodPeca()) < qp.getQuantidade())
                return false;
        }
        return true;
    }

    // ---- Conserto ----

    @Override
    public void registarConsertoOS(int idOS, int idMecanico, List<Integer> codReparacoes, List<Integer> codStocks) {
        if (!sFuncionarios.existeFuncionario(idMecanico))
            throw new EcoRideException("Mecânico não encontrado.");
        if (codReparacoes == null) codReparacoes = new ArrayList<>();
        if (codStocks == null) codStocks = new ArrayList<>();

        OrdemServico os = obterOuFalhar(idOS);
        if (os.getEstado() != EstadoOS.PENDENTE_REPARACAO)
            throw new EcoRideException("Conserto só pode ser registado em estado PENDENTE_REPARACAO.");

        Conserto c = new Conserto(idMecanico);
        float total = 0;
        for (Integer idR : codReparacoes) {
            Reparacao r = sReparacoes.obterDadosReparacao(idR)
                    .orElseThrow(() -> new EcoRideException("Reparação não encontrada."));
            total += r.getPreco();
        }
        c.getCod_reparacoes().addAll(codReparacoes);

        // Marcar stocks como usados em conserto
        for (Integer idStock : codStocks) {
            Stock s = sStock.obterDadosStock(idStock)
                    .orElseThrow(() -> new EcoRideException("Stock não encontrado."));
            if (s.getEstado() != EstadoStock.EM_STOCK)
                throw new EcoRideException("Stock " + idStock + " não está disponível.");
            ((SStockFacade) sStock).marcarUsadoEmConserto(idStock);
            Peca p = sStock.obterDadosPeca(s.getCodPeca())
                    .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
            total += p.getPreco_venda();
            c.getCod_pecas().add(idStock);
        }
        c.setPreco_total(total);
        os.setConserto(c);
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void adicionarPecaConsertoOS(int idOS, int idStock) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getEstado() != EstadoOS.PENDENTE_REPARACAO && os.getEstado() != EstadoOS.AGUARDAR_PECAS)
            throw new EcoRideException("Não é possível adicionar peças neste estado.");
        if (os.getConserto() == null)
            throw new EcoRideException("OS sem conserto. Registe o conserto primeiro.");

        Stock s = sStock.obterDadosStock(idStock)
                .orElseThrow(() -> new EcoRideException("Stock não encontrado."));
        if (s.getEstado() != EstadoStock.EM_STOCK)
            throw new EcoRideException("Stock não disponível.");
        ((SStockFacade) sStock).marcarUsadoEmConserto(idStock);

        Peca p = sStock.obterDadosPeca(s.getCodPeca())
                .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
        os.getConserto().getCod_pecas().add(idStock);
        os.getConserto().setPreco_total(os.getConserto().getPreco_total() + p.getPreco_venda());
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void removerPecaConsertoOS(int idOS, int idStock) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getConserto() == null)
            throw new EcoRideException("OS sem conserto.");
        if (!os.getConserto().getCod_pecas().remove(Integer.valueOf(idStock)))
            throw new EcoRideException("Peça não estava neste conserto.");

        Stock s = sStock.obterDadosStock(idStock)
                .orElseThrow(() -> new EcoRideException("Stock não encontrado."));
        Peca p = sStock.obterDadosPeca(s.getCodPeca())
                .orElseThrow(() -> new EcoRideException("Peça não encontrada."));
        sStock.atualizaEstadoStock(idStock, EstadoStock.EM_STOCK);
        
        os.getConserto().setPreco_total(os.getConserto().getPreco_total() - p.getPreco_venda());
        ordensServicoDAO.put(os.getId(), os);
    }

    @Override
    public void validarChecklistConsertoOS(int idOS, Checklist checklist) {
        Validacoes.naoNulo(checklist, "Checklist");
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getEstado() != EstadoOS.PENDENTE_REPARACAO)
            throw new EcoRideException("Checklist só pode ser validada em PENDENTE_REPARACAO.");
        if (os.getConserto() == null)
            throw new EcoRideException("OS sem conserto.");
        if (!checklist.tudoValidado())
            throw new EcoRideException("Checklist não está completamente validada.");

        os.getConserto().setCheckList(checklist);
        ordensServicoDAO.put(os.getId(), os);
        // Concluir reparação → pendente pagamento
        alterarEstadoOS(idOS, EstadoOS.PENDENTE_PAGAMENTO);
    }

    // ---- Pagamento ----

    @Override
    public boolean clienteNaoTemPagamentosPendentes(int idCliente, int idOS) {
        OrdemServico atual = obterOuFalhar(idOS);
        LocalDateTime referencia = atual.getData_criacao();
        for (OrdemServico os : ordensServicoDAO.obterPorCliente(idCliente)) {
            if (os.getId() == idOS) continue;
            if (os.getEstado() == EstadoOS.PENDENTE_PAGAMENTO &&
                    os.getData_criacao() != null &&
                    referencia != null &&
                    os.getData_criacao().isBefore(referencia)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void registarPagamentoOS(int idOS) {
        OrdemServico os = obterOuFalhar(idOS);
        if (os.getEstado() != EstadoOS.PENDENTE_PAGAMENTO)
            throw new EcoRideException("OS não está em estado PENDENTE_PAGAMENTO.");
        if (!clienteNaoTemPagamentosPendentes(os.getCodCliente(), idOS))
            throw new EcoRideException("Cliente tem pagamentos anteriores pendentes.");

        // Garante que existe notificação de término da OS (entre utilizadores internos)
        boolean temNotificacaoTermino = sNotificacoes
                .obterNotificacoesPorIntervalo(os.getData_criacao(), LocalDateTime.now())
                .stream()
                .anyMatch((Notificacao n) -> n.getDescricao() != null
                        && n.getDescricao().contains("OS #" + os.getId())
                        && n.getDescricao().toLowerCase().contains("conclu"));
        if (!temNotificacaoTermino)
            throw new EcoRideException("Notificação de término da OS não existe.");

        Conserto c = os.getConserto();
        if (c == null)
            throw new EcoRideException("OS sem conserto registado.");

        // Movimentos financeiros: lucro mão-de-obra + lucro venda peças
        float maoObra = 0;
        for (Integer idR : c.getCod_reparacoes()) {
            Reparacao r = sReparacoes.obterDadosReparacao(idR).orElseThrow();
            maoObra += r.getPreco();
        }
        float lucroPecas = 0;
        for (Integer idStock : c.getCod_pecas()) {
            Stock s = sStock.obterDadosStock(idStock).orElseThrow();
            Peca p = sStock.obterDadosPeca(s.getCodPeca()).orElseThrow();
            lucroPecas += p.getPreco_venda() - s.getPreco_compra();
        }
        if (maoObra > 0)
            sFinanceiro.criarMovimentoFinanceiro(maoObra, LocalDateTime.now(),
                    "Mão-de-obra OS #" + os.getId(), TipoMovimento.LUCRO_MAO_OBRA, os.getId());
        if (lucroPecas > 0)
            sFinanceiro.criarMovimentoFinanceiro(lucroPecas, LocalDateTime.now(),
                    "Lucro peças OS #" + os.getId(), TipoMovimento.LUCRO_VENDA_PECAS, os.getId());

        alterarEstadoOS(idOS, EstadoOS.PAGA);
    }

    // ---- Filtros ----

    @Override
    public List<OrdemServico> filtrarOSs(LocalDateTime desde, LocalDateTime ate, Integer idCliente, Integer idFuncionario) {
        return ordensServicoDAO.values().stream()
                .filter(os -> desde == null || (os.getData_criacao() != null && !os.getData_criacao().isBefore(desde)))
                .filter(os -> ate == null || (os.getData_criacao() != null && !os.getData_criacao().isAfter(ate)))
                .filter(os -> idCliente == null || os.getCodCliente() == idCliente)
                .filter(os -> idFuncionario == null || os.getCodResponsavel() == idFuncionario)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdemServico> filtrarOSsPorCliente(int idCliente) {
        return ordensServicoDAO.obterPorCliente(idCliente);
    }

    @Override
    public List<OrdemServico> filtrarOSsPorFuncionario(int idFuncionario) {
        return ordensServicoDAO.obterPorFuncionario(idFuncionario);
    }

    @Override
    public List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate) {
        return filtrarOSs(desde, ate, null, null);
    }

    @Override
    public List<OrdemServico> filtrarOSsPorClienteEIntervalo(int idCliente, LocalDateTime desde, LocalDateTime ate) {
        return filtrarOSs(desde, ate, idCliente, null);
    }

    @Override
    public List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int idFuncionario, LocalDateTime desde, LocalDateTime ate) {
        return filtrarOSs(desde, ate, null, idFuncionario);
    }

    @Override
    public List<OrdemServico> filtrarOSsPorClienteEFuncionario(int idCliente, int idFuncionario) {
        return filtrarOSs(null, null, idCliente, idFuncionario);
    }

    @Override
    public boolean validarFotografia(Fotografia f) {
        if (f == null) return false;
        if (f.getConteudo() == null || f.getConteudo().length == 0) return false;
        if (f.getFormato() == null || f.getFormato().isBlank()) return false;
        String fmt = f.getFormato().toLowerCase();
        if (!fmt.equals("jpg") && !fmt.equals("jpeg") && !fmt.equals("png")) return false;
        if (f.getTamanho() <= 0 || f.getTamanho() > 10L * 1024 * 1024) return false;
        return true;
    }
}
