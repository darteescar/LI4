package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;

public class NotificacaoStock extends Notificacao {

     private int id_peca;

     public NotificacaoStock(int id, String descricao, int id_remetente, int id_destinatario, int id_peca) {
          super(id, descricao, id_remetente, id_destinatario);
          this.id_peca = id_peca;
     }

     public NotificacaoStock(int id, String descricao, LocalDateTime data_emissao, int id_remetente, int id_destinatario, EstadoNotificacao estado, LocalDateTime data_horaTratada, int id_peca) {
          super(id, descricao, data_emissao, id_remetente, id_destinatario, estado, data_horaTratada);
          this.id_peca = id_peca;
     }

     public int getId_peca() {
          return id_peca;
     }

     public void setId_peca(int id_peca) {
          this.id_peca = id_peca;
     }
}
