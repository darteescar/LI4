package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;

public class Notificacao {

    private int id;
    private String descricao;
    private LocalDateTime data_emissao;
    private int idUtilizadorRemetente;
    private int idUtilizadorDestinatario;
    private boolean notificacao_tratada;
    private LocalDateTime data_horaTratada;

    public Notificacao(int id, String descricao, LocalDateTime data_emissao,
                       int idUtilizadorRemetente, int idUtilizadorDestinatario) {
        this.id = id;
        this.descricao = descricao;
        this.data_emissao = data_emissao;
        this.idUtilizadorRemetente = idUtilizadorRemetente;
        this.idUtilizadorDestinatario = idUtilizadorDestinatario;
        this.notificacao_tratada = false;
        this.data_horaTratada = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getData_emissao() { return data_emissao; }
    public void setData_emissao(LocalDateTime data_emissao) { this.data_emissao = data_emissao; }

    public int getIdUtilizadorRemetente() { return idUtilizadorRemetente; }
    public void setIdUtilizadorRemetente(int idUtilizadorRemetente) { this.idUtilizadorRemetente = idUtilizadorRemetente; }

    public int getIdUtilizadorDestinatario() { return idUtilizadorDestinatario; }
    public void setIdUtilizadorDestinatario(int idUtilizadorDestinatario) { this.idUtilizadorDestinatario = idUtilizadorDestinatario; }

    public boolean isNotificacao_tratada() { return notificacao_tratada; }
    public void setNotificacao_tratada(boolean notificacao_tratada) { this.notificacao_tratada = notificacao_tratada; }

    public LocalDateTime getData_horaTratada() { return data_horaTratada; }
    public void setData_horaTratada(LocalDateTime data_horaTratada) { this.data_horaTratada = data_horaTratada; }
}
