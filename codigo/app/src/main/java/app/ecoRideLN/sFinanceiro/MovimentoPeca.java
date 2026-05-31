package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoPeca extends MovimentoFinanceiro {

    private int codStock;

    public MovimentoPeca(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo, int codStock) {
        super(id, descricao, valor, data, tipo);
        if (tipo != TipoMovimento.GastoPecas && tipo != TipoMovimento.LucroVendaPecas) {
            throw new IllegalArgumentException("MovimentoPeca apenas aceita GastoPecas ou LucroVendaPecas");
        }
        this.codStock = codStock;
    }

    public int getCodStock()             { return codStock; }
    public void setCodStock(int codStock) { this.codStock = codStock; }
}
