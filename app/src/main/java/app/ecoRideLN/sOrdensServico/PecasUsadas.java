package app.ecoRideLN.sOrdensServico;

import app.ecoRideCD.sStock.StockDAO;
import app.ecoRideLN.sStock.Stock;

public class PecasUsadas {
     private int quantidade;
     private int codStock;

     private static final StockDAO stockDAO = StockDAO.getInstance();

     public PecasUsadas(int quantidade, int codStock) {
          this.quantidade = quantidade;
          this.codStock = codStock;
     }

     public int getQuantidade() {
          return quantidade;
     }

     public void setQuantidade(int quantidade) {
          this.quantidade = quantidade;
     }

     public int getCodStock() {
          return codStock;
     }

     public void setCodStock(int codStock) {
          this.codStock = codStock;
     }

     // Métodos a mais

     public Stock getStock() {
          return stockDAO.get(codStock);
     }

}
