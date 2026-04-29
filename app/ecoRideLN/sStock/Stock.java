package app.ecoRideLN.sStock;

import java.time.LocalDateTime;

public class Stock {

    private int id;
    private float preco_compra;
    private int codPeca;
    private LocalDateTime data_chegada;
    private EstadoStock estado;
    private String nr_serie;
    private Integer garantia;

    public Stock(int id, float preco_compra, int codPeca, LocalDateTime data_chegada,
                 String nr_serie, Integer garantia) {
        this.id = id;
        this.preco_compra = preco_compra;
        this.codPeca = codPeca;
        this.data_chegada = data_chegada;
        this.estado = EstadoStock.EM_STOCK;
        this.nr_serie = nr_serie;
        this.garantia = garantia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPreco_compra() {
        return preco_compra;
    }

    public void setPreco_compra(float preco_compra) {
        this.preco_compra = preco_compra;
    }

    public int getCodPeca() {
        return codPeca;
    }

    public void setCodPeca(int codPeca) {
        this.codPeca = codPeca;
    }

    public LocalDateTime getData_chegada() {
        return data_chegada;
    }

    public void setData_chegada(LocalDateTime data_chegada) {
        this.data_chegada = data_chegada;
    }

    public EstadoStock getEstado() {
        return estado;
    }

    public void setEstado(EstadoStock estado) {
        this.estado = estado;
    }

    public String getNr_serie() {
        return nr_serie;
    }

    public void setNr_serie(String nr_serie) {
        this.nr_serie = nr_serie;
    }

    public Integer getGarantia() {
        return garantia;
    }

    public void setGarantia(Integer garantia) {
        this.garantia = garantia;
    }

    public boolean temGarantia() {
        return nr_serie != null && garantia != null;
    }
}
