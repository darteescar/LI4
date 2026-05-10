package app.ecoRideLN.sFinanceiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.javatuples.Pair;

import app.common.EcoRideException;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

public class SFinanceiroFacade implements ISFinanceiro {
     private final MovimentoFinanceiroDAO movimentoFinanceiroDAO = MovimentoFinanceiroDAO.getInstance();

     // ------------------- Registo -------------------

     @Override
     public MovimentoFinanceiro registarMovimentoCompraStock(int codStock, float valor, String descricao) {
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFinanceiro movimento = new MovimentoPeca(id, descricao, valor, LocalDateTime.now(), TipoMovimento.GastoPecas, codStock);
          return movimentoFinanceiroDAO.put(id, movimento);
     }

     @Override
     public MovimentoFinanceiro registarMovimentoVendaPeca(int codStock, float valor, String descricao){
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFinanceiro movimento = new MovimentoPeca(id, descricao, valor, LocalDateTime.now(), TipoMovimento.LucroVendaPecas, codStock);
          return movimentoFinanceiroDAO.put(id, movimento);
     }

     @Override
     public MovimentoFinanceiro registarMovimentoPagamentoFuncionario(int idFuncionario, float valor, String descricao){
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFinanceiro movimento = new MovimentoFuncionario(id, descricao, valor, LocalDateTime.now(), TipoMovimento.Salario, idFuncionario);
          return movimentoFinanceiroDAO.put(id, movimento);
     }

     @Override
     public MovimentoFinanceiro registarMovimentoReparacaoOS(int idReparacao, float valor, String descricao){
          int id = movimentoFinanceiroDAO.generateNewId();
          MovimentoFinanceiro movimento = new MovimentoReparacao(id, descricao, valor, LocalDateTime.now(), TipoMovimento.LucroMaoObra, idReparacao);
          return movimentoFinanceiroDAO.put(id, movimento);
     }

     // ------------------- Consulta -------------------

     @Override
     public List<MovimentoFinanceiro> obterMovimentos() {
          return movimentoFinanceiroDAO.values().stream().toList();
     }

     @Override
     public List<MovimentoFinanceiro> obterMovimentosFinanceirosFiltrados(LocalDate desde, LocalDate ate, TipoMovimento tipo) {
          if (desde == null && ate == null && tipo == null) {
               return obterMovimentos();
          }
          if (desde.isAfter(ate)) {
               throw new EcoRideException("A data 'desde' não pode ser posterior à data 'ate'.");
          }

          return obterMovimentos().stream()
                    .filter(m -> desde == null || m.getData().toLocalDate().isAfter(desde))
                    .filter(m -> ate == null || m.getData().toLocalDate().isBefore(ate))
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
     public AnaliseFinanceira calcularAnaliseFinanceira(List<MovimentoFinanceiro> movimentos) {
          AnaliseFinanceira analise = new AnaliseFinanceira(0, 0, 0, movimentos.size(), List.of());
          float totalMaoObra = 0, totalLucroVendaPecas = 0, totalSalarios = 0, totalCompraPecas = 0;
          for (MovimentoFinanceiro m : movimentos){
               TipoMovimento tipo = m.getTipo();
               if (tipo == TipoMovimento.GastoPecas) totalCompraPecas += m.getValor();
               else if (tipo == TipoMovimento.LucroMaoObra) totalMaoObra += m.getValor();
               else if (tipo == TipoMovimento.LucroVendaPecas) totalLucroVendaPecas += m.getValor();
               else if (tipo == TipoMovimento.Salario) totalSalarios += m.getValor();
          }
          analise.setReceitas(totalMaoObra + totalLucroVendaPecas);
          analise.setDespesas(totalCompraPecas + totalSalarios);
          analise.setSaldo(analise.getReceitas() - analise.getDespesas());
          analise.setLista_tipos(List.of(
               new Pair<>(TipoMovimento.GastoPecas, totalCompraPecas),
               new Pair<>(TipoMovimento.LucroMaoObra, totalMaoObra),
               new Pair<>(TipoMovimento.LucroVendaPecas, totalLucroVendaPecas),
               new Pair<>(TipoMovimento.Salario, totalSalarios)
          ));

          return analise;
     }

}
