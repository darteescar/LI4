package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import app.ecoRideLN.sReparacoes.Reparacao;

public class MovimentoReparacao extends MovimentoFinanceiro {
    private int codReparacao;

    private final static ReparacaoDAO reparacaoDAO = ReparacaoDAO.getInstance();

    public MovimentoReparacao(int id, String descricao, float valor, LocalDateTime data, int codReparacao) {
        super(id, descricao, valor, data, TipoMovimento.LucroMaoObra);
        this.codReparacao = codReparacao;
    }

    public int getCodReparacao() {
        return codReparacao;
    }

    public void setCodReparacao(int codReparacao) {
        this.codReparacao = codReparacao;
    }

    // Métodos a mais

    public Reparacao getReparacao() {
        return reparacaoDAO.get(codReparacao);
    }
}
