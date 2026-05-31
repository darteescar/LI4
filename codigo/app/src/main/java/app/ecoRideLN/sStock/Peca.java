package app.ecoRideLN.sStock;

public class Peca {

     private int id;
     private String referencia;
     private String marca;
     private String nome;
     private String descricao;
     private int stock_minimo;
     private float preco_venda;
     private int codFornecedor;
     private boolean ativa;
     private int garantia;

     public Peca(int id, String referencia, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int codFornecedor, boolean ativa, int garantia) {
          this.id = id;
          this.referencia = referencia;
          this.marca = marca;
          this.nome = nome;
          this.descricao = descricao;
          this.stock_minimo = stock_minimo;
          this.preco_venda = preco_venda;
          this.codFornecedor = codFornecedor;
          this.ativa = ativa;
          this.garantia = garantia;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getReferencia() {
          return referencia;
     }

     public void setReferencia(String referencia) {
          this.referencia = referencia;
     }

     public String getMarca() {
          return marca;
     }

     public void setMarca(String marca) {
          this.marca = marca;
     }

     public String getNome() {
          return nome;
     }

     public void setNome(String nome) {
          this.nome = nome;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public int getStock_minimo() {
          return stock_minimo;
     }

     public void setStock_minimo(int stock_minimo) {
          this.stock_minimo = stock_minimo;
     }

     public float getPreco_venda() {
          return preco_venda;
     }

     public void setPreco_venda(float preco_venda) {
          this.preco_venda = preco_venda;
     }

     public int getCodFornecedor() {
          return codFornecedor;
     }

     public void setCodFornecedor(int codFornecedor) {
          this.codFornecedor = codFornecedor;
     }

     public boolean isAtiva() {
          return ativa;
     }

     public void setAtiva(boolean ativa) {
          this.ativa = ativa;
     }

     public int getGarantia() {
          return garantia;
     }

     public void setGarantia(int garantia) {
          this.garantia = garantia;
     }
}
