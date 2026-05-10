package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Encomenda {

    private int id;
    private int codFornecedor;
    private LocalDate data_criacao;
    private LocalDate data_rececao;
    private LocalDate data_envio;
    private EstadoEncomenda estado;
    private List<ItemEncomenda> itensEncomendados;
    private List<Integer> codEntradasStock;

    public Encomenda(int id, int codFornecedor, List<ItemEncomenda> itensEncomendados) {
        this.id = id;
        this.codFornecedor = codFornecedor;
        this.data_criacao = LocalDate.now();
        this.data_rececao = null;
        this.data_envio = null;
        this.estado = EstadoEncomenda.RASCUNHO;
        this.itensEncomendados = new ArrayList<>(itensEncomendados);
        this.codEntradasStock = new ArrayList<>();
    }

    public Encomenda(int id, int codFornecedor, LocalDate data_criacao, LocalDate data_rececao,
            LocalDate data_envio, EstadoEncomenda estado,
            List<ItemEncomenda> itensEncomendados, List<Integer> codEntradasStock) {
        this.id = id;
        this.codFornecedor = codFornecedor;
        this.data_criacao = data_criacao;
        this.data_rececao = data_rececao;
        this.data_envio = data_envio;
        this.estado = estado;
        this.itensEncomendados = new ArrayList<>(itensEncomendados);
        this.codEntradasStock = new ArrayList<>(codEntradasStock);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodFornecedor() {
        return codFornecedor;
    }

    public void setCodFornecedor(int codFornecedor) {
        this.codFornecedor = codFornecedor;
    }

    public LocalDate getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDate data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDate getData_rececao() {
        return data_rececao;
    }

    public void setData_rececao(LocalDate data_rececao) {
        this.data_rececao = data_rececao;
    }

    public LocalDate getData_envio() {
        return data_envio;
    }

    public void setData_envio(LocalDate data_envio) {
        this.data_envio = data_envio;
    }

    public EstadoEncomenda getEstado() {
        return estado;
    }

    public void setEstado(EstadoEncomenda estado) {
        this.estado = estado;
    }

    public List<ItemEncomenda> getItensEncomendados() {
        return new ArrayList<>(itensEncomendados);
    }

    public void setItensEncomendados(List<ItemEncomenda> itensEncomendados) {
        this.itensEncomendados = new ArrayList<>(itensEncomendados);
    }

    public List<Integer> getCodEntradasStock() {
        return new ArrayList<>(codEntradasStock);
    }

    public void setCodEntradasStock(List<Integer> codEntradasStock) {
        this.codEntradasStock = new ArrayList<>(codEntradasStock);
    }

}
