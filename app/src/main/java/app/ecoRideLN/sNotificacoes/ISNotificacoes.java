package app.ecoRideLN.sNotificacoes;

import java.util.List;

public interface ISNotificacoes {

     NotificacaoOS registarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os);

     NotificacaoStock registarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca);

     Notificacao obterNotificacao(int id);

     boolean removerNotificacao(int id);

     List<Notificacao> obterNotificacoes();

     // Utilitários

     List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario);

     boolean sinalizarNotificacao_comoTratada(int id);

     boolean sinalizarNotificacao_comoLida(int id);
}
