package app.ecoRideCD.sFuncionarios;

import app.ecoRideLN.sFuncionarios.Funcionario;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FuncionarioDAO implements Map<Integer, Funcionario> {

    private final Map<Integer, Funcionario> funcionarios = new HashMap<>();

    public Optional<Funcionario> obterPorId(int id) {
        return Optional.ofNullable(funcionarios.get(id));
    }

    public Optional<Funcionario> obterPorNif(String nif) {
        return funcionarios.values().stream()
                .filter(f -> nif.equals(f.getNIF()))
                .findFirst();
    }

    @Override
    public int size() {
        return funcionarios.size();
    }

    @Override
    public boolean isEmpty() {
        return funcionarios.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return funcionarios.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return funcionarios.containsValue(value);
    }

    @Override
    public Funcionario get(Object key) {
        return funcionarios.get(key);
    }

    @Override
    public Funcionario put(Integer key, Funcionario value) {
        return funcionarios.put(key, value);
    }

    @Override
    public Funcionario remove(Object key) {
        return funcionarios.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Funcionario> m) {
        funcionarios.putAll(m);
    }

    @Override
    public void clear() {
        funcionarios.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return funcionarios.keySet();
    }

    @Override
    public java.util.Collection<Funcionario> values() {
        return funcionarios.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Funcionario>> entrySet() {
        return funcionarios.entrySet();
    }
}
