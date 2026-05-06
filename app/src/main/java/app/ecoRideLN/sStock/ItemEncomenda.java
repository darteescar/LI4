package app.ecoRideLN.sStock;

public class ItemEncomenda {
    private int codPeca;
    private int quantidade;
    private float preco_unitario;

    public ItemEncomenda(int codPeca, int quantidade, float preco_unitario) {
        this.codPeca = codPeca;
        this.quantidade = quantidade;
        this.preco_unitario = preco_unitario;
    }

    public int getCodPeca() { return codPeca; }
    public void setCodPeca(int codPeca) { this.codPeca = codPeca; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public float getPreco_unitario() { return preco_unitario; }
    public void setPreco_unitario(float preco_unitario) { this.preco_unitario = preco_unitario; }
}
