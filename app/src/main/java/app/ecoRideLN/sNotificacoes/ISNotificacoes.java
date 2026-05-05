package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;
import java.util.List;

public interface ISNotificacoes {

     // ------------------- Registo -------------------

     Notificacao criarNotificacao(String descricao, int id_remetente, int id_destinatario);

     // ------------------- Consulta -------------------

     Notificacao obterDadosNotificacao(int id);

     List<Notificacao> obterTodasNotificacoes();

     List<Notificacao> obterNotificacoesPorDestinatario(int id_destinatario);

     List<Notificacao> obterNotificacoesNaoTratadas(int id_destinatario);

     List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime data_inicio, LocalDateTime data_fim);

     List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime data_inicio, LocalDateTime data_fim);

     // ------------------- Existe / Remove -------------------

     boolean existeNotificacao(int id);

     boolean removerNotificacao(int id);

     // ------------------- Atualização -------------------

     void atualizarDescricaoNotificacao(int id, String descricao);

     void atualizarDataEmissaoNotificacao(int id, LocalDateTime data_emissao);

     void atualizarIdRemetenteNotificacao(int id, int id_remetente);

     void atualizarIdDestinatarioNotificacao(int id, int id_destinatario);

     void atualizarDataConfirmacaoTratamentoNotificacao(int id, LocalDateTime data);

     boolean sinalizarNotificacao_comoTratada(int id);
}
