package app.ecoRideLN.sOrdensServico;

public class PecasOrcamento {
     private int quantidade;
     private int codPeca;

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
}
