package app.ecoRideLN.sOrdensServico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Conserto {

     private float preco_total;
     private Map<Integer, Integer> stocksUsados; // codStock -> quantidade consumida
     private List<Integer> cod_reparacoes;
     private CheckList checkList;

     public Conserto(Map<Integer, Integer> stocksUsados, List<Integer> cod_reparacoes, float preco_total) {
          this.preco_total = preco_total;
          this.stocksUsados = new LinkedHashMap<>(stocksUsados);
          this.cod_reparacoes = new ArrayList<>(cod_reparacoes);
          this.checkList = new CheckList();
     }

     public Conserto(Conserto conserto) {
          this.preco_total = conserto.preco_total;
          this.stocksUsados = new LinkedHashMap<>(conserto.stocksUsados);
          this.cod_reparacoes = new ArrayList<>(conserto.cod_reparacoes);
          this.checkList = new CheckList(conserto.checkList);
     }

     public Conserto() {
          this.preco_total = 0;
          this.stocksUsados = new LinkedHashMap<>();
          this.cod_reparacoes = new ArrayList<>();
          this.checkList = new CheckList();
     }

     public float getPreco_total() { return preco_total; }
     public void  setPreco_total(float preco_total) { this.preco_total = preco_total; }

     public Map<Integer, Integer> getStocksUsados() { return new LinkedHashMap<>(stocksUsados); }
     public void setStocksUsados(Map<Integer, Integer> stocksUsados) { this.stocksUsados = new LinkedHashMap<>(stocksUsados); }

     public List<Integer> getCodStocks() { return new ArrayList<>(stocksUsados.keySet()); }

     public List<Integer> getCod_reparacoes() { return new ArrayList<>(cod_reparacoes); }
     public void setCod_reparacoes(List<Integer> cod_reparacoes) { this.cod_reparacoes = new ArrayList<>(cod_reparacoes); }

     public CheckList getCheckList() { return new CheckList(checkList); }
     public void setCheckList(CheckList checkList) { this.checkList = checkList; }

     public void validarChecklist() { this.checkList.validarChecklist(); }
}
