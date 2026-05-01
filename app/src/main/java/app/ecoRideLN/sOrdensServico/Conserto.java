package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

public class Conserto {
     private float preco_total;
     private int codMecanico;
     private List<PecasUsadas> listaPecas;
     private List<Integer> cod_reparacoes;
     private CheckList checkList;

     private final static FuncionariosDAO funcionariosDAO = FuncionariosDAO.getInstance();
     private final static ReparacaoDAO reparacoesDAO = ReparacaoDAO.getInstance();

     public Conserto(float preco_total, int codMecanico, List<PecasUsadas> listaPecas, List<Integer> cod_reparacoes, CheckList checkList) {
          this.preco_total = preco_total;
          this.codMecanico = codMecanico;
          this.listaPecas = listaPecas;
          this.cod_reparacoes = cod_reparacoes;
          this.checkList = checkList;
     }

     public float getPreco_total() {
          return preco_total;
     }

     public void setPreco_total(float preco_total) {
          this.preco_total = preco_total;
     }

     public int getCodMecanico() {
          return codMecanico;
     }

     public void setCodMecanico(int codMecanico) {
          this.codMecanico = codMecanico;
     }

     public List<PecasUsadas> getListaPecas() {
          return new ArrayList<>(this.listaPecas);
     }

     public void setListaPecas(List<PecasUsadas> listaPecas) {
          this.listaPecas = new ArrayList<>(listaPecas);
     }

     public List<Integer> getCod_reparacoes() {
          return new ArrayList<>(this.cod_reparacoes);
     }

     public void setCod_reparacoes(List<Integer> cod_reparacoes) {
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
     }

     public CheckList getCheckList() {
          return checkList;
     }

     public void setCheckList(CheckList checkList) {
          this.checkList = checkList;
     }







}
