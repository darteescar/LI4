package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISFinanceiro {

    MovimentoFinanceiro criarMovimentoFinanceiro(float valor, LocalDateTime data, String descricao,
                                                  TipoMovimento tipo, int codEntidade, String tipoEntidade);

    Optional<MovimentoFinanceiro> obterDadosMovimentoFinanceiro(int id);

    boolean existeMovimentoFinanceiro(int id);

    List<MovimentoFinanceiro> obterMovimentos(LocalDateTime desde, LocalDateTime ate, TipoMovimento tipo);

    void aumentarGastoPecasDoMes(LocalDateTime referencia, float valor, String descricao,
                                  int codEntidade, String tipoEntidade);
}
