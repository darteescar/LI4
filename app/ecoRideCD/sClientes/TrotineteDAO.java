package app.ecoRideCD.sClientes;

import app.ecoRideLN.sClientes.Trotinete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrotineteDAO implements Map<Integer, Trotinete> {

    private final Map<Integer, Trotinete> trotinetes = new HashMap<>();

    public Optional<Trotinete> obterPorId(int id) {
        return Optional.ofNullable(trotinetes.get(id));
    }

    public List<Trotinete> obterPorCliente(int idCliente) {
        return trotinetes.values().stream()
                .filter(t -> t.getCodCliente() == idCliente)
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return trotinetes.size();
    }

    @Override
    public boolean isEmpty() {
        return trotinetes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return trotinetes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return trotinetes.containsValue(value);
    }

    @Override
    public Trotinete get(Object key) {
        return trotinetes.get(key);
    }

    @Override
    public Trotinete put(Integer key, Trotinete value) {
        return trotinetes.put(key, value);
    }

    @Override
    public Trotinete remove(Object key) {
        return trotinetes.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Trotinete> m) {
        trotinetes.putAll(m);
    }

    @Override
    public void clear() {
        trotinetes.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return trotinetes.keySet();
    }

    @Override
    public java.util.Collection<Trotinete> values() {
        return trotinetes.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Trotinete>> entrySet() {
        return trotinetes.entrySet();
    }
}
