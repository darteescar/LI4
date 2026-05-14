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
    public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, int codCriador) {
        int id = ordemServicoDAO.generateNewId();
        if (id_cliente <= 0 || id_trotinete <= 0) throw new EcoRideException("ID de cliente e trotinete devem ser positivos.");
        if (descricao == null || descricao.isBlank()) throw new EcoRideException("Descrição não pode ser vazia.");
        OrdemServico os = new OrdemServico(id, descricao, LocalDateTime.now(), id_trotinete, id_cliente, codCriador, acessorios);
        ordemServicoDAO.put(id, os);
        return os;
    }
        
    @Override
    public void atualizarOS(int id, String descricao, List<String> acessorios, int id_cliente, int id_trotinete) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null && descricao != null && !descricao.isBlank() && (id_cliente > 0 || id_cliente == 0) && (id_trotinete > 0 || id_trotinete == 0)) {
            os.setDescricao(descricao);
            os.setAcessorios(acessorios);
            os.setCodCliente(id_cliente);
            os.setCodTrotinete(id_trotinete);
            ordemServicoDAO.put(id, os);
        }
    }

    @Override
    public OrdemServico obterOS(int id) {
        return ordemServicoDAO.get(id);
    }

    @Override
    public boolean existeOS(int id) {
        return ordemServicoDAO.containsKey(id);
    }

    @Override
    public List<OrdemServico> obterOSs() {
        return new ArrayList<>(ordemServicoDAO.values());
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
        return alterarEstadoOS(id, EstadoOS.PendenteReparacao);
    }

    @Override
    public boolean rejeitarOrcamentoOS(int id) {
        return alterarEstadoOS(id, EstadoOS.OrcamentoNaoAprovado);
    }

    @Override
    public boolean marcarAguardarPecasOS(int id) {
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
        return alterarEstadoOS(id_OS, EstadoOS.ClienteNotificado);
    }

    // ------------------- Diagnóstico -------------------
    @Override
    public Diagnostico registarDiagnosticoOS(int idOS, Map<Integer, Integer> pecasQuantidades, List<Integer> reparacoes, float orcamento, String descricao, int id_funcionario) {

        if (pecasQuantidades == null || reparacoes == null || descricao == null || descricao.isBlank()) {
            throw new EcoRideException("Dados de diagnóstico incompletos ou inválidos.");
        }

        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null) {
            if (os.getCodMecanico() != id_funcionario) {
                throw new EcoRideException("Funcionário " + id_funcionario + " não é o responsável por esta OS.");
            }
            if (!os.getEstado().podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento))
                throw new EcoRideException("Transição de estado inválida para a OS " + idOS);
            Diagnostico diag = new Diagnostico(descricao, reparacoes, pecasQuantidades, orcamento);
            os.setDiagnostico(diag);
            os.setEstado(EstadoOS.PendenteAprovacaoOrcamento);
            ordemServicoDAO.put(idOS, os);
            return diag;
        }
        throw new EcoRideException("OS " + idOS + " não encontrada.");
    }

    // ------------------- Conserto -------------------
    @Override
    public Conserto registarConsertoOS(int id_OS, Map<Integer, Integer> stocksUsados, List<Integer> reparacoes, float orcamento, int id_funcionario) {

        if (stocksUsados == null || reparacoes == null) {
            throw new EcoRideException("Dados de conserto incompletos ou inválidos.");
        }

        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            if (os.getCodMecanico() != id_funcionario) {
                throw new EcoRideException("Funcionário " + id_funcionario + " não é o responsável por esta OS.");
            }
            if (!os.getEstado().podeTransicionar(EstadoOS.PendentePagamento))
                throw new EcoRideException("Transição de estado inválida para a OS " + id_OS);
            Conserto con = new Conserto(stocksUsados, reparacoes, orcamento);
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
    public boolean registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null) {
            throw new EcoRideException("OS " + id_OS + " não encontrada.");
        }
        os.setMetodo_pagamento(metodo_pagamento);
        return alterarEstadoOS(id_OS, EstadoOS.Paga);
    }

    // ------------------- Utilitários -------------------

    @Override
    public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario) {
        return ordemServicoDAO.filtrarOSs(estado, desde, ate, id_cliente, id_funcionario);
    }

}
