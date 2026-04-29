package app.ecoRideCD.sAutenticacao;

import app.ecoRideLN.sAutenticacao.Utilizador;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UtilizadorDAO implements Map<Integer, Utilizador> {

    private final Map<Integer, Utilizador> utilizadores = new HashMap<>();

    public Optional<Utilizador> obterPorId(int id) {
        return Optional.ofNullable(utilizadores.get(id));
    }

    public Optional<Utilizador> obterPorIdFuncionario(int idFuncionario) {
        return utilizadores.values().stream()
                .filter(u -> u.getIdFuncionario() == idFuncionario)
                .findFirst();
    }

    @Override
    public int size() {
        return utilizadores.size();
    }

    @Override
    public boolean isEmpty() {
        return utilizadores.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return utilizadores.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return utilizadores.containsValue(value);
    }

    @Override
    public Utilizador get(Object key) {
        return utilizadores.get(key);
    }

    @Override
    public Utilizador put(Integer key, Utilizador value) {
        return utilizadores.put(key, value);
    }

    @Override
    public Utilizador remove(Object key) {
        return utilizadores.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> m) {
        utilizadores.putAll(m);
    }

    @Override
    public void clear() {
        utilizadores.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return utilizadores.keySet();
    }

    @Override
    public java.util.Collection<Utilizador> values() {
        return utilizadores.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Utilizador>> entrySet() {
        return utilizadores.entrySet();
    }
}
