package app.ecoRideLN.sNotificacoes;

import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sNotificacoes.NotificacoesDAO;

public class SNotificacoesFacade implements ISNotificacoes {
     private final NotificacoesDAO notificacaoDAO = NotificacoesDAO.getInstance();

     @Override
     public NotificacaoOS registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os) {
          int id = notificacaoDAO.generateNewId();
          NotificacaoOS notificacao = new NotificacaoOS(id, descricao, id_remetente, id_destinatario, id_os);
          notificacaoDAO.put(id, notificacao);
          return notificacao;
     }

     @Override
     public NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca) {
          int id = notificacaoDAO.generateNewId();
          NotificacaoStock notificacao = new NotificacaoStock(id, descricao, id_remetente, id_destinatario, id_peca);
          notificacaoDAO.put(id, notificacao);
          return notificacao;
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
