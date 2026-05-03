package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

import app.ecoRideCD.sFuncionarios.FuncionarioDAO;
import app.ecoRideLN.sFuncionarios.Funcionario;

public class MovimentoFuncionario extends MovimentoFinanceiro {
    private int codFuncionario;

    private final static FuncionarioDAO funcionarioDAO = FuncionarioDAO.getInstance();

    public MovimentoFuncionario(int id, String descricao, float valor, LocalDateTime data, int codFuncionario) {
        super(id, descricao, valor, data, TipoMovimento.Salario);
        this.codFuncionario = codFuncionario;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    // Métodos a mais

    public Funcionario getFuncionario() {
        return funcionarioDAO.get(codFuncionario);
    }
}
