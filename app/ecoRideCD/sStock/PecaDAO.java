package app.ecoRideCD.sStock;

import app.ecoRideLN.sStock.Peca;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PecaDAO implements Map<Integer, Peca> {

    private final Map<Integer, Peca> pecas = new HashMap<>();

    public Optional<Peca> obterPorId(int id) {
        return Optional.ofNullable(pecas.get(id));
    }

    public Optional<Peca> obterPorReferencia(String referencia) {
        return pecas.values().stream()
                .filter(p -> referencia.equals(p.getReferencia()))
                .findFirst();
    }

    @Override
    public int size() {
        return pecas.size();
    }

    @Override
    public boolean isEmpty() {
        return pecas.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return pecas.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return pecas.containsValue(value);
    }

    @Override
    public Peca get(Object key) {
        return pecas.get(key);
    }

    @Override
    public Peca put(Integer key, Peca value) {
        return pecas.put(key, value);
    }

    @Override
    public Peca remove(Object key) {
        return pecas.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Peca> m) {
        pecas.putAll(m);
    }

    @Override
    public void clear() {
        pecas.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return pecas.keySet();
    }

    @Override
    public java.util.Collection<Peca> values() {
        return pecas.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Peca>> entrySet() {
        return pecas.entrySet();
    }
}
