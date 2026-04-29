package app.ecoRideCD.sReparacoes;

import app.ecoRideLN.sReparacoes.Reparacao;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReparacaoDAO implements Map<Integer, Reparacao> {

    private final Map<Integer, Reparacao> reparacoes = new HashMap<>();

    public Optional<Reparacao> obterPorId(int id) {
        return Optional.ofNullable(reparacoes.get(id));
    }

    public Optional<Reparacao> obterPorNomenclatura(String nomenclatura) {
        return reparacoes.values().stream()
                .filter(r -> nomenclatura.equals(r.getNomenclatura()))
                .findFirst();
    }

    @Override
    public int size() {
        return reparacoes.size();
    }

    @Override
    public boolean isEmpty() {
        return reparacoes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return reparacoes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return reparacoes.containsValue(value);
    }

    @Override
    public Reparacao get(Object key) {
        return reparacoes.get(key);
    }

    @Override
    public Reparacao put(Integer key, Reparacao value) {
        return reparacoes.put(key, value);
    }

    @Override
    public Reparacao remove(Object key) {
        return reparacoes.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Reparacao> m) {
        reparacoes.putAll(m);
    }

    @Override
    public void clear() {
        reparacoes.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return reparacoes.keySet();
    }

    @Override
    public java.util.Collection<Reparacao> values() {
        return reparacoes.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Reparacao>> entrySet() {
        return reparacoes.entrySet();
    }
}
