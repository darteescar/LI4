package app.ecoRideLN.sReparacoes;

import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;

public class SReparacoesFacade implements ISReparacoes {
     private final ReparacaoDAO reparacaoDAO = ReparacaoDAO.getInstance();

     @Override
     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco, boolean disponivel) {
          int id = reparacaoDAO.generateNewId();
          Reparacao reparacao = new Reparacao(id, nomenclatura, descricao, preco, disponivel);
          reparacaoDAO.put(id, reparacao);
          return reparacao;
     }

     @Override
     public Reparacao atualizarReparacao(int id, String novaNomenclatura, String novaDescricao, float novoPreco, boolean novaDisponibilidade) {
          Reparacao reparacao = reparacaoDAO.get(id);
          if (reparacao != null) {
               if (novaNomenclatura != null && !novaNomenclatura.isEmpty()) reparacao.setNomenclatura(novaNomenclatura);
               if (novaDescricao != null && !novaDescricao.isEmpty())       reparacao.setDescricao(novaDescricao);
               if (novoPreco >= 0)                                           reparacao.setPreco(novoPreco);
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
