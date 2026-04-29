package app.ecoRideLN.sReparacoes;

import java.util.Optional;

public interface ISReparacoes {

    Reparacao registarReparacao(String nomenclatura, String descricao, float preco);

    Optional<Reparacao> obterDadosReparacao(int id);

    void removerReparacao(int id);

    boolean existeReparacao(int id);

    boolean reparacaoDisponivel(int id);

    void atualizarNomenclaturaReparacao(int id, String nomenclatura);

    void atualizarDescricaoReparacao(int id, String descricao);

    void atualizarPrecoReparacao(int id, float preco);

    void atualizarFlagDisponibilidadeReparacao(int id, boolean novaFlag);
}
