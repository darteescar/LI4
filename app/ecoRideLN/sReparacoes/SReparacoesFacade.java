package app.ecoRideLN.sReparacoes;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sReparacoes.ReparacaoDAO;
import java.util.Optional;

public class SReparacoesFacade implements ISReparacoes {

    private final ReparacaoDAO reparacoesDAO = new ReparacaoDAO();
    private int proximoId = 1;

    @Override
    public Reparacao registarReparacao(String nomenclatura, String descricao, float preco) {
        Validacoes.naoVazio(nomenclatura, "Nomenclatura");
        Validacoes.naoVazio(descricao, "Descrição");
        Validacoes.valorMonetario(preco, "Preço");

        if (reparacoesDAO.obterPorNomenclatura(nomenclatura).isPresent()) {
            throw new EcoRideException("Já existe uma reparação com esta nomenclatura.");
        }

        Reparacao r = new Reparacao(proximoId++, nomenclatura, descricao, preco);
        reparacoesDAO.put(r.getId(), r);
        return r;
    }

    @Override
    public Optional<Reparacao> obterDadosReparacao(int id) {
        return reparacoesDAO.obterPorId(id);
    }

    @Override
    public void removerReparacao(int id) {
        Reparacao r = reparacoesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Reparação não encontrada."));
        r.setDisponivel(false);
        reparacoesDAO.put(r.getId(), r);
    }

    @Override
    public boolean existeReparacao(int id) {
        return reparacoesDAO.containsKey(id);
    }

    @Override
    public boolean reparacaoDisponivel(int id) {
        return reparacoesDAO.obterPorId(id).map(Reparacao::isDisponivel).orElse(false);
    }

    private Reparacao obterOuFalhar(int id) {
        return reparacoesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Reparação não encontrada."));
    }

    @Override
    public void atualizarNomenclaturaReparacao(int id, String nomenclatura) {
        Validacoes.naoVazio(nomenclatura, "Nomenclatura");
        Reparacao r = obterOuFalhar(id);
        if (reparacoesDAO.obterPorNomenclatura(nomenclatura).filter(o -> o.getId() != id).isPresent()) {
            throw new EcoRideException("Já existe uma reparação com esta nomenclatura.");
        }
        r.setNomenclatura(nomenclatura);
        reparacoesDAO.put(r.getId(), r);
    }

    @Override
    public void atualizarDescricaoReparacao(int id, String descricao) {
        Validacoes.naoVazio(descricao, "Descrição");
        Reparacao r = obterOuFalhar(id);
        r.setDescricao(descricao);
        reparacoesDAO.put(r.getId(), r);
    }

    @Override
    public void atualizarPrecoReparacao(int id, float preco) {
        Validacoes.valorMonetario(preco, "Preço");
        Reparacao r = obterOuFalhar(id);
        r.setPreco(preco);
        reparacoesDAO.put(r.getId(), r);
    }

    @Override
    public void atualizarFlagDisponibilidadeReparacao(int id, boolean novaFlag) {
        Reparacao r = obterOuFalhar(id);
        r.setDisponivel(novaFlag);
        reparacoesDAO.put(r.getId(), r);
    }
}
