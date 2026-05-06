package app.ecoRideLN.sNotificacoes;

import java.util.List;

public interface ISNotificacoes {

     public NotificacaoOS registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os);

     public NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca);

     public Notificacao obterNotificacao(int id);

     public boolean removerNotificacao(int id);

     public List<Notificacao> obterNotificacoes();

     // Utilitários

     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     public List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario);

     public boolean sinalizarNotificacao_comoTratada(int id);

     public boolean sinalizarNotificacao_comoLida(int id);
}
