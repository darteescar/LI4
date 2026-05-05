package app.ecoRideLN.sReparacoes;

import java.util.List;

public interface ISReparacoes {

     public Reparacao registarReparacao(String nomenclatura, String descricao, float preco);

     public Reparacao obterReparacao(int id);

     public boolean existeReparacao(int id);

     public boolean removerReparacao(int id);

     public List<Reparacao> obterTodasReparacoes();

     public List<Reparacao> obterReparacoesDisponiveis();

     public void atualizarReparacao(int id, String novaNomenclatura, String novaDescricao, float novoPreco, boolean novaDisponibilidade);
}
