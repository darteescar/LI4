package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

public class Diagnostico {
     private String descricao;
     private float orcamento;
     private int codMecanico;
     private List<PecasOrcamento> pecasOrcamento;
     private List<Integer> cod_reparacoes;

     public Diagnostico(String descricao, float orcamento, int codMecanico) {
          this.descricao = descricao;
          this.orcamento = orcamento;
          this.codMecanico = codMecanico;
          this.pecasOrcamento = new ArrayList<>();
          this.cod_reparacoes = new ArrayList<>();
     }

     public Diagnostico(String descricao, int codMecanico, List<Integer> cod_reparacoes, List<PecasOrcamento> pecasOrcamento, float orcamento) {
          this.descricao = descricao;
          this.codMecanico = codMecanico;
          this.pecasOrcamento = new ArrayList<>(pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.orcamento = orcamento;
     }

     public Diagnostico(Diagnostico diagnostico) {
          this.descricao = diagnostico.descricao;
          this.orcamento = diagnostico.orcamento;
          this.codMecanico = diagnostico.codMecanico;
          this.pecasOrcamento = new ArrayList<>(diagnostico.pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(diagnostico.cod_reparacoes);
     }

     public String getDescricao() { return descricao; }
     public void setDescricao(String descricao) { this.descricao = descricao; }

     public float getOrcamento() { return orcamento; }
     public void setOrcamento(float orcamento) { this.orcamento = orcamento; }

     public int getCodMecanico() { return codMecanico; }
     public void setCodMecanico(int codMecanico) { this.codMecanico = codMecanico; }

     public List<PecasOrcamento> getPecasOrcamento() { return new ArrayList<>(pecasOrcamento); }
     public void setPecasOrcamento(List<PecasOrcamento> pecasOrcamento) { this.pecasOrcamento = new ArrayList<>(pecasOrcamento); }

     public List<Integer> getCod_reparacoes() { return new ArrayList<>(cod_reparacoes); }
     public void setCod_reparacoes(List<Integer> cod_reparacoes) { this.cod_reparacoes = new ArrayList<>(cod_reparacoes); }
}
