package app.ecoRideLN.sStock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sStock.StockDAO;

public class Encomenda {

    private int id;
    private int codFornecedor;
    private LocalDateTime data_criacao;
    private LocalDateTime data_rececao;
    private LocalDateTime data_envio;
    private EstadoEncomenda estado;
    private List<ItemEncomenda> itensEncomendados;
    private List<Integer> codEntradasStock;

    private static final StockDAO stockDAO = StockDAO.getInstance();

    public Encomenda(int id, int codFornecedor, List<ItemEncomenda> itensEncomendados) {
        this.id = id;
        this.codFornecedor = codFornecedor;
        this.data_criacao = LocalDateTime.now();
        this.data_rececao = null;
        this.data_envio = null;
        this.estado = EstadoEncomenda.RASCUNHO;
        this.itensEncomendados = new ArrayList<>(itensEncomendados);
        this.codEntradasStock = new ArrayList<>();
    }

    public Encomenda(int id, int codFornecedor, LocalDateTime data_criacao, LocalDateTime data_rececao,
            LocalDateTime data_envio, EstadoEncomenda estado,
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

    public LocalDateTime getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDateTime data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDateTime getData_rececao() {
        return data_rececao;
    }

    public void setData_rececao(LocalDateTime data_rececao) {
        this.data_rececao = data_rececao;
    }

    public LocalDateTime getData_envio() {
        return data_envio;
    }

    public void setData_envio(LocalDateTime data_envio) {
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

    public List<Stock> getEntradasStock() {
        List<Stock> entradas = new ArrayList<>();
        for (Integer codEntrada : codEntradasStock) {
            Stock stock = stockDAO.get(codEntrada);
            if (stock != null) {
                entradas.add(stock);
            }
        }
        return entradas;
    }
}
