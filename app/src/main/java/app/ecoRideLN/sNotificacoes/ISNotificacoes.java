package app.ecoRideLN.sNotificacoes;

import java.util.List;

public interface ISNotificacoes {

     // ------------------- Registo -------------------

     NotificacaoOS    criarNotificacaoOS(String descricao, int id_remetente, int id_destinatario, int id_os);

     NotificacaoStock criarNotificacaoStock(String descricao, int id_remetente, int id_destinatario, int id_peca);

     // ------------------- Consulta -------------------

     Notificacao obterNotificacao(int id);

     List<Notificacao> obterNotificacoes();

     List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario);

     // ------------------- Remove -------------------

     boolean removerNotificacao(int id);

     // ------------------- Atualização -------------------

     boolean sinalizarNotificacao_comoTratada(int id);

     boolean sinalizarNotificacao_comoLida(int id);
}
