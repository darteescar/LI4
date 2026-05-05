package app.ecoRideLN.sStock;

public class Peca {
     private int id;
     private String referencia;
     private String nome;
     private String descricao;
     private int stock_minimo;
     private float preco_venda;
     private int codFornecedor;
     private boolean ativa;

     public Peca(int id, String referencia, String nome, String descricao, int stock_minimo, float preco_venda, int codFornecedor, boolean ativa) {
          this.id = id;
          this.referencia = referencia;
          this.nome = nome;
          this.descricao = descricao;
          this.stock_minimo = stock_minimo;
          this.preco_venda = preco_venda;
          this.codFornecedor = codFornecedor;
          this.ativa = ativa;
     }

     public int getId() { return id; }
     public void setId(int id) { this.id = id; }

     public String getReferencia() { return referencia; }
     public void setReferencia(String referencia) { this.referencia = referencia; }

     public String getNome() { return nome; }
     public void setNome(String nome) { this.nome = nome; }

     public String getDescricao() { return descricao; }
     public void setDescricao(String descricao) { this.descricao = descricao; }

     public int getStock_minimo() { return stock_minimo; }
     public void setStock_minimo(int stock_minimo) { this.stock_minimo = stock_minimo; }

     public float getPreco_venda() { return preco_venda; }
     public void setPreco_venda(float preco_venda) { this.preco_venda = preco_venda; }

     public int getCodFornecedor() { return codFornecedor; }
     public void setCodFornecedor(int codFornecedor) { this.codFornecedor = codFornecedor; }

     public boolean isAtiva() { return ativa; }
     public void setAtiva(boolean ativa) { this.ativa = ativa; }
}
