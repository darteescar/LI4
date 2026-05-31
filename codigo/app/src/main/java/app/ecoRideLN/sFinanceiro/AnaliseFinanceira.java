package app.ecoRideLN.sFinanceiro;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

public class AnaliseFinanceira {
     private float receitas;
     private float despesas;
     private float saldo;
     private float movimentos;
     private List<Pair<TipoMovimento,Float>> lista_tipos;

     public AnaliseFinanceira(float receitas, float despesas, float saldo, float movimentos, List<Pair<TipoMovimento,Float>> lista_tipos) {
          this.receitas = receitas;
          this.despesas = despesas;
          this.saldo = saldo;
          this.movimentos = movimentos;
          this.lista_tipos = lista_tipos;
     }

     public float getReceitas() {
          return receitas;
     }

     public float getDespesas() {
          return despesas;
     }

     public float getSaldo() {
          return saldo;
     }

     public float getMovimentos() {
          return movimentos;
     }

     public List<Pair<TipoMovimento, Float>> getLista_tipos() {
          return new ArrayList<>(this.lista_tipos);
     }

     public void setReceitas(float receitas) {
          this.receitas = receitas;
     }

     public void setDespesas(float despesas) {
          this.despesas = despesas;
     }

     public void setSaldo(float saldo) {
          this.saldo = saldo;
     }

     public void setMovimentos(float movimentos) {
          this.movimentos = movimentos;
     }

     public void setLista_tipos(List<Pair<TipoMovimento, Float>> lista_tipos) {
          this.lista_tipos = new ArrayList<>(lista_tipos);
     }

}
