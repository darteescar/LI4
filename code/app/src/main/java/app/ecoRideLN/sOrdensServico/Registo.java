package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Registo {

    private String descricao;
    private LocalDateTime dataCriacao;
    private int codTrotinete;
    private int codCliente;
    private int codCriador;
    private List<String> acessorios;

    public Registo(String descricao, LocalDateTime dataCriacao, int codTrotinete, int codCliente, int codCriador, List<String> acessorios) {
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.codTrotinete = codTrotinete;
        this.codCliente = codCliente;
        this.codCriador = codCriador;
        this.acessorios = acessorios == null ? new ArrayList<>() : new ArrayList<>(acessorios);
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public int getCodTrotinete() { return codTrotinete; }
    public void setCodTrotinete(int codTrotinete) { this.codTrotinete = codTrotinete; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public int getCodCriador() { return codCriador; }
    public void setCodCriador(int codCriador) { this.codCriador = codCriador; }

    public List<String> getAcessorios() { return new ArrayList<>(acessorios); }
    public void setAcessorios(List<String> acessorios) {
        this.acessorios = acessorios == null ? new ArrayList<>() : new ArrayList<>(acessorios);
    }
}
