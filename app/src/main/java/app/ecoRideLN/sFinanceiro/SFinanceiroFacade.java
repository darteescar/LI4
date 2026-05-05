package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

public class SFinanceiroFacade implements ISFinanceiro {
     private final MovimentoFinanceiroDAO movimentoFinanceiroDAO = MovimentoFinanceiroDAO.getInstance();

     @Override
     public MovimentoFinanceiro criarMovimentoFinanceiro(TipoMovimento tipo, float valor, String descricao) {
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFinanceiro novo = new MovimentoFinanceiro(id, descricao, valor, LocalDateTime.now(), tipo);
          movimentoFinanceiroDAO.put(id, novo);
          return novo;
     }

     @Override
     public MovimentoFinanceiro obterDadosMovimentoFinanceiro(int id) {
          return movimentoFinanceiroDAO.get(id);
     }

     @Override
     public List<MovimentoFinanceiro> obterMovimentos() {
          return movimentoFinanceiroDAO.values().stream().toList();
     }

     @Override
     public boolean existeMovimentoFinanceiro(int id) {
          return movimentoFinanceiroDAO.containsKey(id);
     }

     @Override
     public boolean removerMovimentoFinanceiro(int id) {
          return movimentoFinanceiroDAO.remove(id) != null;
     }

     @Override
     public void atualizarMovimentoFinanceiro(int id, TipoMovimento tipo, float valor, String descricao, LocalDateTime data) {
          MovimentoFinanceiro movimento = movimentoFinanceiroDAO.get(id);
          if (movimento != null) {
               if (tipo != null)        movimento.setTipo(tipo);
               if (valor >= 0)          movimento.setValor(valor);
               if (descricao != null)   movimento.setDescricao(descricao);
               if (data != null)        movimento.setData(data);
               movimentoFinanceiroDAO.put(id, movimento);
          } else {
               throw new EcoRideException("Movimento Financeiro com ID " + id + " não existe.");
          }
     }
}
