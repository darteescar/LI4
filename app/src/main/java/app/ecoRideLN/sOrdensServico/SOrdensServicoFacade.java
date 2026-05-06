package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Peca;

public class SOrdensServicoFacade implements ISOrdensServico {

    private final OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();
    private final PecaDAO pecaDAO = PecaDAO.getInstance();

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
    public List<OrdemServico> obterTodasOSs() {
        return new ArrayList<>(ordemServicoDAO.values());
    }

    @Override
    public boolean removerOS(int id) {
        return ordemServicoDAO.remove(id) != null;
    }

    // ------------------- Máquina de estados -------------------

    @Override
    public void alterarEstadoOS(int id, EstadoOS novoEstado) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os == null) {
            throw new EcoRideException("OS " + id + " não encontrada.");
        }
        if (!os.getEstado().podeTransicionar(novoEstado)) {
            throw new EcoRideException("Transição inválida: " + os.getEstado() + " → " + novoEstado);
        }
        os.setEstado(novoEstado);
        ordemServicoDAO.put(id, os);
    }

    @Override
    public void submeterDiagnosticoOS(int id) {
        alterarEstadoOS(id, EstadoOS.PendenteAprovacaoOrcamento);
    }

    @Override
    public void aprovarOrcamentoOS(int id) {
        alterarEstadoOS(id, EstadoOS.PendenteReparacao);
    }

    @Override
    public void rejeitarOrcamentoOS(int id) {
        alterarEstadoOS(id, EstadoOS.OrcamentoNaoAprovado);
    }

    @Override
    public void marcarAguardarPecasOS(int id) {
        alterarEstadoOS(id, EstadoOS.AguardarPecas);
    }

    @Override
    public void pecasRecebidasOS(int id) {
        alterarEstadoOS(id, EstadoOS.PendenteReparacao);
    }

    @Override
    public void concluirReparacaoOS(int id) {
        alterarEstadoOS(id, EstadoOS.PendentePagamento);
    }

    @Override
    public void pagarOS(int id) {
        alterarEstadoOS(id, EstadoOS.Paga);
    }

    @Override
    public void eliminarOS(int id) {
        alterarEstadoOS(id, EstadoOS.Eliminada);
    }

    @Override
    public void atribuirOS(int id, int id_mecanico) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null) {
            os.setCodMecanico(id_mecanico);
            ordemServicoDAO.put(id, os);
        }
    }

    @Override
    public void registarNotificacaoPagamentoOS(int id_OS) {
        alterarEstadoOS(id_OS, EstadoOS.ClienteNotificado);
    }

    // ------------------- Diagnóstico -------------------
    @Override
    public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, float orcamento, String descricao) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null) {
            Diagnostico diag = new Diagnostico(descricao, reparacoes, listPecas, orcamento);
            os.setDiagnostico(diag);
            ordemServicoDAO.put(idOS, os);
        }
    }

    @Override
    public List<Integer> obterReparacoesDiagnosticoOS(int idOS) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null && os.getDiagnostico() != null) {
            return os.getDiagnostico().getCod_reparacoes();
        }
        return new ArrayList<>();
    }

    @Override
    public List<PecasOrcamento> obterPecasDiagnosticoOS(int idOS) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null && os.getDiagnostico() != null) {
            return os.getDiagnostico().getPecasOrcamento();
        }
        return new ArrayList<>();
    }

    // ------------------- Conserto -------------------

    @Override
    public void registarConsertoOS(int id_OS, List<PecasUsadas> pecas, List<Integer> reparacoes, float orcamento) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            Conserto con = new Conserto(pecas, reparacoes, orcamento);
            os.setConserto(con);
            ordemServicoDAO.put(id_OS, os);
        }
    }

    @Override
    public List <Integer> obterReparacoesConsertoOS(int id_OS) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null && os.getConserto() != null) {
            return os.getConserto().getCod_reparacoes();
        }
        return new ArrayList<>();
    }

    @Override
    public List <PecasUsadas> obterPecasUsadasConsertoOS(int id_OS) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null && os.getConserto() != null) {
            return os.getConserto().getListaPecas();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean validarChecklist_ConsertoOS(int idOS) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null && os.getConserto() != null) {
            Conserto con = os.getConserto();
            con.validarChecklist();
            os.setConserto(con);
            ordemServicoDAO.put(idOS, os);
            return true;
        }
        return false;
    }

    // ------------------- Pagamento -------------------

    @Override
    public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null) {
            throw new EcoRideException("OS " + id_OS + " não encontrada.");
        }
        os.setMetodo_pagamento(metodo_pagamento);
        pagarOS(id_OS);
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
