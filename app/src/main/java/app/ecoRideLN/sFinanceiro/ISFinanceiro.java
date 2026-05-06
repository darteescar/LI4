package app.ecoRideLN.sFinanceiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISFinanceiro {

     // ------------------- Registo -------------------

     MovimentoFinanceiro criarMovimentoFuncionario(float valor, String descricao, int codFuncionario);

     MovimentoFinanceiro criarMovimentoReparacao(float valor, String descricao, int codReparacao);

     MovimentoFinanceiro criarMovimentoPeca(float valor, String descricao, TipoMovimento tipo, int codPeca);

     // ------------------- Consulta -------------------

     MovimentoFinanceiro obterMovimentoFinanceiro(int id);

     List<MovimentoFinanceiro> obterMovimentos();

     List<MovimentoFinanceiro> obterMovimentosPorTipo(TipoMovimento tipo);

     List<MovimentoFinanceiro> obterMovimentosPorIntervalo(LocalDateTime desde, LocalDateTime ate);

     List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo);

     // ------------------- Existe / Remove -------------------

     boolean existeMovimentoFinanceiro(int id);

     boolean removerMovimentoFinanceiro(int id);

     // ------------------- Atualização -------------------

     void atualizarMovimentoFinanceiro(int id, TipoMovimento tipo, float valor, String descricao, LocalDateTime data);

     // ------------------- Cálculos -------------------

     float calcularSaldoTotal();
}
