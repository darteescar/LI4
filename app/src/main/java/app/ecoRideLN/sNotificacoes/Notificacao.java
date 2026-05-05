package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;

import app.ecoRideCD.sAutenticacao.UtilizadorDAO;

public class Notificacao {
     private int id;
     private String descricao;
     private LocalDateTime data_emissao;
     private int id_remetente;
     private int id_destinatario;
     private boolean notificacao_tratada;
     private boolean notificacao_lida;
     private LocalDateTime data_horaTratada;

     private static final UtilizadorDAO utilizadorDAO = UtilizadorDAO.getInstance();

     public Notificacao(int id, String descricao, int id_remetente, int id_destinatario) {
          this.id = id;
          this.descricao = descricao;
          this.data_emissao = LocalDateTime.now();
          this.id_remetente = id_remetente;
          this.id_destinatario = id_destinatario;
          this.notificacao_tratada = false;
          this.data_horaTratada = null;
          this.notificacao_lida = false;
     }

     public Notificacao(int id, String descricao, LocalDateTime data_emissao, int id_remetente, int id_destinatario, boolean notificacao_tratada, LocalDateTime data_horaTratada) {
          this.id = id;
          this.descricao = descricao;
          this.data_emissao = data_emissao;
          this.id_remetente = id_remetente;
          this.id_destinatario = id_destinatario;
          this.notificacao_tratada = notificacao_tratada;
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

     public boolean isNotificacao_tratada() {
          return notificacao_tratada;
     }

     public void setNotificacao_tratada(boolean notificacao_tratada) {
          this.notificacao_tratada = notificacao_tratada;
          this.data_horaTratada = LocalDateTime.now();
     }

     public LocalDateTime getData_horaTratada() {
          return data_horaTratada;
     }

     public void setData_horaTratada(LocalDateTime data_horaTratada) {
          this.data_horaTratada = data_horaTratada;
     }

     public boolean isNotificacao_lida() {
          return notificacao_lida;
     }

     public void setNotificacao_lida(boolean notificacao_lida) {
          this.notificacao_lida = notificacao_lida;
     }
}
