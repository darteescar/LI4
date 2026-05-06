package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;
import java.util.List;

import org.javatuples.Pair;

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
