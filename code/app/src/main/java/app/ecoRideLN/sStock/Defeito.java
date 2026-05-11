package app.ecoRideLN.sStock;

public class Defeito {

    private int id;
    private int codStock;
    private String motivo;
    private int idFuncionario;
    private EstadoStock estadoAnterior;

    public Defeito(int id, int codStock, String motivo, int idFuncionario, EstadoStock estadoAnterior) {
        this.id = id;
        this.codStock = codStock;
        this.motivo = motivo;
        this.idFuncionario = idFuncionario;
        this.estadoAnterior = estadoAnterior;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCodStock() { return codStock; }
    public void setCodStock(int codStock) { this.codStock = codStock; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }

    public EstadoStock getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoStock estadoAnterior) { this.estadoAnterior = estadoAnterior; }
}
