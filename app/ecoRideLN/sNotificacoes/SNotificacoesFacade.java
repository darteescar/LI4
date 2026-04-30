package app.ecoRideLN.sNotificacoes;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sNotificacoes.NotificacaoDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SNotificacoesFacade implements ISNotificacoes {

    private final NotificacaoDAO notificacoesDAO = NotificacaoDAO.getInstance();

    @Override
    public Notificacao criarNotificacao(int idUtilizadorRemetente, int idUtilizadorDestinatario, String descricao) {
        Validacoes.naoVazio(descricao, "Descrição da notificação");
        Validacoes.inteiroPositivo(idUtilizadorRemetente, "Id do utilizador remetente");
        Validacoes.inteiroPositivo(idUtilizadorDestinatario, "Id do utilizador destinatário");

        int id = notificacoesDAO.generateNewId();
        Notificacao n = new Notificacao(id, descricao, LocalDateTime.now(),
                idUtilizadorRemetente, idUtilizadorDestinatario);
        notificacoesDAO.put(n.getId(), n);
        return n;
    }

    @Override
    public Optional<Notificacao> obterDadosNotificacao(int idNotificacao) {
        return notificacoesDAO.obterPorId(idNotificacao);
    }

    @Override
    public boolean existeNotificacao(int idNotificacao) {
        return notificacoesDAO.containsKey(idNotificacao);
    }

    @Override
    public void removerNotificacao(int idNotificacao) {
        if (!existeNotificacao(idNotificacao))
            throw new EcoRideException("Notificação não encontrada.");
        notificacoesDAO.remove(idNotificacao);
    }

    private Notificacao obterOuFalhar(int id) {
        return notificacoesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Notificação não encontrada."));
    }

    @Override
    public void atualizarDescricaoNotificacao(int idNotificacao, String descricao) {
        Validacoes.naoVazio(descricao, "Descrição");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setDescricao(descricao);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void atualizarDataEmissaoNotificacao(int idNotificacao, LocalDateTime dataEmissao) {
        Validacoes.naoNulo(dataEmissao, "Data de emissão");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setData_emissao(dataEmissao);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void atualizarIdUtilizadorRemetenteNotificacao(int idNotificacao, int idUtilizadorRemetente) {
        Validacoes.inteiroPositivo(idUtilizadorRemetente, "Id do utilizador remetente");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setIdUtilizadorRemetente(idUtilizadorRemetente);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void atualizarIdUtilizadorDestinatarioNotificacao(int idNotificacao, int idUtilizadorDestinatario) {
        Validacoes.inteiroPositivo(idUtilizadorDestinatario, "Id do utilizador destinatário");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setIdUtilizadorDestinatario(idUtilizadorDestinatario);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void sinalizarNotificacaoComoTratada(int idNotificacao) {
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setNotificacao_tratada(true);
        n.setData_horaTratada(LocalDateTime.now());
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void atualizarDataConfirmacaoTratamentoNotificacao(int idNotificacao, LocalDateTime data) {
        Validacoes.naoNulo(data, "Data de confirmação");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setData_horaTratada(data);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        Validacoes.naoNulo(dataInicio, "Data de início");
        Validacoes.naoNulo(dataFim, "Data de fim");
        if (dataFim.isBefore(dataInicio))
            throw new EcoRideException("Data de fim anterior à data de início.");

        return notificacoesDAO.todas().stream()
                .filter(n -> !n.getData_emissao().isBefore(dataInicio) && !n.getData_emissao().isAfter(dataFim))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notificacao> obterNotificacoesPorUtilizadorEIntervalo(int idUtilizador,
                                                                      LocalDateTime dataInicio,
                                                                      LocalDateTime dataFim) {
        return obterNotificacoesPorIntervalo(dataInicio, dataFim).stream()
                .filter(n -> n.getIdUtilizadorDestinatario() == idUtilizador
                        || n.getIdUtilizadorRemetente() == idUtilizador)
                .collect(Collectors.toList());
    }
}
