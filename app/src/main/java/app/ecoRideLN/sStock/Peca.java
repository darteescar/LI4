package app.ecoRideLN.sStock;

import app.ecoRideCD.sStock.FornecedorDAO;

public class Peca {
     private int id;
     private String referencia;
     private int stock_minimo;
     private int preco_venda;
     private int codFornecedor;
     private boolean ativa;
     private static final FornecedorDAO fornecedorDAO = FornecedorDAO.getInstance();

     public Peca(int id, String referencia, int stock_minimo, int preco_venda, int codFornecedor, boolean ativa) {
          this.id = id;
          this.referencia = referencia;
          this.stock_minimo = stock_minimo;
          this.preco_venda = preco_venda;
          this.codFornecedor = codFornecedor;
          this.ativa = ativa;
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

     public int getStock_minimo() {
          return stock_minimo;
     }

     public void setStock_minimo(int stock_minimo) {
          this.stock_minimo = stock_minimo;
     }

     public int getPreco_venda() {
          return preco_venda;
     }

     public void setPreco_venda(int preco_venda) {
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

}
