package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;

public class Notificacao {

     private int id;
     private String descricao;
     private LocalDateTime data_emissao;
     private int id_remetente;
     private int id_destinatario;
     private EstadoNotificacao estado;
     private LocalDateTime data_horaTratada;

     public Notificacao(int id, String descricao, int id_remetente, int id_destinatario) {
          this.id = id;
          this.descricao = descricao;
          this.data_emissao = LocalDateTime.now();
          this.id_remetente = id_remetente;
          this.id_destinatario = id_destinatario;
          this.estado = EstadoNotificacao.NAOLIDA;
          this.data_horaTratada = null;
     }

     public Notificacao(int id, String descricao, LocalDateTime data_emissao, int id_remetente, int id_destinatario, EstadoNotificacao estado, LocalDateTime data_horaTratada) {
          this.id = id;
          this.descricao = descricao;
          this.data_emissao = data_emissao;
          this.id_remetente = id_remetente;
          this.id_destinatario = id_destinatario;
          this.estado = estado;
          this.data_horaTratada = data_horaTratada;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public LocalDateTime getData_emissao() {
          return data_emissao;
     }

     public void setData_emissao(LocalDateTime data_emissao) {
          this.data_emissao = data_emissao;
     }

     public int getId_remetente() {
          return id_remetente;
     }

     public void setId_remetente(int id_remetente) {
          this.id_remetente = id_remetente;
     }

     public int getId_destinatario() {
          return id_destinatario;
     }

     public void setId_destinatario(int id_destinatario) {
          this.id_destinatario = id_destinatario;
     }

     public EstadoNotificacao getEstado() {
          return estado;
     }

     public LocalDateTime getData_horaTratada() {
          return data_horaTratada;
     }

     public void setData_horaTratada(LocalDateTime data_horaTratada) {
          this.data_horaTratada = data_horaTratada;
     }

     public void setNotificacao_lida() {
          this.estado = EstadoNotificacao.LIDA;
          this.data_horaTratada = LocalDateTime.now();
     }

     public void setNotificacao_tratada() {
          this.estado = EstadoNotificacao.TRATADA;
          this.data_horaTratada = LocalDateTime.now();
     }
}
