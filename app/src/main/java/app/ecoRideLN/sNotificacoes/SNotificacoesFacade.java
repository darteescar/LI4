package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideCD.sNotificacoes.NotificacoesDAO;

public class SNotificacoesFacade implements ISNotificacoes {
     private NotificacoesDAO notificacaoDAO = NotificacoesDAO.getInstance();

     @Override
     public Notificacao obterDadosNotificacao(int id) {
          return notificacaoDAO.get(id);
     }

     @Override
     public boolean existeNotificacao(int id) {
          return notificacaoDAO.containsKey(id);
     }

     @Override
     public boolean removerNotificacao(int id) {
          return notificacaoDAO.remove(id) != null;
     }

     @Override
     public void atualizarDescricaoNotificacao(int id, String descricao) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setDescricao(descricao);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public void atualizarDataEmissaoNotificacao(int id, LocalDateTime data_emissao) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setData_emissao(data_emissao);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public void atualizarIdRemetenteNotificacao(int id, int id_remetente) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setId_remetente(id_remetente);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public void atualizarIdDestinatarioNotificacao(int id, int id_destinatario) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setId_destinatario(id_destinatario);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public java.util.List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime data_inicio, LocalDateTime data_fim) {
          return notificacaoDAO.getByEmployeeAndDateRange(id_funcionario, data_inicio, data_fim);
     }

     @Override
     public List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime data_inicio, LocalDateTime data_fim) {
          return notificacaoDAO.getByDateRange(data_inicio, data_fim);
     }

     @Override
     public Notificacao criarNotificacao(String descricao, int id_remetente, int id_destinatario) {
          int id = notificacaoDAO.generateNewId();
          Notificacao notificacao = new Notificacao(id , descricao, id_remetente, id_destinatario);
          return notificacaoDAO.put(id, notificacao);
     }

     @Override
     public boolean sinalizarNotificacao_comoTratada(int id) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setNotificacao_tratada(true);
               notificacaoDAO.put(id, notificacao);
               return true;
          }
          return false;
     }

     @Override
     public void atualizarDataConfirmacaoTratamentoNotificacao(int id, LocalDateTime data) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setData_horaTratada(data);
               notificacaoDAO.put(id, notificacao);
          }
     }

}
