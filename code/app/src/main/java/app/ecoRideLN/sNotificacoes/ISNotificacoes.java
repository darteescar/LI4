package app.ecoRideLN.sNotificacoes;

import java.util.List;

public interface ISNotificacoes {

     public void registarNotificacaoOS(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_os);

     public void registarNotificacaoStock(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_peca);

     public Notificacao obterNotificacao(int id);

     public boolean removerNotificacao(int id);

     public List<Notificacao> obterNotificacoes();

     // Utilitários

     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     public List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario);

     public boolean sinalizarNotificacao_comoTratada(int id);

     public boolean sinalizarNotificacao_comoLida(int id);
}
