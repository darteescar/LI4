package app.ecoRideLN.sStock;

import java.time.LocalDateTime;

public class Stock {

     private int id;
     private float preco_compra;
     private int codPeca;
     private LocalDateTime data_chegada;
     private int quantidade;
     private EstadoStock estado;

     public Stock(int id, float preco_compra, int codPeca, LocalDateTime data_chegada, int quantidade) {
          this.id = id;
          this.preco_compra = preco_compra;
          this.codPeca = codPeca;
          this.data_chegada = data_chegada;
          this.quantidade = quantidade;
          this.estado = EstadoStock.EmStock;
     }

     public Stock(int id, float preco_compra, int codPeca, LocalDateTime data_chegada, int quantidade, EstadoStock estado) {
          this.id = id;
          this.preco_compra = preco_compra;
          this.codPeca = codPeca;
          this.data_chegada = data_chegada;
          this.quantidade = quantidade;
          this.estado = estado;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public float getPreco_compra() {
          return preco_compra;
     }

     public void setPreco_compra(float preco_compra) {
          this.preco_compra = preco_compra;
     }

     public int getCodPeca() {
          return codPeca;
     }

     public void setCodPeca(int codPeca) {
          this.codPeca = codPeca;
     }

     public LocalDateTime getData_chegada() {
          return data_chegada;
     }

     public void setData_chegada(LocalDateTime data_chegada) {
          this.data_chegada = data_chegada;
     }

     public int getQuantidade() {
          return quantidade;
     }

     public void setQuantidade(int quantidade) {
          this.quantidade = quantidade;
     }

     public EstadoStock getEstado() {
          return estado;
     }

     public void setEstado(EstadoStock estado) {
          this.estado = estado;
     }
}
