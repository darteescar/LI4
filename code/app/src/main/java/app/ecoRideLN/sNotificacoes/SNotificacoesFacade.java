package app.ecoRideLN.sNotificacoes;

import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sNotificacoes.NotificacoesDAO;

public class SNotificacoesFacade implements ISNotificacoes {
     private final NotificacoesDAO notificacaoDAO = NotificacoesDAO.getInstance();

     @Override
     public void registarNotificacaoOS(String descricao, int id_remetente,List<Integer> ids_destinatarios, int id_os) {
          for (int iddest : ids_destinatarios) {
               int id = notificacaoDAO.generateNewId();
               NotificacaoOS notificacao = new NotificacaoOS(id, descricao, id_remetente, iddest, id_os);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public void registarNotificacaoStock(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_peca) {
          for (int iddest : ids_destinatarios) {
               int id = notificacaoDAO.generateNewId();
               NotificacaoStock notificacao = new NotificacaoStock(id, descricao, id_remetente, iddest, id_peca);
               notificacaoDAO.put(id, notificacao);
          }
     }

     @Override
     public Notificacao obterNotificacao(int id) {
          return notificacaoDAO.get(id);
     }

     @Override
     public boolean removerNotificacao(int id) {
          return notificacaoDAO.remove(id) != null;
     }

     @Override
     public List<Notificacao> obterNotificacoes() {
          return new ArrayList<>(notificacaoDAO.values());
     }

     // Utilitários

     @Override
     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario) {
          return notificacaoDAO.getByDestinatario(id_destinatario);
     }

     @Override
     public List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario) {
          return notificacaoDAO.getUntreatedByDestinatario(id_destinatario);
     }

     @Override
     public boolean sinalizarNotificacao_comoTratada(int id) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setNotificacao_tratada();
               notificacaoDAO.put(id, notificacao);
               return true;
          }
          return false;
     }

     @Override
     public boolean sinalizarNotificacao_comoLida(int id) {
          Notificacao notificacao = notificacaoDAO.get(id);
          if (notificacao != null) {
               notificacao.setNotificacao_lida();
               notificacaoDAO.put(id, notificacao);
               return true;
          }
          return false;
     }
}
