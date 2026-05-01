package app.ecoRideLN.sReparacoes;

import java.util.List;

public interface ISReparacoes {

     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco);

     public Reparacao obterDadosReparacao(int id);

     public boolean removerReparacao(int id);

     public void atualizarDescricaoReparacao(int id, String novaDescricao);

     public void atualizarPrecoReparacao(int id, float novoPreco);

     public void atualizarFlagDisponibilidadeReparacao(int id, boolean novaDisponibilidade);

     public List<Reparacao> obterReparacoesDisponiveis();
}
