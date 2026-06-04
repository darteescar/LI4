package app.ecoRideLN.sNotificacoes;

import java.util.List;

public interface ISNotificacoes {

     public void registarNotificacaoOS(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_os);

     public void registarNotificacaoStock(String descricao, int id_remetente, List<Integer> ids_destinatarios, int id_peca);

     public boolean removerNotificacao(int id, int idUser);

     // Utilitários

     public List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     public boolean sinalizarNotificacao_comoTratada(int id, int idUser);

     public boolean sinalizarNotificacao_comoLida(int id, int idUser);
}
