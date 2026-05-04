package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Stock;


public class SOrdensServicoFacade implements ISOrdensServico {

     private final OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();
     private final TrotineteDAO trotineteDAO = TrotineteDAO.getInstance();
     private final ClienteDAO clienteDAO = ClienteDAO.getInstance();

     @Override
     public OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao) {
          if (clienteDAO.get(id_cliente) == null || trotineteDAO.get(id_trotinete) == null) {
               return null;
          }
          int id = ordemServicoDAO.generateNewId();
          OrdemServico os = new OrdemServico(id, descricao, LocalDateTime.now(), id_trotinete, id_cliente, codResponsavel);
          ordemServicoDAO.put(id, os);
          return os;
     }

     @Override
     public OrdemServico registarOS_Extras(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, List<PecasOrcamento> pecasOrcamento) {
          if (clienteDAO.get(id_cliente) == null || trotineteDAO.get(id_trotinete) == null) {
               return null;
          }
          int id = ordemServicoDAO.generateNewId();
          OrdemServico os = new OrdemServico(id, descricao, LocalDateTime.now(), id_trotinete, id_cliente, codResponsavel, fotografias, acessorios);
          ordemServicoDAO.put(id, os);
          return os;
     }

     @Override
     public OrdemServico obterDadosOS(int id) {
          return ordemServicoDAO.get(id);
     }

     @Override
     public boolean removerOS(int id) {
          return ordemServicoDAO.remove(id) != null;
     }

     @Override
     public void alterarDescricaoOS(int id, String descricao) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setDescricao(descricao);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarAcessoriosOS(int id, List<String> acessorios) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setAcessorios(acessorios);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarFotografiasOS(int id, List<Fotografia> fotografias) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setFotografias(fotografias);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarEstadoOS(int id, EstadoOS estado) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setEstado(estado);
               ordemServicoDAO.put(id, os);
          }
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
     public boolean clienteNaoTemPagamentosPendentes(int id) {
          List<OrdemServico> osList = ordemServicoDAO.getOSDoCliente(id);
          int numero = 0;
          for (OrdemServico os : osList) {
               if (os.getEstado() == EstadoOS.PendentePagamento) {
                    numero++;
               }
          }
          return numero == 1;
     }

     @Override
     public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, int id_cliente, int id_funcionario) {
          return ordemServicoDAO.filtrarOSs(estado, desde, ate, id_cliente, id_funcionario);
     }

     @Override
     public float calcularOrcamento(List<PecasOrcamento> listaPecas, List<Reparacao> reparacoes) {
          float total = 0;
          for (PecasOrcamento p : listaPecas) {
               total += p.calcularValor();
          }
          for (Reparacao r : reparacoes) {
               total += r.getPreco();
          }
          return total;
     }

     @Override
     public boolean pecasDiagnosticoDisponiveisReparacao(int id_OS) {
          return false;
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
     public boolean validarFotografia(Fotografia foto) {
          return foto.isValid();
     }

     @Override
     public void alterarDataCriacaoOS(int id, LocalDateTime data_criacao) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setDataCriacao(data_criacao);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarFuncionarioResponsavelOS(int id, int codResponsavel) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setCodResponsavel(codResponsavel);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarClienteOS(int id, int id_cliente) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setCodCliente(id_cliente);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public void alterarTrotineteOS(int id, int id_trotinete) {
          OrdemServico os = ordemServicoDAO.get(id);
          if (os != null) {
               os.setCodTrotinete(id_trotinete);
               ordemServicoDAO.put(id, os);
          }
     }

     @Override
     public List<OrdemServico> filtrarOSsPorCliente(int id_cliente) {
          return ordemServicoDAO.filtrarOSs(null, null, null, id_cliente, null);
     }

     @Override
     public List<OrdemServico> filtrarOSsPorFuncionario(int id_funcionario) {
          return ordemServicoDAO.filtrarOSs(null, null, null, null, id_funcionario);
     }

     @Override
     public List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate) {
          return ordemServicoDAO.filtrarOSs(null, desde, ate, null, null);
     }

     @Override
     public List<OrdemServico> filtrarOSsPorClienteEIntervalo(int id_cliente, LocalDateTime desde, LocalDateTime ate) {
          return ordemServicoDAO.filtrarOSs(null, desde, ate, id_cliente, null);
     }

     @Override
     public List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime desde, LocalDateTime ate) {
          return ordemServicoDAO.filtrarOSs(null, desde, ate, null, id_funcionario);
     }

     @Override
     public List<OrdemServico> filtrarOSsPorClienteEFuncionario(int id_cliente, int id_funcionario) {
          return ordemServicoDAO.filtrarOSs(null, null, null, id_cliente, id_funcionario);
     }

     @Override
     public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, String descricao, int idMecanico) {
          OrdemServico os = ordemServicoDAO.get(idOS);
          if (os != null) {
               Diagnostico diag = new Diagnostico(descricao, idMecanico, reparacoes, listPecas);
               os.setDiagnostico(diag);
               ordemServicoDAO.put(idOS, os);
          }
     }

     @Override
     public List<Reparacao> obterReparacoesDiagnosticoOS(int idOS) {
          OrdemServico os = ordemServicoDAO.get(idOS);
          if (os != null && os.getDiagnostico() != null) {
               return os.getDiagnostico().obterReparacoesDiagnostico();
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

     @Override
     public void adicionarPecaConserto_OS(int id_OS, int id_Stock) {
          OrdemServico os = ordemServicoDAO.get(id_OS);
          if (os != null) {
               Conserto con = os.getConserto();
               if (con == null) return;
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
     public void registarConsertoOS(int id_OS, int idMecanico, List<Stock> pecas, List<Reparacao> reparacoes) {
          OrdemServico os = ordemServicoDAO.get(id_OS);
          if (os != null) {
               List<PecasUsadas> pecasUsadas = new ArrayList<>();
               for (Stock s : pecas) {
                    pecasUsadas.add(new PecasUsadas(s.getQuantidade(), s.getId()));
               }
               List<Integer> codReparacoes = new ArrayList<>();
               for (Reparacao r : reparacoes) {
                    codReparacoes.add(r.getId());
               }
               Conserto con = new Conserto(idMecanico, pecasUsadas, codReparacoes);
               os.setConserto(con);
               ordemServicoDAO.put(id_OS, os);
          }
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
}
