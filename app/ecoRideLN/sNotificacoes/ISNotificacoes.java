package app.ecoRideLN.sNotificacoes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISNotificacoes {

    Notificacao criarNotificacao(int idRemetente, int idDestinatario, String descricao);

    Optional<Notificacao> obterDadosNotificacao(int idNotificacao);

    boolean existeNotificacao(int idNotificacao);

    void removerNotificacao(int idNotificacao);

    void atualizarDescricaoNotificacao(int idNotificacao, String descricao);

    void atualizarDataEmissaoNotificacao(int idNotificacao, LocalDateTime dataEmissao);

    void atualizarIdRemetenteNotificacao(int idNotificacao, int idRemetente);

    void atualizarIdDestinatarioNotificacao(int idNotificacao, int idDestinatario);

    void sinalizarNotificacaoComoTratada(int idNotificacao);

    void atualizarDataConfirmacaoTratamentoNotificacao(int idNotificacao, LocalDateTime data);

    List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int idFuncionario,
            LocalDateTime dataInicio,
            LocalDateTime dataFim);
}
