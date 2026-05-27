package app.ecoRideLN.sReparacoes;

import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;

public class SReparacoesFacade implements ISReparacoes {
     private final ReparacaoDAO reparacaoDAO;

     public SReparacoesFacade() {
          this.reparacaoDAO = ReparacaoDAO.getInstance();
     }

     public SReparacoesFacade(ReparacaoDAO dao) {
          this.reparacaoDAO = dao;
     }

     @Override
     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco, boolean disponivel) {
          if (nomenclatura == null || nomenclatura.isBlank()) throw new EcoRideException("Nomenclatura não pode ser vazia.");
          if (descricao == null || descricao.isBlank()) throw new EcoRideException("Descrição não pode ser vazia.");
          if (preco < 0) throw new EcoRideException("Preço não pode ser negativo.");
          Reparacao reparacao = new Reparacao(0, nomenclatura, descricao, preco, disponivel);
          reparacaoDAO.insert(reparacao);
          return reparacao;
     }

     @Override
     public Reparacao atualizarReparacao(int id, String novaNomenclatura, String novaDescricao, float novoPreco, boolean novaDisponibilidade) {
          Reparacao reparacao = reparacaoDAO.get(id);
          if (reparacao != null && novaNomenclatura != null && !novaNomenclatura.isBlank() && novaDescricao != null && !novaDescricao.isBlank() && novoPreco >= 0) {
               reparacao.setNomenclatura(novaNomenclatura);
               reparacao.setDescricao(novaDescricao);
               reparacao.setPreco(novoPreco);
               reparacao.setDisponivel(novaDisponibilidade);
               reparacaoDAO.put(id, reparacao);
               return reparacao;
          } else {
               throw new EcoRideException("Reparação com ID " + id + " não encontrada.");
          }
     }

     @Override
     public Reparacao obterReparacao(int id) {
          return reparacaoDAO.get(id);
     }

     @Override
     public boolean existeReparacao(int id) {
          return reparacaoDAO.containsKey(id);
     }

     @Override
     public boolean removerReparacao(int id) {
          return reparacaoDAO.remove(id) != null;
     }

     @Override
     public List<Reparacao> obterReparacoes() {
          return reparacaoDAO.values().stream().toList();
     }

     // Utilitários

     @Override
     public List<Reparacao> obterReparacoesDisponiveis() {
          return reparacaoDAO.values().stream().filter(Reparacao::isDisponivel).toList();
     }

}
