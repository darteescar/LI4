package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoPeca extends MovimentoFinanceiro {

    private int codPeca;

    public MovimentoPeca(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo, int codPeca) {
        super(id, descricao, valor, data, tipo);
        if (tipo != TipoMovimento.GastoPecas && tipo != TipoMovimento.LucroVendaPecas) {
            throw new IllegalArgumentException("MovimentoPeca apenas aceita GastoPecas ou LucroVendaPecas");
        }
        this.codPeca = codPeca;
    }

    public int getCodPeca() {
        return codPeca;
    }

    public void setCodPeca(int codPeca) {
        this.codPeca = codPeca;
    }
}
