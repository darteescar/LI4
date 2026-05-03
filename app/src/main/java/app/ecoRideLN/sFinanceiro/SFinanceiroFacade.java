package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

public class SFinanceiroFacade implements ISFinanceiro {
     private final MovimentoFinanceiroDAO movimentoFinanceiroDAO = MovimentoFinanceiroDAO.getInstance();
     
        @Override
        public MovimentoFinanceiro obterDadosMovimentoFinanceiro(int id) {
            return movimentoFinanceiroDAO.get(id);
        }

        @Override
        public boolean removerMovimentoFinanceiro(int id) {
            return movimentoFinanceiroDAO.remove(id) != null;
        }

        @Override
        public boolean existeMovimentoFinanceiro(int id) {
            return movimentoFinanceiroDAO.containsKey(id);
        }

        @Override
        public List<MovimentoFinanceiro> obterMovimentos() {
            return movimentoFinanceiroDAO.values().stream().toList();
        }

        @Override
        public MovimentoFinanceiro criarMovimentoFinanceiro(TipoMovimento tipo, float valor, String descricao) {
            MovimentoFinanceiro novo = new MovimentoFinanceiro(
                    movimentoFinanceiroDAO.generateNewId(),
                    descricao,
                    valor,
                    java.time.LocalDateTime.now(),
                    tipo);
            movimentoFinanceiroDAO.put(novo.getId(), novo);
            return novo;
        }

        @Override
        public void atualizarValorCompraMovimentoFinanceiro(int id, float valor) {
            MovimentoFinanceiro movimento = movimentoFinanceiroDAO.get(id);
            if (movimento != null) {
                movimento.setValor(valor);
                movimentoFinanceiroDAO.put(id, movimento);
            }
        }

        @Override
        public void atualizarDescricaoMovimentoFinanceiro(int id, String descricao) {
            MovimentoFinanceiro movimento = movimentoFinanceiroDAO.get(id);
            if (movimento != null) {
                movimento.setDescricao(descricao);
                movimentoFinanceiroDAO.put(id, movimento);
            }
        }

        @Override
        public void atualizarDataMovimentoFinanceiro(int id, LocalDateTime data) {
            MovimentoFinanceiro movimento = movimentoFinanceiroDAO.get(id);
            if (movimento != null) {
                movimento.setData(data);
                movimentoFinanceiroDAO.put(id, movimento);
            }
        }

        @Override
        public void atualizarTipoMovimentoFinanceiro(int id, TipoMovimento tipo) {
            MovimentoFinanceiro movimento = movimentoFinanceiroDAO.get(id);
            if (movimento != null) {
                movimento.setTipo(tipo);
                movimentoFinanceiroDAO.put(id, movimento);
            }
        }

}