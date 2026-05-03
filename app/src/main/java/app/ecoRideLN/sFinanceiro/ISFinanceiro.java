package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;

public interface ISFinanceiro {
     public MovimentoFinanceiro obterDadosMovimentoFinanceiro(int id);

     public boolean removerMovimentoFinanceiro(int id);

     public boolean existeMovimentoFinanceiro(int id);

     public List<MovimentoFinanceiro> obterMovimentos();

     public MovimentoFinanceiro criarMovimentoFinanceiro(TipoMovimento tipo, float valor, String descricao);

     public void atualizarValorCompraMovimentoFinanceiro(int id, float valor);

     public void atualizarDescricaoMovimentoFinanceiro(int id, String descricao);

     public void atualizarDataMovimentoFinanceiro(int id, LocalDateTime data);

     public void atualizarTipoMovimentoFinanceiro(int id, TipoMovimento tipo);
}
