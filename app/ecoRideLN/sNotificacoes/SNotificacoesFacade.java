package app.ecoRideLN.sNotificacoes;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sNotificacoes.NotificacaoDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SNotificacoesFacade implements ISNotificacoes {

    private final NotificacaoDAO notificacoesDAO = new NotificacaoDAO();
    private int proximoId = 1;

    @Override
    public Notificacao criarNotificacao(int idRemetente, int idDestinatario, String descricao) {
        Validacoes.naoVazio(descricao, "Descrição da notificação");
        Validacoes.inteiroPositivo(idRemetente, "Id do remetente");
        Validacoes.inteiroPositivo(idDestinatario, "Id do destinatário");

        Notificacao n = new Notificacao(proximoId++, descricao, LocalDateTime.now(), idRemetente, idDestinatario);
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
    public void atualizarIdRemetenteNotificacao(int idNotificacao, int idRemetente) {
        Validacoes.inteiroPositivo(idRemetente, "Id do remetente");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setId_remetente(idRemetente);
        notificacoesDAO.put(n.getId(), n);
    }

    @Override
    public void atualizarIdDestinatarioNotificacao(int idNotificacao, int idDestinatario) {
        Validacoes.inteiroPositivo(idDestinatario, "Id do destinatário");
        Notificacao n = obterOuFalhar(idNotificacao);
        n.setId_destinatario(idDestinatario);
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
    public List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int idFuncionario,
                                                                        LocalDateTime dataInicio,
                                                                        LocalDateTime dataFim) {
        return obterNotificacoesPorIntervalo(dataInicio, dataFim).stream()
                .filter(n -> n.getId_destinatario() == idFuncionario || n.getId_remetente() == idFuncionario)
                .collect(Collectors.toList());
    }
}
