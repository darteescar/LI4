package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISNotificacoes {

    Notificacao criarNotificacao(int idUtilizadorRemetente, int idUtilizadorDestinatario, String descricao);

    Optional<Notificacao> obterDadosNotificacao(int idNotificacao);

    boolean existeNotificacao(int idNotificacao);

    void removerNotificacao(int idNotificacao);

    void atualizarDescricaoNotificacao(int idNotificacao, String descricao);

    void atualizarDataEmissaoNotificacao(int idNotificacao, LocalDateTime dataEmissao);

    void atualizarIdUtilizadorRemetenteNotificacao(int idNotificacao, int idUtilizadorRemetente);

    void atualizarIdUtilizadorDestinatarioNotificacao(int idNotificacao, int idUtilizadorDestinatario);

    void sinalizarNotificacaoComoTratada(int idNotificacao);

    void atualizarDataConfirmacaoTratamentoNotificacao(int idNotificacao, LocalDateTime data);

    List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Notificacao> obterNotificacoesPorUtilizadorEIntervalo(int idUtilizador,
                                                                LocalDateTime dataInicio,
                                                                LocalDateTime dataFim);
}
