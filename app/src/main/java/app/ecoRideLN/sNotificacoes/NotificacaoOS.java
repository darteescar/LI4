package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;

public class NotificacaoOS extends Notificacao {
     private int id_os;

     public NotificacaoOS(int id, String descricao, int id_remetente, int id_destinatario, int id_os) {
          super(id, descricao, id_remetente, id_destinatario);
          this.id_os = id_os;
     }

     public NotificacaoOS(int id, String descricao, LocalDateTime data_emissao, int id_remetente, int id_destinatario, EstadoNotificacao estado, LocalDateTime data_horaTratada, int id_os) {
          super(id, descricao, data_emissao, id_remetente, id_destinatario, estado, data_horaTratada);
          this.id_os = id_os;
     }

     public int getId_os() { return id_os; }
     public void setId_os(int id_os) { this.id_os = id_os; }
}
