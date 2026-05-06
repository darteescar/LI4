package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

public class SFinanceiroFacade implements ISFinanceiro {
     private final MovimentoFinanceiroDAO movimentoFinanceiroDAO = MovimentoFinanceiroDAO.getInstance();

     // ------------------- Registo -------------------

     @Override
     public MovimentoFinanceiro criarMovimentoFuncionario(float valor, String descricao, int codFuncionario) {
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFuncionario novo = new MovimentoFuncionario(id, descricao, valor, LocalDateTime.now(), codFuncionario);
          movimentoFinanceiroDAO.put(id, novo);
          return novo;
     }

     @Override
     public MovimentoFinanceiro criarMovimentoReparacao(float valor, String descricao, int codReparacao) {
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoReparacao novo = new MovimentoReparacao(id, descricao, valor, LocalDateTime.now(), codReparacao);
          movimentoFinanceiroDAO.put(id, novo);
          return novo;
     }

     @Override
     public MovimentoFinanceiro criarMovimentoPeca(float valor, String descricao, TipoMovimento tipo, int codPeca) {
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoPeca novo = new MovimentoPeca(id, descricao, valor, LocalDateTime.now(), tipo, codPeca);
          movimentoFinanceiroDAO.put(id, novo);
          return novo;
     }

     // ------------------- Consulta -------------------

     @Override
     public List<MovimentoFinanceiro> obterMovimentos() {
          return movimentoFinanceiroDAO.values().stream().toList();
     }

     @Override
     public List<MovimentoFinanceiro> obterMovimentosPorTipo(TipoMovimento tipo) {
          return movimentoFinanceiroDAO.values().stream()
                    .filter(m -> m.getTipo() == tipo)
                    .toList();
     }

     @Override
     public List<MovimentoFinanceiro> obterMovimentosPorIntervalo(LocalDateTime desde, LocalDateTime ate) {
          return movimentoFinanceiroDAO.values().stream()
                    .filter(m -> !m.getData().isBefore(desde) && !m.getData().isAfter(ate))
                    .toList();
     }

     @Override
     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(java.time.LocalDate desde, java.time.LocalDate ate, TipoMovimento tipo) {
          return obterMovimentos().stream()
                    .filter(m -> desde == null || !m.getData().toLocalDate().isBefore(desde))
                    .filter(m -> ate == null || !m.getData().toLocalDate().isAfter(ate))
                    .filter(m -> tipo == null || m.getTipo() == tipo)
                    .collect(java.util.stream.Collectors.toList());
     }

     // ------------------- Existe / Remove -------------------

     @Override
     public boolean existeMovimentoFinanceiro(int id) {
          return movimentoFinanceiroDAO.containsKey(id);
     }

     @Override
     public boolean removerMovimentoFinanceiro(int id) {
          return movimentoFinanceiroDAO.remove(id) != null;
     }

     // ------------------- Atualização -------------------

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

     // ------------------- Cálculos -------------------

     @Override
     public float calcularSaldoTotal() {
          float total = 0;
          for (MovimentoFinanceiro m : movimentoFinanceiroDAO.values()) {
               if (m.getTipo() == TipoMovimento.LucroMaoObra || m.getTipo() == TipoMovimento.LucroVendaPecas) {
                    total += m.getValor();
               } else {
                    total -= m.getValor();
               }
          }
          return total;
     }
}
