package app.ecoRideLN.sStock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Encomenda {

    private int id;
    private int codFornecedor;
    private LocalDateTime data_envio;
    private LocalDateTime data_rececao;
    private final List<Integer> codEntradasStock;
    private EstadoEncomenda estado;

    public Encomenda(int id, int codFornecedor) {
        this.id = id;
        this.codFornecedor = codFornecedor;
        this.codEntradasStock = new ArrayList<>();
        this.estado = EstadoEncomenda.RASCUNHO;
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

    public LocalDateTime getData_envio() {
        return data_envio;
    }

    public void setData_envio(LocalDateTime data_envio) {
        this.data_envio = data_envio;
    }

    public LocalDateTime getData_rececao() {
        return data_rececao;
    }

    public void setData_rececao(LocalDateTime data_rececao) {
        this.data_rececao = data_rececao;
    }

    public List<Integer> getCodEntradasStock() {
        return codEntradasStock;
    }

    public EstadoEncomenda getEstado() {
        return estado;
    }

    public void setEstado(EstadoEncomenda estado) {
        this.estado = estado;
    }
}
