package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.common.EcoRideException;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;

public class SOrdensServicoFacade implements ISOrdensServico {

    private final OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();

    @Override
    public OrdemServico registarOS(Registo registo) {
        if (registo.getCodCliente() <= 0 || registo.getCodTrotinete() <= 0)
            throw new EcoRideException("ID de cliente e trotinete devem ser positivos.");
        if (registo.getDescricao() == null || registo.getDescricao().isBlank())
            throw new EcoRideException("Descrição não pode ser vazia.");
        OrdemServico os = new OrdemServico(0, registo);
        ordemServicoDAO.insert(os);
        return os;
    }

    @Override
    public void atualizarOS(int id, Registo registo) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null && registo != null && !registo.getDescricao().isBlank()) {
            os.setRegisto(registo);
            ordemServicoDAO.put(id, os);
        }
    }

    @Override
    public OrdemServico obterOS(int id) {
        return ordemServicoDAO.get(id);
    }

    @Override
    public List<OrdemServico> obterOSs() {
        return new ArrayList<>(ordemServicoDAO.values());
    }

    @Override
    public List<OrdemServico> obterOSsAtivas() {
        return ordemServicoDAO.getOSsAtivas();
    }

    @Override
    public List<OrdemServico> obterOSsDisponiveis() {
        return ordemServicoDAO.getAvailableOSs();
    }

    @Override
    public boolean removerOS(int id) {
        return ordemServicoDAO.remove(id) != null;
    }

    // ------------------- Máquina de estados -------------------

    @Override
    public boolean alterarEstadoOS(int id, EstadoOS novoEstado) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os == null || !os.getEstado().podeTransicionar(novoEstado)) {
            return false;
        }
        os.setEstado(novoEstado);
        ordemServicoDAO.put(id, os);
        return true;
    }

    @Override
    public boolean aprovarOrcamentoOS(int id) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os == null)
            throw new EcoRideException("OS " + id + " não encontrada.");
        if (os.getDiagnostico() == null)
            throw new EcoRideException("Não é possível aprovar o orçamento sem um diagnóstico prévio.");
        if (!os.getEstado().podeTransicionar(EstadoOS.PendenteReparacao))
            return false;

        Diagnostico diag = os.getDiagnostico();
        diag.setAprovado(true);
        os.setDiagnostico(diag);
        os.setEstado(EstadoOS.PendenteReparacao);
        ordemServicoDAO.put(id, os);
        return true;
    }

    @Override
    public boolean rejeitarOrcamentoOS(int id) {
        return alterarEstadoOS(id, EstadoOS.OrcamentoNaoAprovado);
    }

    @Override
    public boolean marcarAguardarPecasOS(int id, int id_funcionario) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os == null)
            throw new EcoRideException("OS " + id + " não encontrada.");
        if (os.getCodMecanico() != id_funcionario)
            throw new EcoRideException("Não é o responsável por esta OS.");
        return alterarEstadoOS(id, EstadoOS.AguardarPecas);
    }

    @Override
    public boolean pecasRecebidasOS(int id) {
        return alterarEstadoOS(id, EstadoOS.PendenteReparacao);
    }

    @Override
    public boolean eliminarOS(int id) {
        return alterarEstadoOS(id, EstadoOS.Eliminada);
    }

    @Override
    public List<OrdemServico> obterOSs_Trotinete(int id_trotinete) {
        return ordemServicoDAO.getOSDoTrotinete(id_trotinete);
    }

    @Override
    public boolean atribuirOS(int id, int id_mecanico) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os == null) return false;
        if (os.getEstado() == EstadoOS.Paga || os.getEstado() == EstadoOS.Eliminada)
            throw new EcoRideException("Não é possível atribuir uma OS no estado " + os.getEstado() + ".");
        os.setCodMecanico(id_mecanico);
        ordemServicoDAO.put(id, os);
        return true;
    }

    @Override
    public boolean registarNotificacaoPagamentoOS(int id_OS) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null)
            throw new EcoRideException("OS " + id_OS + " não encontrada.");
        if (!os.getEstado().podeTransicionar(EstadoOS.ClienteNotificado))
            return false;
        // Criar registo de pagamento com a notificação; metodo e data de pagamento são preenchidos depois
        Pagamento pag = new Pagamento(true, LocalDateTime.now());
        os.setPagamento(pag);
        os.setEstado(EstadoOS.ClienteNotificado);
        ordemServicoDAO.put(id_OS, os);
        return true;
    }

    // ------------------- Diagnóstico -------------------

    @Override
    public Diagnostico registarDiagnosticoOS(int idOS, Map<Integer, Integer> pecasQuantidades, List<Integer> reparacoes, float orcamento, String descricao, int id_funcionario) {
        if (pecasQuantidades == null || reparacoes == null || descricao == null || descricao.isBlank())
            throw new EcoRideException("Dados de diagnóstico incompletos ou inválidos.");

        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null) {
            if (os.getCodMecanico() == null)
                os.setCodMecanico(id_funcionario);
            if (os.getCodMecanico() != id_funcionario)
                throw new EcoRideException("Não é o responsável por esta OS.");
            if (!os.getEstado().podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento))
                throw new EcoRideException("Transição de estado inválida para a OS " + idOS);

            Diagnostico diag = new Diagnostico(descricao, reparacoes, pecasQuantidades, orcamento);
            os.setDiagnostico(diag);
            os.setConserto(null);
            os.setEstado(EstadoOS.PendenteAprovacaoOrcamento);
            ordemServicoDAO.put(idOS, os);
            return diag;
        }
        throw new EcoRideException("OS " + idOS + " não encontrada.");
    }

    // ------------------- Conserto -------------------

    @Override
    public Conserto registarConsertoOS(int id_OS, Map<Integer, Integer> stocksUsados, List<Integer> reparacoes, float valor, int id_funcionario) {
        if (stocksUsados == null || reparacoes == null)
            throw new EcoRideException("Dados de conserto incompletos ou inválidos.");

        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            if (os.getCodMecanico() != id_funcionario)
                throw new EcoRideException("Não é o responsável por esta OS.");
            if (!os.getEstado().podeTransicionar(EstadoOS.PendentePagamento))
                throw new EcoRideException("Transição de estado inválida para a OS " + id_OS);
            if (os.getDiagnostico() == null)
                throw new EcoRideException("Não é possível registar um conserto sem um diagnóstico prévio.");
            if (!os.getDiagnostico().isAprovado())
                throw new EcoRideException("Não é possível registar um conserto sem um orçamento aprovado.");

            Conserto con = new Conserto(stocksUsados, reparacoes, valor);
            os.setConserto(con);
            os.setEstado(EstadoOS.PendentePagamento);
            ordemServicoDAO.put(id_OS, os);
            return con;
        }
        throw new EcoRideException("OS " + id_OS + " não encontrada.");
    }

    @Override
    public Map<Integer, Integer> obterStocksUsadosConsertoOS(int id_OS) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null || os.getConserto() == null) return Map.of();
        return os.getConserto().getStocksUsados();
    }

    // ------------------- Pagamento -------------------

    @Override
    public boolean registarPagamentoOS(int id_OS, Pagamento pagamento) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null)
            throw new EcoRideException("OS " + id_OS + " não encontrada.");
        if (os.getEstado() != EstadoOS.ClienteNotificado)
            throw new EcoRideException("O cliente deve ser notificado antes de registar o pagamento.");

        Pagamento existente = os.getPagamento();
        if (existente == null || !existente.isClienteNotificado())
            throw new EcoRideException("O cliente deve ser notificado antes de registar o pagamento.");
        if (!os.getEstado().podeTransicionar(EstadoOS.Paga))
            return false;

        // Preservar dados de notificação ao completar o pagamento
        pagamento.setClienteNotificado(existente.isClienteNotificado());
        pagamento.setDataNotificacao(existente.getDataNotificacao());
        os.setPagamento(pagamento);
        os.setEstado(EstadoOS.Paga);
        ordemServicoDAO.put(id_OS, os);
        return true;
    }

    // ------------------- Utilitários -------------------

    @Override
    public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario) {
        return ordemServicoDAO.filtrarOSs(estado, desde, ate, id_cliente, id_funcionario);
    }
}
