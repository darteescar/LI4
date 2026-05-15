package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Diagnostico {

     private String descricao;
     private float orcamento;
     private Map<Integer, Integer> pecasOrcamento; // codPeca -> quantidade
     private List<Integer> cod_reparacoes;
     private boolean aprovado;

     public Diagnostico(String descricao, float orcamento) {
          this.descricao = descricao;
          this.orcamento = orcamento;
          this.pecasOrcamento = new LinkedHashMap<>();
          this.cod_reparacoes = new ArrayList<>();
          this.aprovado = false;
     }

     public Diagnostico(String descricao, List<Integer> cod_reparacoes, Map<Integer, Integer> pecasOrcamento, float orcamento) {
          this.descricao = descricao;
          this.pecasOrcamento = new LinkedHashMap<>(pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.orcamento = orcamento;
     }

     public Diagnostico(String descricao, List<Integer> cod_reparacoes, Map<Integer, Integer> pecasOrcamento, float orcamento, boolean aprovado) {
          this.descricao = descricao;
          this.pecasOrcamento = new LinkedHashMap<>(pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.orcamento = orcamento;
          this.aprovado = aprovado;
     }

     public Diagnostico(Diagnostico diagnostico) {
          this.descricao = diagnostico.descricao;
          this.orcamento = diagnostico.orcamento;
          this.pecasOrcamento = new LinkedHashMap<>(diagnostico.pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(diagnostico.cod_reparacoes);
          this.aprovado = diagnostico.aprovado;
     }

     public String getDescricao() { return descricao; }
     public void setDescricao(String descricao) { this.descricao = descricao; }

     public float getOrcamento() { return orcamento; }
     public void setOrcamento(float orcamento) { this.orcamento = orcamento; }

     public Map<Integer, Integer> getPecasOrcamento() { return new LinkedHashMap<>(pecasOrcamento); }
     public void setPecasOrcamento(Map<Integer, Integer> pecasOrcamento) { this.pecasOrcamento = new LinkedHashMap<>(pecasOrcamento); }

     public List<Integer> getCod_reparacoes() { return new ArrayList<>(cod_reparacoes); }
     public void setCod_reparacoes(List<Integer> cod_reparacoes) { this.cod_reparacoes = new ArrayList<>(cod_reparacoes); }

     public boolean isAprovado() { return aprovado; }
     public void setAprovado(boolean aprovado) { this.aprovado = aprovado; }
}
