package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;
import java.util.List;

public interface ISNotificacoes {

     public Notificacao obterDadosNotificacao(int id);

     public boolean existeNotificacao(int id);

     public boolean removerNotificacao(int id);

     void atualizarDescricaoNotificacao(int id, String descricao);

     public void atualizarDataEmissaoNotificacao(int id, LocalDateTime data_emissao);

     public void atualizarIdRemetenteNotificacao(int id, int id_remetente);
     
     public void atualizarIdDestinatarioNotificacao(int id, int id_destinatario);

     public List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime data_inicio, LocalDateTime data_fim);

     public List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime data_inicio, LocalDateTime data_fim);

     public Notificacao criarNotificacao(String descricao, int id_remetente, int id_destinatario);

     public boolean sinalizarNotificacao_comoTratada(int id);

     public void atualizarDataConfirmacaoTratamentoNotificacao(int id, LocalDateTime data);

}
