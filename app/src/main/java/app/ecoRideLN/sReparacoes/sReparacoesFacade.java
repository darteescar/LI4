package app.ecoRideLN.sReparacoes;

import java.util.List;

import app.common.EcoRideException;

public class sReparacoesFacade implements ISReparacoes {
     private ReparacaoDAO reparacaoDAO;

     public sReparacoesFacade() {
          this.reparacaoDAO = ReparacaoDAO.getInstance();
     }

     @Override
     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco){
          return reparacaoDAO.add(nomenclatura, descricao, preco);
     }

     @Override
     public Reparacao obterDadosReparacao(int id){
          return reparacaoDAO.getById(id);
     }

     @Override
     public boolean removerReparacao(int id){
          return reparacaoDAO.remove(id);
     }

     @Override
     public void atualizarDescricaoReparacao(int id, String novaDescricao){
          Reparacao reparacao = reparacaoDAO.getById(id);
          if (reparacao != null) {
               reparacao.setDescricao(novaDescricao);
               reparacaoDAO.update(reparacao);
          } else {
               throw new EcoRideException("Reparação não encontrada para atualizar descrição.");
          }
     }

     @Override
     public void atualizarPrecoReparacao(int id, float novoPreco){
          Reparacao reparacao = reparacaoDAO.getById(id);
          if (reparacao != null) {
               reparacao.setPreco(novoPreco);
               reparacaoDAO.update(reparacao);
          } else {
               throw new EcoRideException("Reparação não encontrada para atualizar preço.");
          }
     }

     @Override
     public void atualizarFlagDisponibilidadeReparacao(int id, boolean novaDisponibilidade){
          Reparacao reparacao = reparacaoDAO.getById(id);
          if (reparacao != null) {
               reparacao.setDisponivel(novaDisponibilidade);
               reparacaoDAO.update(reparacao);
          } else {
               throw new EcoRideException("Reparação não encontrada para atualizar disponibilidade.");
          }
     }

     @Override
     public List<Reparacao> obterReparacoesDisponiveis(){
          return reparacaoDAO.getAll();
     }
}
