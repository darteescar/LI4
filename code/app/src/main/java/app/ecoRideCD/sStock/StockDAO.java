package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sStock.Defeito;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.EstadoDevolucao;
import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.Stock;

public class StockDAO implements Map<Integer, Stock> {

    private static StockDAO instance;

    private StockDAO() {
    }

    public static StockDAO getInstance() {
        if (instance == null) {
            instance = new StockDAO();
        }
        return instance;
    }

    private Stock buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        float preco = rs.getFloat("preco_compra");
        int codPeca = rs.getInt("codPeca");
        var dataChegadaRaw = rs.getDate("data_chegada");
        var dataChegada = dataChegadaRaw != null ? dataChegadaRaw.toLocalDate() : null;
        int qtd = rs.getInt("quantidade");
        EstadoStock estado = rs.getString("estado") != null ? EstadoStock.valueOf(rs.getString("estado")) : null;
        var dataGarantiaRaw = rs.getDate("garantia");
        var dataGarantia = dataGarantiaRaw != null ? dataGarantiaRaw.toLocalDate() : null;
        return new Stock(id, preco, codPeca, dataChegada, qtd, estado, dataGarantia);
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Stock")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar stocks", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) {
            return false;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Stock WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar stock " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Stock s)) {
            return false;
        }
        Stock stored = get(s.getId());
        return stored != null && stored.getCodPeca() == s.getCodPeca();
    }

    @Override
    public Stock get(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        String sql = """
                SELECT * FROM Stock WHERE id = ?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stock " + id, e);
        }
    }

    @Override
    public Stock put(Integer key, Stock value) {
        Stock prev = get(key);
        String sql = """
                UPDATE Stock SET preco_compra=?, codPeca=?, data_chegada=?, quantidade=?, garantia=?, estado=?
                WHERE id=?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setFloat(1, value.getPreco_compra());
            ps.setInt(2, value.getCodPeca());
            if (value.getData_chegada() != null)
                ps.setDate(3, Date.valueOf(value.getData_chegada()));
            else
                ps.setNull(3, Types.DATE);
            ps.setInt(4, value.getQuantidade());
            if (value.getGarantia() != null)
                ps.setDate(5, Date.valueOf(value.getGarantia()));
            else
                ps.setNull(5, Types.DATE);
            ps.setString(6, value.getEstado().name());
            ps.setInt(7, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar stock " + key, e);
        }
        return prev;
    }

    public int insert(Stock value) {
        String sql = """
                INSERT INTO Stock (preco_compra, codPeca, data_chegada, quantidade, garantia, estado)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setFloat(1, value.getPreco_compra());
            ps.setInt(2, value.getCodPeca());
            if (value.getData_chegada() != null)
                ps.setDate(3, Date.valueOf(value.getData_chegada()));
            else
                ps.setNull(3, Types.DATE);
            ps.setInt(4, value.getQuantidade());
            if (value.getGarantia() != null)
                ps.setDate(5, Date.valueOf(value.getGarantia()));
            else
                ps.setNull(5, Types.DATE);
            ps.setString(6, value.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para stock");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir stock", e);
        }
    }

    @Override
    public Stock remove(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        Stock prev = get(id);
        if (prev == null) {
            return null;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM Stock WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover stock " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Stock");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar stocks", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id FROM Stock")) {
            while (rs.next()) {
                out.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de stocks", e);
        }
        return out;
    }

    @Override
    public Collection<Stock> values() {
        Set<Stock> out = new LinkedHashSet<>();
        String sql = """
                SELECT * FROM Stock
                """;
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Stock>> entrySet() {
        Set<Entry<Integer, Stock>> out = new HashSet<>();
        for (Stock st : values()) {
            out.add(new AbstractMap.SimpleEntry<>(st.getId(), st));
        }
        return out;
    }

    // --------- Aliases / domínio ---------

    public List<Stock> getByPecaId(int id_peca) {
        List<Stock> out = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE codPeca = ? ORDER BY data_chegada ASC, id ASC";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_peca);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks da peça " + id_peca, e);
        }
        return out;
    }

    public List<Stock> getOperacionais() {
        List<Stock> out = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE estado IN (?, ?) ORDER BY data_chegada ASC, id ASC";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, EstadoStock.StockEmArmazem.name());
            ps.setString(2, EstadoStock.StockEncomendado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks operacionais", e);
        }
        return out;
    }

    // --------- Helpers privados (usam Connection externa) ---------

    private Stock getStockConn(Connection c, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM Stock WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        }
    }

    private void updateStockConn(Connection c, Stock s) throws SQLException {
        String sql = "UPDATE Stock SET preco_compra=?, codPeca=?, data_chegada=?, quantidade=?, garantia=?, estado=? WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setFloat(1, s.getPreco_compra());
            ps.setInt(2, s.getCodPeca());
            if (s.getData_chegada() != null) ps.setDate(3, Date.valueOf(s.getData_chegada()));
            else ps.setNull(3, Types.DATE);
            ps.setInt(4, s.getQuantidade());
            if (s.getGarantia() != null) ps.setDate(5, Date.valueOf(s.getGarantia()));
            else ps.setNull(5, Types.DATE);
            ps.setString(6, s.getEstado().name());
            ps.setInt(7, s.getId());
            ps.executeUpdate();
        }
    }

    private int insertStockConn(Connection c, Stock s) throws SQLException {
        String sql = "INSERT INTO Stock (preco_compra, codPeca, data_chegada, quantidade, garantia, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setFloat(1, s.getPreco_compra());
            ps.setInt(2, s.getCodPeca());
            if (s.getData_chegada() != null) ps.setDate(3, Date.valueOf(s.getData_chegada()));
            else ps.setNull(3, Types.DATE);
            ps.setInt(4, s.getQuantidade());
            if (s.getGarantia() != null) ps.setDate(5, Date.valueOf(s.getGarantia()));
            else ps.setNull(5, Types.DATE);
            ps.setString(6, s.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); s.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para stock");
            }
        }
    }

    private Defeito getDefeitoConn(Connection c, int id) throws SQLException {
        String sql = "SELECT id, codStock, motivo, idFuncionario, estado_anterior FROM Defeito WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Defeito(rs.getInt("id"), rs.getInt("codStock"), rs.getString("motivo"),
                        rs.getInt("idFuncionario"), EstadoStock.valueOf(rs.getString("estado_anterior")));
            }
        }
    }

    private Defeito insertDefeitoConn(Connection c, int codStock, String motivo, int idFuncionario, EstadoStock estadoAnterior) throws SQLException {
        String sql = "INSERT INTO Defeito (codStock, motivo, idFuncionario, estado_anterior) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, codStock);
            ps.setString(2, motivo);
            ps.setInt(3, idFuncionario);
            ps.setString(4, estadoAnterior.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new EcoRideException("Sem ID gerado para defeito");
                return new Defeito(rs.getInt(1), codStock, motivo, idFuncionario, estadoAnterior);
            }
        }
    }

    private Devolucao insertDevolucaoConn(Connection c, int codStock, String motivo, LocalDate data) throws SQLException {
        String sql = "INSERT INTO Devolucao (data, motivo, estado, codStock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(data));
            ps.setString(2, motivo);
            ps.setString(3, EstadoDevolucao.StockPendenteDeDevolucao.name());
            ps.setInt(4, codStock);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new EcoRideException("Sem ID gerado para devolucao");
                return new Devolucao(rs.getInt(1), data, motivo, codStock);
            }
        }
    }

    // --------- Métodos transacionais públicos ---------

    public Map<Integer, Integer> atribuirFIFO(int codPeca, int quantidade) {
        Map<Integer, Integer> resultado = new LinkedHashMap<>();
        String selectSql = "SELECT * FROM Stock WHERE codPeca = ? AND estado = ? AND quantidade > 0 ORDER BY data_chegada ASC, id ASC";
        String updateSql = "UPDATE Stock SET quantidade=?, estado=? WHERE id=?";
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                List<Stock> candidatos = new ArrayList<>();
                try (PreparedStatement ps = c.prepareStatement(selectSql)) {
                    ps.setInt(1, codPeca);
                    ps.setString(2, EstadoStock.StockEmArmazem.name());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) candidatos.add(buildFromRow(rs));
                    }
                }
                int restante = quantidade;
                for (Stock s : candidatos) {
                    if (restante <= 0) break;
                    int consumir = Math.min(s.getQuantidade(), restante);
                    resultado.put(s.getId(), consumir);
                    int novaQtd = s.getQuantidade() - consumir;
                    String novoEstado = (novaQtd == 0) ? EstadoStock.StockUsadoConserto.name() : EstadoStock.StockEmArmazem.name();
                    try (PreparedStatement ps = c.prepareStatement(updateSql)) {
                        ps.setInt(1, novaQtd);
                        ps.setString(2, novoEstado);
                        ps.setInt(3, s.getId());
                        ps.executeUpdate();
                    }
                    restante -= consumir;
                }
                if (restante > 0) throw new EcoRideException("Stock insuficiente para a peça " + codPeca + ". Faltam " + restante + " unidades.");
                c.commit();
                return resultado;
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro em atribuirFIFO para peça " + codPeca, (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em atribuirFIFO", e);
        }
    }

    public List<Defeito> registarDefeito(int codPeca, String motivo, int idFuncionario) {
        List<Defeito> resultado = new ArrayList<>();
        String selectSql = "SELECT * FROM Stock WHERE codPeca = ? AND estado = ? AND quantidade > 0";
        String updateSql = "UPDATE Stock SET estado=? WHERE id=?";
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                List<Stock> candidatos = new ArrayList<>();
                try (PreparedStatement ps = c.prepareStatement(selectSql)) {
                    ps.setInt(1, codPeca);
                    ps.setString(2, EstadoStock.StockEmArmazem.name());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) candidatos.add(buildFromRow(rs));
                    }
                }
                if (candidatos.isEmpty())
                    throw new EcoRideException("Não foram encontrados stocks disponíveis para a peça " + codPeca + ".");
                for (Stock s : candidatos) {
                    try (PreparedStatement ps = c.prepareStatement(updateSql)) {
                        ps.setString(1, EstadoStock.StockComPossivelDefeito.name());
                        ps.setInt(2, s.getId());
                        ps.executeUpdate();
                    }
                    resultado.add(insertDefeitoConn(c, s.getId(), motivo, idFuncionario, EstadoStock.StockEmArmazem));
                }
                c.commit();
                return resultado;
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro a registar defeito para peça " + codPeca, (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em registarDefeito", e);
        }
    }

    public Devolucao registarDevolucao(int codStock, String motivo, LocalDate data) {
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                Stock s = getStockConn(c, codStock);
                if (s == null) throw new EcoRideException("Stock " + codStock + " não encontrado.");
                if (s.getEstado() != EstadoStock.StockComPossivelDefeito && s.getEstado() != EstadoStock.StockEmArmazem)
                    throw new EcoRideException("Stock " + codStock + " não está disponível (estado: " + s.getEstado() + ").");
                try (PreparedStatement ps = c.prepareStatement("UPDATE Stock SET estado=? WHERE id=?")) {
                    ps.setString(1, EstadoStock.StockPendenteDeDevolucao.name());
                    ps.setInt(2, codStock);
                    ps.executeUpdate();
                }
                Devolucao dev = insertDevolucaoConn(c, codStock, motivo, data);
                c.commit();
                return dev;
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro a registar devolução para stock " + codStock, (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em registarDevolucao", e);
        }
    }

    public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data) {
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                Defeito d = getDefeitoConn(c, idDefeito);
                if (d == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM Defeito WHERE id = ?")) {
                    ps.setInt(1, idDefeito);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE Stock SET estado=? WHERE id=?")) {
                    ps.setString(1, EstadoStock.StockPendenteDeDevolucao.name());
                    ps.setInt(2, d.getCodStock());
                    ps.executeUpdate();
                }
                Devolucao dev = insertDevolucaoConn(c, d.getCodStock(), motivo, data);
                c.commit();
                return dev;
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro a confirmar defeito " + idDefeito + " com devolução", (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em confirmarDefeitoComDevolucao", e);
        }
    }

    public void resolverDefeitoComSplit(int idDefeito, int qtdDefeituosa, String motivo, LocalDate data) {
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                Defeito defeito = getDefeitoConn(c, idDefeito);
                if (defeito == null) throw new EcoRideException("Defeito " + idDefeito + " não encontrado.");
                Stock original = getStockConn(c, defeito.getCodStock());
                if (original == null) throw new EcoRideException("Stock associado ao defeito não encontrado.");
                if (qtdDefeituosa <= 0 || qtdDefeituosa > original.getQuantidade())
                    throw new EcoRideException("Quantidade inválida — tem de ser entre 1 e " + original.getQuantidade() + ".");

                try (PreparedStatement ps = c.prepareStatement("DELETE FROM Defeito WHERE id = ?")) {
                    ps.setInt(1, idDefeito);
                    ps.executeUpdate();
                }

                if (qtdDefeituosa == original.getQuantidade()) {
                    try (PreparedStatement ps = c.prepareStatement("UPDATE Stock SET estado=? WHERE id=?")) {
                        ps.setString(1, EstadoStock.StockPendenteDeDevolucao.name());
                        ps.setInt(2, original.getId());
                        ps.executeUpdate();
                    }
                    insertDevolucaoConn(c, original.getId(), motivo, data);
                } else {
                    original.setQuantidade(original.getQuantidade() - qtdDefeituosa);
                    original.setEstado(defeito.getEstadoAnterior());
                    updateStockConn(c, original);

                    Stock novoStock = new Stock(0, original.getPreco_compra(), original.getCodPeca(),
                            original.getData_chegada(), qtdDefeituosa,
                            EstadoStock.StockPendenteDeDevolucao, original.getGarantia());
                    insertStockConn(c, novoStock);
                    insertDevolucaoConn(c, novoStock.getId(), motivo, data);
                }
                c.commit();
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro a resolver defeito com split " + idDefeito, (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em resolverDefeitoComSplit", e);
        }
    }

    public void marcarDevolucaoComoEnviada(int idDevolucao) {
        transicionarDevolucao(idDevolucao, EstadoStock.StockEnviadoParaFornecedor, EstadoDevolucao.Enviada);
    }

    public void marcarDevolucaoComoDevolvida(int idDevolucao) {
        transicionarDevolucao(idDevolucao, EstadoStock.StockDevolvidoFornecedor, EstadoDevolucao.Devolvida);
    }

    public void marcarDevolucaoComoInvalida(int idDevolucao) {
        transicionarDevolucao(idDevolucao, EstadoStock.StockinvalidoParaDevolucao, EstadoDevolucao.Invalida);
    }

    private void transicionarDevolucao(int idDevolucao, EstadoStock novoEstadoStock, EstadoDevolucao novoEstadoDevolucao) {
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                int codStock;
                try (PreparedStatement ps = c.prepareStatement("SELECT codStock FROM Devolucao WHERE id = ?")) {
                    ps.setInt(1, idDevolucao);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new EcoRideException("Devolução " + idDevolucao + " não encontrada.");
                        codStock = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE Stock SET estado=? WHERE id=?")) {
                    ps.setString(1, novoEstadoStock.name());
                    ps.setInt(2, codStock);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE Devolucao SET estado=? WHERE id=?")) {
                    ps.setString(1, novoEstadoDevolucao.name());
                    ps.setInt(2, idDevolucao);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof EcoRideException er) throw er;
                throw new EcoRideException("Erro a transicionar devolução " + idDevolucao, (SQLException) e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação em transicionarDevolucao", e);
        }
    }
}
