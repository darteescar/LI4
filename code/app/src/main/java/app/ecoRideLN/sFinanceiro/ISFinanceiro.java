package app.ecoRideLN.sFinanceiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISFinanceiro {

     // ------------------- Registo -------------------

     public MovimentoFinanceiro registarMovimentoCompraStock(int idPeca, float valor, String descricao);
     public MovimentoFinanceiro registarMovimentoVendaPeca(int idPeca, float valor, String descricao);
     public MovimentoFinanceiro registarMovimentoPagamentoFuncionario(int idFuncionario, float valor, String descricao);
     public MovimentoFinanceiro registarMovimentoPagamentoOS(int idReparacao, float valor, String descricao);

     // ------------------- Consulta -------------------

     public List<MovimentoFinanceiro> obterMovimentos();

     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo);

     // ------------------- Existe / Remove -------------------

     public boolean existeMovimentoFinanceiro(int id);

     public boolean removerMovimentoFinanceiro(int id);

     // ------------------- Atualização -------------------

     public void atualizarMovimentoFinanceiro(int id, TipoMovimento tipo, float valor, String descricao, LocalDateTime data);

     // ------------------- Cálculos -------------------

     public AnaliseFinanceira calcularAnaliseFinanceira(List<MovimentoFinanceiro> movimentos);
}
