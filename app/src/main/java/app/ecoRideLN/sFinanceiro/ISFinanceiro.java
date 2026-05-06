package app.ecoRideLN.sFinanceiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISFinanceiro {

     // ------------------- Registo -------------------

     public MovimentoFinanceiro criarMovimentoFuncionario(float valor, String descricao, int codFuncionario);

     public MovimentoFinanceiro criarMovimentoReparacao(float valor, String descricao, int codReparacao);

     public MovimentoFinanceiro criarMovimentoPeca(float valor, String descricao, TipoMovimento tipo, int codPeca);

     // ------------------- Consulta -------------------

     public List<MovimentoFinanceiro> obterMovimentos();

     public List<MovimentoFinanceiro> obterMovimentosPorTipo(TipoMovimento tipo);

     public List<MovimentoFinanceiro> obterMovimentosPorIntervalo(LocalDateTime desde, LocalDateTime ate);

     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo);

     // ------------------- Existe / Remove -------------------

     public boolean existeMovimentoFinanceiro(int id);

     public boolean removerMovimentoFinanceiro(int id);

     // ------------------- Atualização -------------------

     public void atualizarMovimentoFinanceiro(int id, TipoMovimento tipo, float valor, String descricao, LocalDateTime data);

     // ------------------- Cálculos -------------------

     public float calcularSaldoTotal();
}
