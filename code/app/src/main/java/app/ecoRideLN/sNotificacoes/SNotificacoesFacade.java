package app.ecoRideLN.sNotificacoes;

import java.util.List;

import app.ecoRideCD.sNotificacoes.NotificacoesDAO;

public class SNotificacoesFacade implements ISNotificacoes {
     private final NotificacoesDAO notificacaoDAO = NotificacoesDAO.getInstance();

     @Override
     public void registarNotificacaoOS(String descricao, int id_remetente,List<Integer> ids_destinatarios, int id_os) {

          if (descricao == null || descricao.isEmpty() || ids_destinatarios == null || ids_destinatarios.isEmpty()) {
               throw new IllegalArgumentException("Descrição e destinatários são obrigatórios.");
          }

          for (int iddest : ids_destinatarios) {
               NotificacaoOS notificacao = new NotificacaoOS(0, descricao, id_remetente, iddest, id_os);
               notificacaoDAO.insert(notificacao);
          }
     }

     @Override
     public void registarNotificacaoStock(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_peca) {
          if (descricao == null || descricao.isEmpty() || ids_destinatarios == null || ids_destinatarios.isEmpty()) {
               throw new IllegalArgumentException("Descrição e destinatários são obrigatórios.");
          }
          for (int iddest : ids_destinatarios) {
               NotificacaoStock notificacao = new NotificacaoStock(0, descricao, id_remetente, iddest, id_peca);
               notificacaoDAO.insert(notificacao);
          }
     }

     @Override
     public boolean removerNotificacao(int id, int idUser) {
          Notificacao notificacao = notificacaoDAO.get(id);

          if (notificacao != null && notificacao.getId_destinatario() == idUser) {
               return notificacaoDAO.remove(id) != null;
          }
          return false;
     }

     // Utilitários

     @Override
     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario) {
          return notificacaoDAO.getByDestinatario(id_destinatario);
     }

     @Override
     public boolean sinalizarNotificacao_comoTratada(int id, int idUser) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null && notificacao.getId_destinatario() == idUser) {
               notificacao.setNotificacao_tratada();
               notificacaoDAO.put(id, notificacao);
               return true;
          }
          return false;
     }

     @Override
     public boolean sinalizarNotificacao_comoLida(int id, int idUser) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null && notificacao.getId_destinatario() == idUser) {
               notificacao.setNotificacao_lida();
               notificacaoDAO.put(id, notificacao);
               return true;
          }
          return false;
     }
}
