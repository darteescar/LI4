package app.ecoRideLN.sOrdensServico;

import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideLN.sStock.Peca;

public class PecasOrcamento {
     private int quantidade;
     private int codPeca;

     private static final PecaDAO pecaDAO = PecaDAO.getInstance();

     public PecasOrcamento(int quantidade, int codPeca) {
          this.quantidade = quantidade;
          this.codPeca = codPeca;
     }

     public int getQuantidade() {
          return quantidade;
     }

     public void setQuantidade(int quantidade) {
          this.quantidade = quantidade;
     }

     public int getCodPeca() {
          return codPeca;
     }

     public void setCodPeca(int codPeca) {
          this.codPeca = codPeca;
     }

     public double calcularValor() {
          Peca peca = pecaDAO.get(codPeca);
          if (peca != null) {
               return quantidade * peca.getPreco_venda();
          }
          return 0.0;
     }

}
