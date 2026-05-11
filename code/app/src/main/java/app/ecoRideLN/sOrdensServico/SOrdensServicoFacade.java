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
    public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador) {
        int id = ordemServicoDAO.generateNewId();
        OrdemServico os = new OrdemServico(id, descricao, LocalDateTime.now(), id_trotinete, id_cliente, codCriador, fotografias, acessorios);
        ordemServicoDAO.put(id, os);
        return os;
    }
        
    @Override
    public void atualizarOS(int id, String descricao, List<String> acessorios, List<Fotografia> fotografias, int id_cliente, int id_trotinete) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null) {
            if (descricao != null) os.setDescricao(descricao);
            if (acessorios != null) os.setAcessorios(acessorios);
            if (fotografias != null) os.setFotografias(fotografias);
            if (id_cliente != 0) os.setCodCliente(id_cliente);
            if (id_trotinete != 0) os.setCodTrotinete(id_trotinete);
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
    public boolean atribuirOS(int id, int id_mecanico) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null) {
            os.setCodMecanico(id_mecanico);
            ordemServicoDAO.put(id, os);
            return true;
        }
        return false;
    }

    @Override
    public boolean registarNotificacaoPagamentoOS(int id_OS) {
        return alterarEstadoOS(id_OS, EstadoOS.ClienteNotificado);
    }

    // ------------------- Diagnóstico -------------------
    @Override
    public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, float orcamento, String descricao, int id_funcionario) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null) {
            if (os.getCodMecanico() != id_funcionario) {
                throw new EcoRideException("Funcionário " + id_funcionario + " não é o responsável por esta OS.");
            }
            Diagnostico diag = new Diagnostico(descricao, reparacoes, listPecas, orcamento);
            os.setDiagnostico(diag);
            alterarEstadoOS(idOS, EstadoOS.PendenteAprovacaoOrcamento);
            ordemServicoDAO.put(idOS, os);
        }
    }

    // ------------------- Conserto -------------------
    @Override
    public void registarConsertoOS(int id_OS, Map<Integer, Integer> stocksUsados, List<Integer> reparacoes, float orcamento, int id_funcionario) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            if (os.getCodMecanico() != id_funcionario) {
                throw new EcoRideException("Funcionário " + id_funcionario + " não é o responsável por esta OS.");
            }
            Conserto con = new Conserto(stocksUsados, reparacoes, orcamento);
            os.setConserto(con);
            alterarEstadoOS(id_OS, EstadoOS.PendentePagamento);
            ordemServicoDAO.put(id_OS, os);
        }
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

    @Override
    public boolean validarFotografia(Fotografia foto) {
        return foto.isValid();
    }

}
