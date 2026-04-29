package app.ecoRideLN.sStock;

public class PecasDoOrcamento {

    private int codPeca;
    private int quantidade;

    public PecasDoOrcamento(int codPeca, int quantidade) {
        this.codPeca = codPeca;
        this.quantidade = quantidade;
    }

    public int getCodPeca() {
        return codPeca;
    }

    public void setCodPeca(int codPeca) {
        this.codPeca = codPeca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
