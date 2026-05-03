package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockComGarantia extends Stock {
     private String nr_serie;
     private LocalDate garantia;
     private final int quantidade = 1;

     public StockComGarantia(int id, float preco_compra, int codPeca, LocalDateTime data_chegada, String nr_serie, LocalDate garantia) {
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

     public LocalDate getGarantia() {
          return garantia;
     }

     public void setGarantia(LocalDate garantia) {
          this.garantia = garantia;
     }
}
