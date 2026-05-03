package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideLN.sStock.Peca;

public class MovimentoPeca extends MovimentoFinanceiro {
    private int codPeca;

    private final static PecaDAO pecaDAO = PecaDAO.getInstance();

    public MovimentoPeca(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo, int codPeca) {
        super(id, descricao, valor, data, tipo);
        if (tipo != TipoMovimento.GastoPecas && tipo != TipoMovimento.LucroVendaPecas) {
            throw new IllegalArgumentException(
                "MovimentoPeca apenas aceita TipoMovimento.GastoPecas ou TipoMovimento.LucroVendaPecas");
        }
        this.codPeca = codPeca;
    }

    public int getCodPeca() {
        return codPeca;
    }

    public void setCodPeca(int codPeca) {
        this.codPeca = codPeca;
    }

    // Métodos a mais

    public Peca getPeca() {
        return pecaDAO.get(codPeca);
    }
}
