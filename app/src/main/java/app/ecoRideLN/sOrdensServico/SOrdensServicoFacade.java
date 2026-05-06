package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;

public class SOrdensServicoFacade implements ISOrdensServico {

    private final OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();
    private final TrotineteDAO trotineteDAO = TrotineteDAO.getInstance();
    private final ClienteDAO clienteDAO = ClienteDAO.getInstance();
    private final ReparacaoDAO reparacaoDAO = ReparacaoDAO.getInstance();
    private final PecaDAO pecaDAO = PecaDAO.getInstance();

    // ------------------- Registo -------------------
    @Override
    public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador) {
        if (clienteDAO.get(id_cliente) == null || trotineteDAO.get(id_trotinete) == null) {
            return null;
        }
        int id = ordemServicoDAO.generateNewId();
        OrdemServico os = new OrdemServico(id, descricao, LocalDateTime.now(), id_trotinete, id_cliente, codCriador, fotografias, acessorios);
        ordemServicoDAO.put(id, os);
        return os;
    }

    // ------------------- Consulta -------------------
    @Override
    public OrdemServico obterOS(int id) {
        return ordemServicoDAO.get(id);
    }

    @Override
    public List<OrdemServico> obterTodasOSs() {
        return new ArrayList<>(ordemServicoDAO.values());
    }

    @Override
    public boolean removerOS(int id) {
        return ordemServicoDAO.remove(id) != null;
    }

    // ------------------- Alterações de campos -------------------

    @Override
     public void alterarOS(int id, String descricao, List<String> acessorios, List<Fotografia> fotografias, int id_cliente, int id_trotinete) {
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

    // ------------------- Diagnóstico -------------------
    @Override
    public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Reparacao> reparacoes, String descricao) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null) {
            List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).toList();
            float orcamento = calcularOrcamento(listPecas, reparacoes);
            Diagnostico diag = new Diagnostico(descricao, codReps, listPecas, orcamento);
            os.setDiagnostico(diag);
            ordemServicoDAO.put(idOS, os);
        }
    }

    @Override
    public List<Reparacao> obterReparacoesDiagnosticoOS(int idOS) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null && os.getDiagnostico() != null) {
            List<Reparacao> result = new ArrayList<>();
            for (Integer cod : os.getDiagnostico().getCod_reparacoes()) {
                Reparacao r = reparacaoDAO.get(cod);
                if (r != null) {
                    result.add(r);
                }
            }
            return result;
        }
        return new ArrayList<>();
    }

    @Override
    public List<PecasOrcamento> obterPecasQuantidadeDiagnosticoOS(int idOS) {
        OrdemServico os = ordemServicoDAO.get(idOS);
        if (os != null && os.getDiagnostico() != null) {
            return os.getDiagnostico().getPecasOrcamento();
        }
        return new ArrayList<>();
    }

    // ------------------- Conserto -------------------
    @Override
    public void registarConsertoOS(int id_OS, List<Stock> pecas, List<Reparacao> reparacoes) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            List<PecasUsadas> pecasUsadas = pecas.stream()
                    .map(s -> new PecasUsadas(s.getQuantidade(), s.getId()))
                    .collect(java.util.stream.Collectors.toList());
            List<Integer> codReps = reparacoes.stream().map(Reparacao::getId).toList();
            float preco = 0;
            for (Reparacao r : reparacoes) {
                preco += r.getPreco();
            }
            for (Stock s : pecas) {
                Peca p = pecaDAO.get(s.getCodPeca());
                if (p != null) {
                    preco += s.getQuantidade() * p.getPreco_venda();
                }
            }
            Conserto con = new Conserto(pecasUsadas, codReps, preco);
            os.setConserto(con);
            ordemServicoDAO.put(id_OS, os);
        }
    }

    @Override
    public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os == null) {
            throw new EcoRideException("OS " + id_OS + " não encontrada.");
        }
        os.setMetodo_pagamento(metodo_pagamento);
        pagarOS(id_OS);
    }

    @Override
    public void adicionarPecas_Conserto_OS(int id, List<Stock> pecas) {
        OrdemServico os = ordemServicoDAO.get(id);
        if (os != null) {
            Conserto con = os.getConserto();
            if (con == null) {
                con = new Conserto();
            }
            con.adicionarPecas(pecas);
            os.setConserto(con);
            ordemServicoDAO.put(id, os);
        }
    }

    @Override
    public void adicionarPecaConserto_OS(int id_OS, int id_Stock) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null) {
            Conserto con = os.getConserto();
            if (con == null) {
                return;
            }
            List<PecasUsadas> pecas = con.getListaPecas();
            for (PecasUsadas p : pecas) {
                if (p.getCodStock() == id_Stock) {
                    p.setQuantidade(p.getQuantidade() + 1);
                    con.setListaPecas(pecas);
                    os.setConserto(con);
                    ordemServicoDAO.put(id_OS, os);
                    return;
                }
            }
            pecas.add(new PecasUsadas(1, id_Stock));
            con.setListaPecas(pecas);
            os.setConserto(con);
            ordemServicoDAO.put(id_OS, os);
        }
    }

    @Override
    public boolean removerPecaConserto_OS(int id_OS, int id_Stock) {
        OrdemServico os = ordemServicoDAO.get(id_OS);
        if (os != null && os.getConserto() != null) {
            List<PecasUsadas> pecas = os.getConserto().getListaPecas();
            for (int i = 0; i < pecas.size(); i++) {
                if (pecas.get(i).getCodStock() == id_Stock) {
                    pecas.remove(i);
                    Conserto con = os.getConserto();
                    con.setListaPecas(pecas);
                    os.setConserto(con);
                    ordemServicoDAO.put(id_OS, os);
                    return true;
                }
            }
        }
        return false;
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

    // ------------------- Filtragem -------------------
    @Override
    public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario) {
        return ordemServicoDAO.filtrarOSs(estado, desde, ate, id_cliente, id_funcionario);
    }

    // ------------------- Utilitários -------------------

    @Override
    public float calcularOrcamento(List<PecasOrcamento> listaPecas, List<Reparacao> reparacoes) {
        float total = 0;
        for (PecasOrcamento p : listaPecas) {
            Peca peca = pecaDAO.get(p.getCodPeca());
            if (peca != null) {
                total += p.getQuantidade() * peca.getPreco_venda();
            }
        }
        for (Reparacao r : reparacoes) {
            total += r.getPreco();
        }
        return total;
    }

    @Override
    public boolean validarFotografia(Fotografia foto) {
        return foto.isValid();
    }
}
