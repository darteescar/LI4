package app.ecoRideCD.sStock;

import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.Stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StockDAO implements Map<Integer, Stock> {

    private final Map<Integer, Stock> stocks = new HashMap<>();

    public Optional<Stock> obterPorId(int id) {
        return Optional.ofNullable(stocks.get(id));
    }

    public List<Stock> obterPorPeca(int codPeca) {
        return stocks.values().stream()
                .filter(s -> s.getCodPeca() == codPeca)
                .collect(Collectors.toList());
    }

    public List<Stock> obterPorPecaEEstado(int codPeca, EstadoStock estado) {
        return stocks.values().stream()
                .filter(s -> s.getCodPeca() == codPeca && s.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public long quantidadePorPecaEEstado(int codPeca, EstadoStock estado) {
        return stocks.values().stream()
                .filter(s -> s.getCodPeca() == codPeca && s.getEstado() == estado)
                .count();
    }

    @Override
    public int size() {
        return stocks.size();
    }

    @Override
    public boolean isEmpty() {
        return stocks.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return stocks.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return stocks.containsValue(value);
    }

    @Override
    public Stock get(Object key) {
        return stocks.get(key);
    }

    @Override
    public Stock put(Integer key, Stock value) {
        return stocks.put(key, value);
    }

    @Override
    public Stock remove(Object key) {
        return stocks.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {
        stocks.putAll(m);
    }

    @Override
    public void clear() {
        stocks.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return stocks.keySet();
    }

    @Override
    public java.util.Collection<Stock> values() {
        return stocks.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Stock>> entrySet() {
        return stocks.entrySet();
    }
}
