package app.ecoRideLN.sOrdensServico;

public enum EstadoOS {
    PendenteDiagnostico,
    PendenteAprovacaoOrcamento,
    OrcamentoNaoAprovado,
    PendenteReparacao,
    AguardarPecas,
    ClienteNotificado,
    PendentePagamento,
    Paga,
    Eliminada;

    public boolean podeTransicionar(EstadoOS destino) {
        return switch (this) {
            case PendenteDiagnostico         -> destino == PendenteAprovacaoOrcamento || destino == Eliminada;
            case PendenteAprovacaoOrcamento  -> destino == PendenteReparacao || destino == OrcamentoNaoAprovado || destino == Eliminada;
            case OrcamentoNaoAprovado        -> destino == Eliminada;
            case PendenteReparacao           -> destino == AguardarPecas || destino == PendentePagamento || destino == PendenteAprovacaoOrcamento || destino == Eliminada;
            case AguardarPecas               -> destino == PendenteReparacao || destino == PendenteAprovacaoOrcamento || destino == Eliminada;
            case ClienteNotificado           -> destino == PendentePagamento || destino == Eliminada;
            case PendentePagamento           -> destino == Paga || destino == Eliminada;
            case Paga, Eliminada             -> false;
        };
    }
}
