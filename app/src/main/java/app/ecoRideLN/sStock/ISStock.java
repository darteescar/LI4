package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ISStock {

    // ------------------- Fornecedor -------------------

    public Fornecedor registarFornecedor(String nome, String telemovel, String email);

    public void atualizarFornecedor(int id, String nome, String telemovel, String email);

    public Fornecedor obterFornecedor(int id);

    public boolean existeFornecedor(int id);

    public boolean removerFornecedor(int id);

    public List<Fornecedor> obterFornecedores();

    // ------------------- Peca -------------------

    public Peca registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor);

    public void atualizarPeca(int id, String referencia, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa);

    public Peca obterPeca(int id);

    public boolean existePeca_id(int id);

    public boolean existePeca_ref(String ref);

    public boolean removerPeca(int id);

    public List<Peca> obterPecas();

    // Utilitários

    public int obter_quantidade_Stock_Peca_id(int id);

    public List<Integer> obter_Pecas_baixo_Stock_minimo();

    // ------------------- Stock -------------------

    public Stock registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data, LocalDate garantia, String nr_serie);

    public Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade);

    public void atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado);

    public void atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado, LocalDate garantia, String nr_serie);

    public Stock obterStock(int id);

    public boolean existeStock(int id);

    public boolean removerStock(int id);

    public List<Stock> obterStocks();

    // Utilitários

    public void atualizaEstadoStock(int id, EstadoStock estado);

    public int pecasDefeituosas_Stock(int id_peca);

    public void registar_Defeito_entradaStock(int id_Stock);

    // ------------------- Devolucao -------------------

    public Devolucao registarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade);

    public void atualizarDevolucao(int id, LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade);

    public void devolverPecas(List<Integer> pecas);

    public Devolucao obterDevolucao(int id);

    public boolean existeDevolucao(int id);

    public boolean removerDevolucao(int id);

    public List<Devolucao> obterDevolucoes();

    // Utilitários

    public boolean validaEstadoDevolucao_PendenteDevolucao(int id);

    public void marcarDevolucaoComoEnviada(int id);

    public void marcarDevolucaoComoDevolvida(int id);

    public void marcarDevolucaoComoInvalida(int id);

    // ------------------- Encomenda -------------------

    public Encomenda registarEncomenda(List<ItemEncomenda> itens, int cod_fornecedor);

    public void atualizarEncomenda(int id, List<ItemEncomenda> itens, LocalDateTime data_pedido, LocalDateTime data_chegada, EstadoEncomenda estado);

    public Encomenda obterEncomenda(int id);

    public boolean removerEncomenda(int id);

    public List<Encomenda> obterEncomendas();

    // Utilitários
    
    public void marcarEncomendaComoEnviada(int id);

    public void marcarEncomendaComoRecebida(int id);

    public boolean validaEncomenda_Rascunho(int id_Encomenda);

    public int quantidade_encomendar_peca(int id_peca);

    public Map<Integer, List<ItemEncomenda>> gerarListaAutomatica();
}
