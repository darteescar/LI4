package app.ecoRideLN.sStock;

import java.time.LocalDate;

public class StockComGarantia extends Stock {
     private String nr_serie;
     private int garantia;

     public StockComGarantia(int id, float preco_compra, int codPeca, LocalDate data_chegada, String nr_serie, int garantia) {
          super(id, preco_compra, codPeca, data_chegada, 1);
          this.nr_serie = nr_serie;
          this.garantia = garantia;
     }

     public String getNr_serie() {
          return nr_serie;
     }

     public void setNr_serie(String nr_serie) {
          this.nr_serie = nr_serie;
     }

     public int getGarantia() {
          return garantia;
     }

     public void setGarantia(int garantia) {
          this.garantia = garantia;
     }
}
