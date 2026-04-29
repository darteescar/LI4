package app.ecoRideLN.sOrdens;

import java.util.ArrayList;
import java.util.List;

public class Conserto {

    private float preco_total;
    private int codMecanico;
    private final List<Integer> cod_reparacoes;
    private final List<Integer> cod_pecas;
    private Checklist checkList;

    public Conserto(int codMecanico) {
        this.codMecanico = codMecanico;
        this.preco_total = 0;
        this.cod_reparacoes = new ArrayList<>();
        this.cod_pecas = new ArrayList<>();
        this.checkList = null;
    }

    public float getPreco_total() { return preco_total; }
    public void setPreco_total(float preco_total) { this.preco_total = preco_total; }

    public int getCodMecanico() { return codMecanico; }
    public void setCodMecanico(int codMecanico) { this.codMecanico = codMecanico; }

    public List<Integer> getCod_reparacoes() { return cod_reparacoes; }
    public List<Integer> getCod_pecas() { return cod_pecas; }

    public Checklist getCheckList() { return checkList; }
    public void setCheckList(Checklist checkList) { this.checkList = checkList; }
}
