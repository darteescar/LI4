package app.ecoRideLN.sNotificacoes;

public class sNotificacoesFacade implements ISSNotificacoes {
     private NotificacaoDAO notificacaoDAO;

     public sNotificacoesFacade() {
          this.notificacaoDAO = NotificacaoDAO.getInstance();
     }

     
}
