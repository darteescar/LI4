package app.ecoRideCD.sStock;

import app.ecoRideLN.sStock.Encomenda;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EncomendaDAO implements Map<Integer, Encomenda> {

    private final Map<Integer, Encomenda> encomendas = new HashMap<>();

    public Optional<Encomenda> obterPorId(int id) {
        return Optional.ofNullable(encomendas.get(id));
    }

    @Override
    public int size() {
        return encomendas.size();
    }

    @Override
    public boolean isEmpty() {
        return encomendas.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return encomendas.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return encomendas.containsValue(value);
    }

    @Override
    public Encomenda get(Object key) {
        return encomendas.get(key);
    }

    @Override
    public Encomenda put(Integer key, Encomenda value) {
        return encomendas.put(key, value);
    }

    @Override
    public Encomenda remove(Object key) {
        return encomendas.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Encomenda> m) {
        encomendas.putAll(m);
    }

    @Override
    public void clear() {
        encomendas.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return encomendas.keySet();
    }

    @Override
    public java.util.Collection<Encomenda> values() {
        return encomendas.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Encomenda>> entrySet() {
        return encomendas.entrySet();
    }
}
