package app.ecoRideLN.sStock;

import java.time.LocalDate;

public class Stock {

     private int id;
     private float preco_compra;
     private int codPeca;
     private LocalDate data_chegada;
     private int quantidade;
     private EstadoStock estado;
     private LocalDate garantia;

     public Stock(int id, float preco_compra, int codPeca, LocalDate data_chegada, int quantidade, LocalDate garantia) {
          this.id = id;
          this.preco_compra = preco_compra;
          this.codPeca = codPeca;
          this.data_chegada = data_chegada;
          this.quantidade = quantidade;
          this.garantia = garantia;
          this.estado = EstadoStock.StockEmArmazem;
     }

     public Stock(int id, float preco_compra, int codPeca, LocalDate data_chegada, int quantidade, EstadoStock estado, LocalDate garantia) {
          this.id = id;
          this.preco_compra = preco_compra;
          this.codPeca = codPeca;
          this.data_chegada = data_chegada;
          this.quantidade = quantidade;
          this.garantia = garantia;
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

     public LocalDate getData_chegada() {
          return data_chegada;
     }

     public void setData_chegada(LocalDate data_chegada) {
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

     public LocalDate getGarantia() {
          return garantia;
     }

     public void setGarantia(LocalDate garantia) {
          this.garantia = garantia;
     }
}
