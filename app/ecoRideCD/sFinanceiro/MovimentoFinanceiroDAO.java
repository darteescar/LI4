package app.ecoRideCD.sFinanceiro;

import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MovimentoFinanceiroDAO implements Map<Integer, MovimentoFinanceiro> {

    private final Map<Integer, MovimentoFinanceiro> movimentos = new HashMap<>();

    public Optional<MovimentoFinanceiro> obterPorId(int id) {
        return Optional.ofNullable(movimentos.get(id));
    }

    @Override
    public int size() {
        return movimentos.size();
    }

    @Override
    public boolean isEmpty() {
        return movimentos.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return movimentos.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return movimentos.containsValue(value);
    }

    @Override
    public MovimentoFinanceiro get(Object key) {
        return movimentos.get(key);
    }

    @Override
    public MovimentoFinanceiro put(Integer key, MovimentoFinanceiro value) {
        return movimentos.put(key, value);
    }

    @Override
    public MovimentoFinanceiro remove(Object key) {
        return movimentos.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends MovimentoFinanceiro> m) {
        movimentos.putAll(m);
    }

    @Override
    public void clear() {
        movimentos.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return movimentos.keySet();
    }

    @Override
    public java.util.Collection<MovimentoFinanceiro> values() {
        return movimentos.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, MovimentoFinanceiro>> entrySet() {
        return movimentos.entrySet();
    }
}
