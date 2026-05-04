package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISStock {

    // ------------------- Fornecedor -------------------

    public Fornecedor registarFornecedor(String nome, String telemovel, String email);
    public Fornecedor obterDadosFornecedor(int id);
    public boolean    existeFornecedor(int id);
    public boolean    removerFornecedor(int id);
    public void atualizarFornecedor(int id, String nome, String telemovel, String email);

    // ------------------- Peca -------------------

    public Peca    registarPeca(String ref, int stock_minimo, float preco_venda, int id_fornecedor);
    public Peca    obterDadosPeca(int id);
    public boolean existePeca_id(int id);
    public boolean existePeca_ref(String ref);
    public boolean removerPeca(int id);
    public void atualizarPeca(int id, String referencia, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa);
    public int     obter_quantidade_Stock_Peca_id(int id);
    public int     obter_quantidade_Stock_Peca_ref(String referencia);
    public List<Integer> obter_Pecas_baixo_Stock_minimo();

    // ------------------- Stock -------------------

    public Stock   registarStock_PecaSuperior70(int id_peca, float preco_compra, LocalDateTime data,
                                         LocalDate garantia, String nr_serie);
    public Stock   registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade);
    public Stock   obterDadosStock(int id);
    public boolean existeStock(int id);
    public boolean removerStock(int id);
    public void    atualizaEstadoStock(int id, EstadoStock estado);
    public int     pecasDefeituosas_Stock(int id_peca);
    public void    registar_Defeito_entradaStock(int id_Stock);
    public void atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado);
    public void    atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado, LocalDate garantia, String nr_serie);

    // ------------------- Devolucao -------------------

    public Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock);
    public void      devolverPecas(List<Integer> pecas);
    public Devolucao obterDadosDevolucao(int id);
    public boolean   existeDevolucao(int id);
    public boolean   removerDevolucao(int id);
    public void      atualizarDataDevolucao(int id, LocalDateTime data);
    public void      atualizarMotivoDevolucao(int id, String motivo);
    public void      atualizarCodStockDevolucao(int id, int codStock);
    public void      atualizarEstadoDevolucao(int id, EstadoDevolucao estado);
    public boolean   validaEstadoDevolucao_PendenteDevolucao(int id);

    // ------------------- Encomenda -------------------

    public int       quantidade_encomendar_peca(int id_peca);
    public Encomenda criarEncomenda(List<Stock> pecas);
    public Encomenda obterDadosEncomenda(int id);
    public void      adicionar_PecasEncomenda_Stock(int idEncomenda, List<Stock> pecas);
    public boolean   removerEncomenda(int id);
    public boolean   validaEncomenda_Rascunho(int id_Encomenda);
    public void      atualizarPecasEncomenda(int id, List<Stock> pecas);
    public void      atualizarDataRececaoEncomenda(int id, LocalDateTime data_chegada);
    public void      atualizarDataEnvioEncomenda(int id, LocalDateTime data_pedido);
    public void      atualizarEstadoEncomenda(int id, EstadoEncomenda estado);
}
