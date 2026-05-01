package app.ecoRideLN.sFinanceiro;

import java.util.List;

public interface ISFinanceiro {
     MovimentoFinanceiro obterDadosMovimentoFinanceiro(int id);

     boolean removerMovimentoFinanceiro(int id);

     boolean existeMovimentoFinanceiro(int id);

     List<MovimentoFinanceiro> obterMovimentos();

     MovimentoFinanceiro criarMovimentoFinanceiro(TipoMovimento tipo, float valor, String descricao);

     void atualizarValorCompraMovimentoFinanceiro(int id, float valor);

     void atualizarDescricaoMovimentoFinanceiro(int id, String descricao);

     void atualizarDataMovimentoFinanceiro(int id, String data);

     void atualizarTipoMovimentoFinanceiro(int id, TipoMovimento tipo);
}
