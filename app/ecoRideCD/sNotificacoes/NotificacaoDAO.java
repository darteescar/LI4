package app.ecoRideCD.sNotificacoes;

import app.ecoRideLN.sNotificacoes.Notificacao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificacaoDAO implements Map<Integer, Notificacao> {

    private final Map<Integer, Notificacao> notificacoes = new HashMap<>();

    public Optional<Notificacao> obterPorId(int id) {
        return Optional.ofNullable(notificacoes.get(id));
    }

    public List<Notificacao> obterPorDestinatario(int idDestinatario) {
        return notificacoes.values().stream()
                .filter(n -> n.getId_destinatario() == idDestinatario)
                .collect(Collectors.toList());
    }

    public List<Notificacao> todas() {
        return notificacoes.values().stream().collect(Collectors.toList());
    }

    @Override
    public int size() {
        return notificacoes.size();
    }

    @Override
    public boolean isEmpty() {
        return notificacoes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return notificacoes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return notificacoes.containsValue(value);
    }

    @Override
    public Notificacao get(Object key) {
        return notificacoes.get(key);
    }

    @Override
    public Notificacao put(Integer key, Notificacao value) {
        return notificacoes.put(key, value);
    }

    @Override
    public Notificacao remove(Object key) {
        return notificacoes.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Notificacao> m) {
        notificacoes.putAll(m);
    }

    @Override
    public void clear() {
        notificacoes.clear();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return notificacoes.keySet();
    }

    @Override
    public java.util.Collection<Notificacao> values() {
        return notificacoes.values();
    }

    @Override
    public java.util.Set<Map.Entry<Integer, Notificacao>> entrySet() {
        return notificacoes.entrySet();
    }
}
