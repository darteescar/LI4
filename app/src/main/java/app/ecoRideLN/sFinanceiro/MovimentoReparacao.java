package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoReparacao extends MovimentoFinanceiro {

    private int codReparacao;

    public MovimentoReparacao(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo, int codReparacao) {
        super(id, descricao, valor, data, tipo);
        this.codReparacao = codReparacao;
    }

    public int getCodReparacao() {
        return codReparacao;
    }

    public void setCodReparacao(int codReparacao) {
        this.codReparacao = codReparacao;
    }
}
