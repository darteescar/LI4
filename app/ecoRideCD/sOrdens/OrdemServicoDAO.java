package app.ecoRideCD.sOrdens;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sOrdens.Checklist;
import app.ecoRideLN.sOrdens.Conserto;
import app.ecoRideLN.sOrdens.Diagnostico;
import app.ecoRideLN.sOrdens.EstadoOS;
import app.ecoRideLN.sOrdens.OrdemServico;
import app.ecoRideLN.sStock.PecasDoOrcamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class OrdemServicoDAO implements Map<Integer, OrdemServico> {

    private static OrdemServicoDAO singleton = null;

    private OrdemServicoDAO() {}

    public static OrdemServicoDAO getInstance() {
        if (singleton == null) singleton = new OrdemServicoDAO();
        return singleton;
    }

    public Optional<OrdemServico> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public List<OrdemServico> obterPorCliente(int idCliente) {
        return filtrar("SELECT id FROM OrdemServico WHERE codCliente_FK=?", idCliente);
    }

    public List<OrdemServico> obterPorFuncionario(int idFuncionario) {
        return filtrar("SELECT id FROM OrdemServico WHERE codResponsavel_FK=?", idFuncionario);
    }

    private List<OrdemServico> filtrar(String sql, int param) {
        List<OrdemServico> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, param);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    OrdemServico os = this.get(rs.getInt("id"));
                    if (os != null) res.add(os);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    private OrdemServico fromRS(ResultSet rs, Connection conn) throws SQLException {
        Timestamp ts = rs.getTimestamp("data_criacao");
        OrdemServico os = new OrdemServico(rs.getInt("id"), rs.getString("descricao"),
                rs.getInt("codCliente_FK"), rs.getInt("codTrotinete_FK"),
                ts == null ? null : ts.toLocalDateTime(),
                rs.getInt("codResponsavel_FK"));
        os.setEstado(EstadoOS.valueOf(rs.getString("estado")));

        // load acessorios
        try (PreparedStatement pstm = conn.prepareStatement("SELECT acessorio FROM AcessorioOS WHERE idOS_FK=?")) {
            pstm.setInt(1, os.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                while (rs2.next()) os.getAcessorios().add(rs2.getString("acessorio"));
            }
        }
        // load fotografias
        try (PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Fotografia WHERE idOS_FK=?")) {
            pstm.setInt(1, os.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                while (rs2.next()) os.getCodFotografias().add(rs2.getInt("id"));
            }
        }
        // load diagnostico
        try (PreparedStatement pstm = conn.prepareStatement(
                "SELECT descricao, orcamento, codMecanico_FK FROM Diagnostico WHERE idOS_FK=?")) {
            pstm.setInt(1, os.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                if (rs2.next()) {
                    Diagnostico d = new Diagnostico(rs2.getString("descricao"),
                            rs2.getFloat("orcamento"), rs2.getInt("codMecanico_FK"));
                    try (PreparedStatement r = conn.prepareStatement("SELECT idReparacao_FK FROM DiagnosticoReparacao WHERE idOS_FK=?")) {
                        r.setInt(1, os.getId());
                        try (ResultSet rrs = r.executeQuery()) {
                            while (rrs.next()) d.getCod_reparacoes().add(rrs.getInt("idReparacao_FK"));
                        }
                    }
                    try (PreparedStatement p = conn.prepareStatement("SELECT idPeca_FK, quantidade FROM DiagnosticoPeca WHERE idOS_FK=?")) {
                        p.setInt(1, os.getId());
                        try (ResultSet prs = p.executeQuery()) {
                            while (prs.next())
                                d.getListaPecas().add(new PecasDoOrcamento(prs.getInt("idPeca_FK"), prs.getInt("quantidade")));
                        }
                    }
                    os.setDiagnostico(d);
                }
            }
        }
        // load conserto + checklist
        try (PreparedStatement pstm = conn.prepareStatement(
                "SELECT preco_total, codMecanico_FK FROM Conserto WHERE idOS_FK=?")) {
            pstm.setInt(1, os.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                if (rs2.next()) {
                    Conserto c = new Conserto(rs2.getInt("codMecanico_FK"));
                    c.setPreco_total(rs2.getFloat("preco_total"));
                    try (PreparedStatement r = conn.prepareStatement("SELECT idReparacao_FK FROM ConsertoReparacao WHERE idOS_FK=?")) {
                        r.setInt(1, os.getId());
                        try (ResultSet rrs = r.executeQuery()) {
                            while (rrs.next()) c.getCod_reparacoes().add(rrs.getInt("idReparacao_FK"));
                        }
                    }
                    try (PreparedStatement r = conn.prepareStatement("SELECT idStock_FK FROM ConsertoStock WHERE idOS_FK=?")) {
                        r.setInt(1, os.getId());
                        try (ResultSet rrs = r.executeQuery()) {
                            while (rrs.next()) c.getCod_pecas().add(rrs.getInt("idStock_FK"));
                        }
                    }
                    try (PreparedStatement r = conn.prepareStatement(
                            "SELECT travoes, luzes, pneus, aceleracao, travagem, visor, teste_pratico FROM Checklist WHERE idOS_FK=?")) {
                        r.setInt(1, os.getId());
                        try (ResultSet rrs = r.executeQuery()) {
                            if (rrs.next()) {
                                c.setCheckList(new Checklist(rrs.getBoolean("travoes"), rrs.getBoolean("luzes"),
                                        rrs.getBoolean("pneus"), rrs.getBoolean("aceleracao"),
                                        rrs.getBoolean("travagem"), rrs.getBoolean("visor"),
                                        rrs.getBoolean("teste_pratico")));
                            }
                        }
                    }
                    os.setConserto(c);
                }
            }
        }
        return os;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM OrdemServico")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM OrdemServico")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM OrdemServico WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof OrdemServico && containsKey(((OrdemServico) value).getId());
    }

    @Override public OrdemServico get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM OrdemServico WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs, conn);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public OrdemServico put(Integer key, OrdemServico value) {
        OrdemServico prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Timestamp ts = value.getData_criacao() == null ? null : Timestamp.valueOf(value.getData_criacao());
                if (prev != null) {
                    try (PreparedStatement pstm = conn.prepareStatement(
                            "UPDATE OrdemServico SET descricao=?, codCliente_FK=?, codTrotinete_FK=?, "
                                    + "data_criacao=?, codResponsavel_FK=?, estado=? WHERE id=?")) {
                        pstm.setString(1, value.getDescricao());
                        pstm.setInt(2, value.getCodCliente());
                        pstm.setInt(3, value.getCodTrotinete());
                        pstm.setTimestamp(4, ts);
                        pstm.setInt(5, value.getCodResponsavel());
                        pstm.setString(6, value.getEstado().name());
                        pstm.setInt(7, value.getId());
                        pstm.executeUpdate();
                    }
                    try (Statement stm = conn.createStatement()) {
                        stm.executeUpdate("DELETE FROM AcessorioOS WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM DiagnosticoReparacao WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM DiagnosticoPeca WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM Diagnostico WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM ConsertoReparacao WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM ConsertoStock WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM Checklist WHERE idOS_FK=" + value.getId());
                        stm.executeUpdate("DELETE FROM Conserto WHERE idOS_FK=" + value.getId());
                    }
                } else {
                    try (PreparedStatement pstm = conn.prepareStatement(
                            "INSERT INTO OrdemServico (id, descricao, codCliente_FK, codTrotinete_FK, "
                                    + "data_criacao, codResponsavel_FK, estado) VALUES (?,?,?,?,?,?,?)")) {
                        pstm.setInt(1, value.getId());
                        pstm.setString(2, value.getDescricao());
                        pstm.setInt(3, value.getCodCliente());
                        pstm.setInt(4, value.getCodTrotinete());
                        pstm.setTimestamp(5, ts);
                        pstm.setInt(6, value.getCodResponsavel());
                        pstm.setString(7, value.getEstado().name());
                        pstm.executeUpdate();
                    }
                }

                // Insert acessorios
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO AcessorioOS (idOS_FK, acessorio) VALUES (?,?)")) {
                    for (String a : value.getAcessorios()) {
                        pstm.setInt(1, value.getId());
                        pstm.setString(2, a);
                        pstm.executeUpdate();
                    }
                }
                // Insert diagnostico if present
                Diagnostico d = value.getDiagnostico();
                if (d != null) {
                    try (PreparedStatement pstm = conn.prepareStatement(
                            "INSERT INTO Diagnostico (idOS_FK, descricao, orcamento, codMecanico_FK) VALUES (?,?,?,?)")) {
                        pstm.setInt(1, value.getId());
                        pstm.setString(2, d.getDescricao());
                        pstm.setFloat(3, d.getOrcamento());
                        pstm.setInt(4, d.getCodMecanico());
                        pstm.executeUpdate();
                    }
                    try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO DiagnosticoReparacao (idOS_FK, idReparacao_FK) VALUES (?,?)")) {
                        for (Integer r : d.getCod_reparacoes()) {
                            pstm.setInt(1, value.getId());
                            pstm.setInt(2, r);
                            pstm.executeUpdate();
                        }
                    }
                    try (PreparedStatement pstm = conn.prepareStatement(
                            "INSERT INTO DiagnosticoPeca (idOS_FK, idPeca_FK, quantidade) VALUES (?,?,?)")) {
                        for (PecasDoOrcamento qp : d.getListaPecas()) {
                            pstm.setInt(1, value.getId());
                            pstm.setInt(2, qp.getCodPeca());
                            pstm.setInt(3, qp.getQuantidade());
                            pstm.executeUpdate();
                        }
                    }
                }
                // Insert conserto if present
                Conserto c = value.getConserto();
                if (c != null) {
                    try (PreparedStatement pstm = conn.prepareStatement(
                            "INSERT INTO Conserto (idOS_FK, preco_total, codMecanico_FK) VALUES (?,?,?)")) {
                        pstm.setInt(1, value.getId());
                        pstm.setFloat(2, c.getPreco_total());
                        pstm.setInt(3, c.getCodMecanico());
                        pstm.executeUpdate();
                    }
                    try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO ConsertoReparacao (idOS_FK, idReparacao_FK) VALUES (?,?)")) {
                        for (Integer r : c.getCod_reparacoes()) {
                            pstm.setInt(1, value.getId());
                            pstm.setInt(2, r);
                            pstm.executeUpdate();
                        }
                    }
                    try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO ConsertoStock (idOS_FK, idStock_FK) VALUES (?,?)")) {
                        for (Integer s : c.getCod_pecas()) {
                            pstm.setInt(1, value.getId());
                            pstm.setInt(2, s);
                            pstm.executeUpdate();
                        }
                    }
                    if (c.getCheckList() != null) {
                        Checklist cl = c.getCheckList();
                        try (PreparedStatement pstm = conn.prepareStatement(
                                "INSERT INTO Checklist (idOS_FK, travoes, luzes, pneus, aceleracao, travagem, visor, teste_pratico) "
                                        + "VALUES (?,?,?,?,?,?,?,?)")) {
                            pstm.setInt(1, value.getId());
                            pstm.setBoolean(2, cl.isTravoes());
                            pstm.setBoolean(3, cl.isLuzes());
                            pstm.setBoolean(4, cl.isPneus());
                            pstm.setBoolean(5, cl.isAceleracao());
                            pstm.setBoolean(6, cl.isTravagem());
                            pstm.setBoolean(7, cl.isVisor());
                            pstm.setBoolean(8, cl.isTeste_pratico());
                            pstm.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public OrdemServico remove(Object key) {
        OrdemServico os = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement()) {
            int id = (Integer) key;
            stm.executeUpdate("DELETE FROM AcessorioOS WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM DiagnosticoReparacao WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM DiagnosticoPeca WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM Diagnostico WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM ConsertoReparacao WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM ConsertoStock WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM Checklist WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM Conserto WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM Fotografia WHERE idOS_FK=" + id);
            stm.executeUpdate("DELETE FROM OrdemServico WHERE id=" + id);
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return os;
    }

    @Override public void putAll(Map<? extends Integer, ? extends OrdemServico> m) {
        for (OrdemServico os : m.values()) put(os.getId(), os);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM AcessorioOS");
            stm.executeUpdate("DELETE FROM DiagnosticoReparacao");
            stm.executeUpdate("DELETE FROM DiagnosticoPeca");
            stm.executeUpdate("DELETE FROM Diagnostico");
            stm.executeUpdate("DELETE FROM ConsertoReparacao");
            stm.executeUpdate("DELETE FROM ConsertoStock");
            stm.executeUpdate("DELETE FROM Checklist");
            stm.executeUpdate("DELETE FROM Conserto");
            stm.executeUpdate("DELETE FROM Fotografia");
            stm.executeUpdate("DELETE FROM OrdemServico");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM OrdemServico")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<OrdemServico> values() {
        Collection<OrdemServico> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM OrdemServico")) {
            while (rs.next()) res.add(fromRS(rs, conn));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, OrdemServico>> entrySet() {
        Set<Map.Entry<Integer, OrdemServico>> res = new HashSet<>();
        for (OrdemServico os : values()) res.add(new AbstractMap.SimpleEntry<>(os.getId(), os));
        return res;
    }
}
