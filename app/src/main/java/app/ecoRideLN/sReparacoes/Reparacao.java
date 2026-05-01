package app.ecoRideLN.sReparacoes;

public class Reparacao {
     private int id;
     private String nomenclatura;
     private String descricao;
     private float preco;
     private boolean disponivel;

     public Reparacao(int id, String nomenclatura, String descricao, float preco) {
          this.id = id;
          this.nomenclatura = nomenclatura;
          this.descricao = descricao;
          this.preco = preco;
          this.disponivel = true;
     }

     public Reparacao(int id, String nomenclatura, String descricao, float preco, boolean disponivel) {
          this.id = id;
          this.nomenclatura = nomenclatura;
          this.descricao = descricao;
          this.preco = preco;
          this.disponivel = disponivel;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getNomenclatura() {
          return nomenclatura;
     }

     public void setNomenclatura(String nomenclatura) {
          this.nomenclatura = nomenclatura;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public float getPreco() {
          return preco;
     }

     public void setPreco(float preco) {
          this.preco = preco;
     }

     public boolean isDisponivel() {
          return disponivel;
     }

     public void setDisponivel(boolean disponivel) {
          this.disponivel = disponivel;
     }

}
