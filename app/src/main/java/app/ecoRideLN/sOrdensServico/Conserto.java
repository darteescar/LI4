package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.List;

import app.ecoRideLN.sStock.Stock;

public class Conserto {
     private float preco_total;
     private List<PecasUsadas> listaPecas;
     private List<Integer> cod_reparacoes;
     private CheckList checkList;

     public Conserto(List<PecasUsadas> listaPecas, List<Integer> cod_reparacoes, float preco_total) {
          this.preco_total = preco_total;
          this.listaPecas = new ArrayList<>(listaPecas);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.checkList = new CheckList();
     }

     public Conserto(Conserto conserto) {
          this.preco_total = conserto.preco_total;
          this.listaPecas = new ArrayList<>(conserto.listaPecas);
          this.cod_reparacoes = new ArrayList<>(conserto.cod_reparacoes);
          this.checkList = new CheckList(conserto.checkList);
     }

     public Conserto() {
          this.preco_total = 0;
          this.listaPecas = new ArrayList<>();
          this.cod_reparacoes = new ArrayList<>();
          this.checkList = new CheckList();
     }

     public float getPreco_total() { return preco_total; }
     public void setPreco_total(float preco_total) { this.preco_total = preco_total; }

     public List<PecasUsadas> getListaPecas() { return new ArrayList<>(this.listaPecas); }
     public void setListaPecas(List<PecasUsadas> listaPecas) { this.listaPecas = new ArrayList<>(listaPecas); }

     public List<Integer> getCod_reparacoes() { return new ArrayList<>(this.cod_reparacoes); }
     public void setCod_reparacoes(List<Integer> cod_reparacoes) { this.cod_reparacoes = new ArrayList<>(cod_reparacoes); }

     public CheckList getCheckList() { return new CheckList(checkList); }
     public void setCheckList(CheckList checkList) { this.checkList = checkList; }

     public void adicionarPecas(List<Stock> pecas) {
          for (Stock stock : pecas) {
               this.listaPecas.add(new PecasUsadas(stock.getQuantidade(), stock.getId()));
          }
     }

     public void validarChecklist() {
          this.checkList.validarChecklist();
     }
}
