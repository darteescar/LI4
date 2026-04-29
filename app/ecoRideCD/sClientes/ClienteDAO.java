package app.ecoRideCD.sClientes;

import app.ecoRideLN.sClientes.Cliente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClienteDAO implements Map<Integer, Cliente> {

    private final Map<Integer, Cliente> clientes = new HashMap<>();

    public Optional<Cliente> obterPorId(int id) {
        return Optional.ofNullable(clientes.get(id));
    }

    public Optional<Cliente> obterPorNif(String nif) {
        return clientes.values().stream()
                .filter(c -> nif.equals(c.getNIF()))
                .findFirst();
    }

    @Override
    public int size() {
        return clientes.size();
    }

    @Override
    public boolean isEmpty() {
        return clientes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return clientes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return clientes.containsValue(value);
    }

    @Override
    public Cliente get(Object key) {
        return clientes.get(key);
    }

    @Override
    public Cliente put(Integer key, Cliente value) {
        return clientes.put(key, value);
    }

    @Override
    public Cliente remove(Object key) {
        return clientes.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Cliente> m) {
        clientes.putAll(m);
    }

    @Override
    public void clear() {
        clientes.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return clientes.keySet();
    }

    @Override
    public java.util.Collection<Cliente> values() {
        return clientes.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Cliente>> entrySet() {
        return clientes.entrySet();
    }
}
