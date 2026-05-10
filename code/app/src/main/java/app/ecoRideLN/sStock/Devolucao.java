package app.ecoRideLN.sStock;

import java.time.LocalDate;

public class Devolucao {

    private int id;
    private LocalDate data;
    private String motivo;
    private EstadoDevolucao estado;
    private int codStock;

    public Devolucao(int id, LocalDate data, String motivo, int codStock) {
        this.id = id;
        this.data = data;
        this.motivo = motivo;
        this.estado = EstadoDevolucao.PendenteDevolucao;
        this.codStock = codStock;
    }

    public Devolucao(int id, LocalDate data, String motivo, EstadoDevolucao estado, int codStock) {
        this.id = id;
        this.data = data;
        this.motivo = motivo;
        this.estado = estado;
        this.codStock = codStock;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public EstadoDevolucao getEstado() { return estado; }
    public void setEstado(EstadoDevolucao estado) { this.estado = estado; }

    public int getCodStock() { return codStock; }
    public void setCodStock(int codStock) { this.codStock = codStock; }
}
