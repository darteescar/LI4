package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;

public class Pagamento {

    private Metodo_Pagamento metodo;
    private LocalDateTime dataPagamento;
    private boolean clienteNotificado;
    private LocalDateTime dataNotificacao;

    /** Usado ao registar notificação — método e data de pagamento ainda desconhecidos. */
    public Pagamento(boolean clienteNotificado, LocalDateTime dataNotificacao) {
        this.clienteNotificado = clienteNotificado;
        this.dataNotificacao = dataNotificacao;
        this.metodo = null;
        this.dataPagamento = null;
    }

    /** Usado pelo EcoRideController antes de delegar ao subsistema — campos de notificação são preenchidos pela facade. */
    public Pagamento(Metodo_Pagamento metodo, LocalDateTime dataPagamento) {
        this.metodo = metodo;
        this.dataPagamento = dataPagamento;
        this.clienteNotificado = false;
        this.dataNotificacao = null;
    }

    /** Usado pelo DAO ao reconstruir do BD. */
    public Pagamento(Metodo_Pagamento metodo, LocalDateTime dataPagamento, boolean clienteNotificado, LocalDateTime dataNotificacao) {
        this.metodo = metodo;
        this.dataPagamento = dataPagamento;
        this.clienteNotificado = clienteNotificado;
        this.dataNotificacao = dataNotificacao;
    }

    public Metodo_Pagamento getMetodo() { return metodo; }
    public void setMetodo(Metodo_Pagamento metodo) { this.metodo = metodo; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public boolean isClienteNotificado() { return clienteNotificado; }
    public void setClienteNotificado(boolean clienteNotificado) { this.clienteNotificado = clienteNotificado; }

    public LocalDateTime getDataNotificacao() { return dataNotificacao; }
    public void setDataNotificacao(LocalDateTime dataNotificacao) { this.dataNotificacao = dataNotificacao; }
}
