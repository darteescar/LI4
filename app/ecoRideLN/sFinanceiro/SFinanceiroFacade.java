package app.ecoRideLN.sFinanceiro;

import app.common.Validacoes;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SFinanceiroFacade implements ISFinanceiro {

    private final MovimentoFinanceiroDAO movimentos_financeirosDAO = MovimentoFinanceiroDAO.getInstance();

    @Override
    public MovimentoFinanceiro criarMovimentoFinanceiro(float valor, LocalDateTime data, String descricao,
                                                         TipoMovimento tipo, int codEntidade) {
        Validacoes.naoNulo(data, "Data");
        Validacoes.naoVazio(descricao, "Descrição");
        Validacoes.naoNulo(tipo, "Tipo de movimento");
        Validacoes.inteiroPositivo(codEntidade, "Código da entidade");

        int id = movimentos_financeirosDAO.generateNewId();
        MovimentoFinanceiro m = new MovimentoFinanceiro(id, valor, data, descricao, tipo, codEntidade);
        movimentos_financeirosDAO.put(m.getId(), m);
        return m;
    }

    @Override
    public Optional<MovimentoFinanceiro> obterDadosMovimentoFinanceiro(int id) {
        return movimentos_financeirosDAO.obterPorId(id);
    }

    @Override
    public boolean existeMovimentoFinanceiro(int id) {
        return movimentos_financeirosDAO.containsKey(id);
    }

    @Override
    public List<MovimentoFinanceiro> obterMovimentos(LocalDateTime desde, LocalDateTime ate, TipoMovimento tipo) {
        return movimentos_financeirosDAO.values().stream()
                .filter(m -> desde == null || !m.getData().isBefore(desde))
                .filter(m -> ate == null || !m.getData().isAfter(ate))
                .filter(m -> tipo == null || m.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    @Override
    public void aumentarGastoPecasDoMes(LocalDateTime referencia, float valor, String descricao,
                                         int codEntidade) {
        Validacoes.naoNulo(referencia, "Referência");
        Validacoes.valorMonetario(valor, "Valor");

        Optional<MovimentoFinanceiro> existente = movimentos_financeirosDAO.values().stream()
                .filter(m -> m.getTipo() == TipoMovimento.GASTO_PECAS)
                .filter(m -> m.getData().getYear() == referencia.getYear() &&
                             m.getData().getMonth() == referencia.getMonth())
                .filter(m -> m.getCodEntidade() == codEntidade)
                .findFirst();

        if (existente.isPresent()) {
            MovimentoFinanceiro m = existente.get();
            m.setValor(m.getValor() + valor);
            movimentos_financeirosDAO.put(m.getId(), m);
        } else {
            criarMovimentoFinanceiro(valor, referencia, descricao, TipoMovimento.GASTO_PECAS, codEntidade);
        }
    }
}
