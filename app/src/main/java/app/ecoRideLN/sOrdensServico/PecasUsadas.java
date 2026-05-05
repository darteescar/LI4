package app.ecoRideLN.sOrdensServico;

public class PecasUsadas {
     private int quantidade;
     private int codStock;

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
}
