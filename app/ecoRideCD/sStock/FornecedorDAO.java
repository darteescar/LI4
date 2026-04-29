package app.ecoRideCD.sStock;

import app.ecoRideLN.sStock.Fornecedor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FornecedorDAO implements Map<Integer, Fornecedor> {

    private final Map<Integer, Fornecedor> fornecedores = new HashMap<>();

    public Optional<Fornecedor> obterPorId(int id) {
        return Optional.ofNullable(fornecedores.get(id));
    }

    @Override
    public int size() {
        return fornecedores.size();
    }

    @Override
    public boolean isEmpty() {
        return fornecedores.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return fornecedores.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return fornecedores.containsValue(value);
    }

    @Override
    public Fornecedor get(Object key) {
        return fornecedores.get(key);
    }

    @Override
    public Fornecedor put(Integer key, Fornecedor value) {
        return fornecedores.put(key, value);
    }

    @Override
    public Fornecedor remove(Object key) {
        return fornecedores.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Fornecedor> m) {
        fornecedores.putAll(m);
    }

    @Override
    public void clear() {
        fornecedores.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return fornecedores.keySet();
    }

    @Override
    public java.util.Collection<Fornecedor> values() {
        return fornecedores.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Fornecedor>> entrySet() {
        return fornecedores.entrySet();
    }
}
