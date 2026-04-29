package app.ecoRideCD.sStock;

import app.ecoRideLN.sStock.Devolucao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DevolucaoDAO implements Map<Integer, Devolucao> {

    private final Map<Integer, Devolucao> devolucoes = new HashMap<>();

    public Optional<Devolucao> obterPorId(int id) {
        return Optional.ofNullable(devolucoes.get(id));
    }

    @Override
    public int size() {
        return devolucoes.size();
    }

    @Override
    public boolean isEmpty() {
        return devolucoes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return devolucoes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return devolucoes.containsValue(value);
    }

    @Override
    public Devolucao get(Object key) {
        return devolucoes.get(key);
    }

    @Override
    public Devolucao put(Integer key, Devolucao value) {
        return devolucoes.put(key, value);
    }

    @Override
    public Devolucao remove(Object key) {
        return devolucoes.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Devolucao> m) {
        devolucoes.putAll(m);
    }

    @Override
    public void clear() {
        devolucoes.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return devolucoes.keySet();
    }

    @Override
    public java.util.Collection<Devolucao> values() {
        return devolucoes.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Devolucao>> entrySet() {
        return devolucoes.entrySet();
    }
}
