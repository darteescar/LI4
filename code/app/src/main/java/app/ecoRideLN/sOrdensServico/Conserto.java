package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

public class Conserto {

     private float preco_total;
     private List<Integer> codStocks;
     private List<Integer> cod_reparacoes;
     private CheckList checkList;

     public Conserto(List<Integer> codStocks, List<Integer> cod_reparacoes, float preco_total) {
          this.preco_total = preco_total;
          this.codStocks = new ArrayList<>(codStocks);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.checkList = new CheckList();
     }

     public Conserto(Conserto conserto) {
          this.preco_total = conserto.preco_total;
          this.codStocks = new ArrayList<>(conserto.codStocks);
          this.cod_reparacoes = new ArrayList<>(conserto.cod_reparacoes);
          this.checkList = new CheckList(conserto.checkList);
     }

     public Conserto() {
          this.preco_total = 0;
          this.codStocks = new ArrayList<>();
          this.cod_reparacoes = new ArrayList<>();
          this.checkList = new CheckList();
     }

     public float getPreco_total() {
          return preco_total;
     }

     public void setPreco_total(float preco_total) {
          this.preco_total = preco_total;
     }

     public List<Integer> getCodStocks() {
          return new ArrayList<>(this.codStocks);
     }

     public void setCodStocks(List<Integer> codStocks) {
          this.codStocks = new ArrayList<>(codStocks);
     }

     public List<Integer> getCod_reparacoes() {
          return new ArrayList<>(this.cod_reparacoes);
     }

     public void setCod_reparacoes(List<Integer> cod_reparacoes) {
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
     }

     public CheckList getCheckList() {
          return new CheckList(checkList);
     }

     public void setCheckList(CheckList checkList) {
          this.checkList = checkList;
     }

     public void validarChecklist() {
          this.checkList.validarChecklist();
     }
}
