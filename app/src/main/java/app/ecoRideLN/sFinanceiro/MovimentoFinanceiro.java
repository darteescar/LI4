package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoFinanceiro {
     private int id;
     private float valor;
     private LocalDateTime data;
     private String descricao;
     private TipoMovimento tipo;

     public MovimentoFinanceiro(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo) {
          this.id = id;
          this.descricao = descricao;
          this.valor = valor;
          this.data = data;
          this.tipo = tipo;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public float getValor() {
          return valor;
     }

     public void setValor(float valor) {
          this.valor = valor;
     }

     public LocalDateTime getData() {
          return data;
     }

     public void setData(LocalDateTime data) {
          this.data = data;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public TipoMovimento getTipo() {
          return tipo;
     }

     public void setTipo(TipoMovimento tipo) {
          this.tipo = tipo;
     }

}
