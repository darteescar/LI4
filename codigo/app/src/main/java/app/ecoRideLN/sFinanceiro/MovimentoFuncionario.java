package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoFuncionario extends MovimentoFinanceiro {

    private int codFuncionario;

    public MovimentoFuncionario(int id, String descricao, float valor, LocalDateTime data, TipoMovimento tipo, int codFuncionario) {
        super(id, descricao, valor, data, tipo);
        this.codFuncionario = codFuncionario;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }
}
