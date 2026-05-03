package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sFuncionarios.FuncionarioDAO;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;

public class Diagnostico {
     private String descricao;
     private float orcamento;
     private int codMecanico;
     private List<Integer> cod_reparacoes;
     private List<Integer> cod_pecas;
     private List<PecasOrcamento> pecasOrcamento;

     private static final FuncionarioDAO funcionariosDAO = FuncionarioDAO.getInstance();
     private static final ReparacaoDAO reparacoesDAO = ReparacaoDAO.getInstance();
     

     public Diagnostico(String descricao, float orcamento, int codMecanico) {
          this.descricao = descricao;
          this.orcamento = orcamento;
          this.codMecanico = codMecanico;
          this.cod_reparacoes = new ArrayList<>();
          this.cod_pecas = new ArrayList<>();
          this.pecasOrcamento = new ArrayList<>();
     }

     public Diagnostico(String descricao, float orcamento, int codMecanico, List<Integer> cod_reparacoes, List<Integer> cod_pecas, List<PecasOrcamento> pecasOrcamento) {
          this.descricao = descricao;
          this.orcamento = orcamento;
          this.codMecanico = codMecanico;
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.cod_pecas = new ArrayList<>(cod_pecas);
          this.pecasOrcamento = new ArrayList<>(pecasOrcamento);
     }

     public Diagnostico(Diagnostico diagnostico) {
          this.descricao = diagnostico.descricao;
          this.orcamento = diagnostico.orcamento;
          this.codMecanico = diagnostico.codMecanico;
          this.cod_reparacoes = new ArrayList<>(diagnostico.cod_reparacoes);
          this.cod_pecas = new ArrayList<>(diagnostico.cod_pecas);
          this.pecasOrcamento = new ArrayList<>(diagnostico.pecasOrcamento);
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public float getOrcamento() {
          return orcamento;
     }

     public void setOrcamento(float orcamento) {
          this.orcamento = orcamento;
     }

     public int getCodMecanico() {
          return codMecanico;
     }

     public void setCodMecanico(int codMecanico) {
          this.codMecanico = codMecanico;
     }

     public List<Integer> getCod_reparacoes() {
          return new ArrayList<>(cod_reparacoes);
     }

     public void setCod_reparacoes(List<Integer> cod_reparacoes) {
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
     }

     public List<Integer> getCod_pecas() {
          return new ArrayList<>(cod_pecas);
     }

     public void setCod_pecas(List<Integer> cod_pecas) {
          this.cod_pecas = new ArrayList<>(cod_pecas);
     }

     public List<PecasOrcamento> getPecasOrcamento() {
          return new ArrayList<>(pecasOrcamento);
     }

     public void setPecasOrcamento(List<PecasOrcamento> pecasOrcamento) {
          this.pecasOrcamento = new ArrayList<>(pecasOrcamento);
     }

}
