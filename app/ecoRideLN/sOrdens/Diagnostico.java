package app.ecoRideLN.sOrdens;

import app.ecoRideLN.sStock.PecasDoOrcamento;

import java.util.ArrayList;
import java.util.List;

public class Diagnostico {

    private String descricao;
    private float orcamento;
    private int codMecanico;
    private final List<Integer> cod_reparacoes;
    private final List<PecasDoOrcamento> listaPecas;

    public Diagnostico(String descricao, float orcamento, int codMecanico) {
        this.descricao = descricao;
        this.orcamento = orcamento;
        this.codMecanico = codMecanico;
        this.cod_reparacoes = new ArrayList<>();
        this.listaPecas = new ArrayList<>();
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public float getOrcamento() { return orcamento; }
    public void setOrcamento(float orcamento) { this.orcamento = orcamento; }

    public int getCodMecanico() { return codMecanico; }
    public void setCodMecanico(int codMecanico) { this.codMecanico = codMecanico; }

    public List<Integer> getCod_reparacoes() { return cod_reparacoes; }
    public List<PecasDoOrcamento> getListaPecas() { return listaPecas; }
}
