package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sFuncionarios.FuncionarioDAO;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import app.ecoRideLN.sReparacoes.Reparacao;

public class Diagnostico {
     private String descricao;
     private float orcamento;
     private int codMecanico;
     private List<PecasOrcamento> pecasOrcamento;
     private List<Integer> cod_reparacoes;

     private static final FuncionarioDAO funcionariosDAO = FuncionarioDAO.getInstance();
     private static final ReparacaoDAO reparacoesDAO = ReparacaoDAO.getInstance();
     

     public Diagnostico(String descricao, float orcamento, int codMecanico) {
          this.descricao = descricao;
          this.orcamento = orcamento;
          this.codMecanico = codMecanico;
          this.pecasOrcamento = new ArrayList<>();
          this.cod_reparacoes = new ArrayList<>();
     }

     public Diagnostico(String descricao, int codMecanico, List<Integer> cod_reparacoes, List<PecasOrcamento> pecasOrcamento) {
          this.descricao = descricao;
          this.codMecanico = codMecanico;
          this.pecasOrcamento = new ArrayList<>(pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.orcamento = 0;
          for (Integer cod_reparacao : cod_reparacoes) {
               Reparacao reparacao = reparacoesDAO.get(cod_reparacao);
               if (reparacao != null) {
                    this.orcamento += reparacao.getPreco();
               }
          }
          for (PecasOrcamento p : pecasOrcamento) {
               this.orcamento += p.calcularValor();
          }
     }

     public Diagnostico(Diagnostico diagnostico) {
          this.descricao = diagnostico.descricao;
          this.orcamento = diagnostico.orcamento;
          this.codMecanico = diagnostico.codMecanico;
          this.pecasOrcamento = new ArrayList<>(diagnostico.pecasOrcamento);
          this.cod_reparacoes = new ArrayList<>(diagnostico.cod_reparacoes);
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

     public List<PecasOrcamento> getPecasOrcamento() {
          return new ArrayList<>(pecasOrcamento);
     }

     public void setPecasOrcamento(List<PecasOrcamento> pecasOrcamento) {
          this.pecasOrcamento = new ArrayList<>(pecasOrcamento);
     }

     public List<Integer> getCod_reparacoes() {
          return new ArrayList<>(cod_reparacoes);
     }

     public void setCod_reparacoes(List<Integer> cod_reparacoes) {
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
     }

     // Métodos a mais

     public List<Reparacao> obterReparacoesDiagnostico() {
          List<Reparacao> reparacoes = new ArrayList<>();
          for (Integer cod_reparacao : cod_reparacoes) {
               Reparacao reparacao = reparacoesDAO.get(cod_reparacao);
               if (reparacao != null) {
                    reparacoes.add(reparacao);
               }
          }
          return reparacoes;
     }

}