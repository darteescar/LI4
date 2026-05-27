package app.ecoRideLN.sFinanceiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.javatuples.Pair;

import app.common.EcoRideException;
import app.ecoRideCD.sFinanceiro.MovimentoFinanceiroDAO;

public class SFinanceiroFacade implements ISFinanceiro {
     private final MovimentoFinanceiroDAO movimentoFinanceiroDAO;

     public SFinanceiroFacade() {
          this.movimentoFinanceiroDAO = MovimentoFinanceiroDAO.getInstance();
     }

     public SFinanceiroFacade(MovimentoFinanceiroDAO dao) {
          this.movimentoFinanceiroDAO = dao;
     }

     // ------------------- Registo -------------------

     @Override
     public MovimentoFinanceiro registarMovimentoCompraStock(int codStock, float valor, String descricao) {

          validaMovimentoFinanceiro(descricao, valor);

          MovimentoFinanceiro movimento = new MovimentoPeca(0, descricao, valor, LocalDateTime.now(), TipoMovimento.GastoPecas, codStock);
          movimentoFinanceiroDAO.insert(movimento);
          return movimento;
     }

     @Override
     public MovimentoFinanceiro registarMovimentoVendaPeca(int codStock, float valor, String descricao){

          validaMovimentoFinanceiro(descricao, valor);

          MovimentoFinanceiro movimento = new MovimentoPeca(0, descricao, valor, LocalDateTime.now(), TipoMovimento.LucroVendaPecas, codStock);
          movimentoFinanceiroDAO.insert(movimento);
          return movimento;
     }

     @Override
     public MovimentoFinanceiro registarMovimentoPagamentoFuncionario(int idFuncionario, float valor, String descricao){

          validaMovimentoFinanceiro(descricao, valor);

          MovimentoFinanceiro movimento = new MovimentoFuncionario(0, descricao, valor, LocalDateTime.now(), TipoMovimento.Salario, idFuncionario);
          movimentoFinanceiroDAO.insert(movimento);
          return movimento;
     }

     @Override
     public MovimentoFinanceiro registarMovimentoReparacaoOS(int idReparacao, float valor, String descricao){

          validaMovimentoFinanceiro(descricao, valor);

          MovimentoFinanceiro movimento = new MovimentoReparacao(0, descricao, valor, LocalDateTime.now(), TipoMovimento.LucroMaoObra, idReparacao);
          movimentoFinanceiroDAO.insert(movimento);
          return movimento;
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
          if (desde != null && ate != null && desde.isAfter(ate)) {
               throw new EcoRideException("A data 'desde' não pode ser posterior à data 'ate'.");
          }

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

     @Override
     public void removerMovimentosFinanceirosPorStock(int codStock) {
          movimentoFinanceiroDAO.removeByStock(codStock);
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

     // Utilitários

     private void validaMovimentoFinanceiro(String descricao, float valor) {
          if (descricao == null || descricao.isBlank()) {
               throw new EcoRideException("A descrição do movimento financeiro não pode ser vazia.");
          }
          if (valor < 0) {
               throw new EcoRideException("O valor do movimento financeiro não pode ser negativo.");
          }
     }

}
