package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISStock {

    // ------------------- Fornecedor -------------------

    Fornecedor registarFornecedor(String nome, String telemovel, String email);
    Fornecedor obterDadosFornecedor(int id);
    boolean    existeFornecedor(int id);
    boolean    removerFornecedor(int id);
    void       atualizarNomeFornecedor(int id, String nome);
    void       atualizarTelemovelFornecedor(int id, String telemovel);
    void       atualizarEmailFornecedor(int id, String email);

    // ------------------- Peca -------------------

    Peca    registarPeca(String ref, int stock_minimo, float preco_venda, int id_fornecedor);
    Peca    obterDadosPeca(int id);
    boolean existePeca_id(int id);
    boolean existePeca_ref(String ref);
    boolean removerPeca(int id);
    void    atualizarReferenciaPeca(int id, String referencia);
    void    atualizarStockMinimoPeca(int id, int stock_minimo);
    void    atualizarPrecoVendaPeca(int id, float preco_venda);
    void    atualizarIdFornecedorPeca(int id, int id_fornecedor);
    void    atualizarFlagDisponibilidadePeca(int id, boolean novaFlag);
    void    aumentarQuantidadePeca(int idPeca);
    void    diminuirQuantidadePeca(int idPeca);
    int     obter_quantidade_Stock_Peca_id(int id);
    int     obter_quantidade_Stock_Peca_ref(String referencia);
    List<Integer> obter_Pecas_baixo_Stock_minimo();

    // ------------------- Stock -------------------

    Stock   registarStock_PecaSuperior70(int id_peca, float preco_compra, LocalDateTime data,
                                         LocalDate garantia, String nr_serie);
    Stock   registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data);
    Stock   obterDadosStock(int id);
    boolean existeStock(int id);
    boolean removerStock(int id);
    void    atualizaEstadoStock(int id, EstadoStock estado);
    int     pecasDefeituosas_Stock(int id_peca);
    void    registar_Defeito_entradaStock(int id_Stock);
    void    atualizarIdPecaStock(int id_stock, int id_peca);
    void    atualizarPrecoCompraStock(int id_stock, float preco_compra);
    void    atualizarDataRececaoStock(int id_stock, LocalDateTime data_rececao);
    void    atualizarGarantiaStock(int id_stock, LocalDate garantia);
    void    atualizarNrSerieStock(int id_stock, String nr_serie);

    // ------------------- Devolucao -------------------

    Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock);
    void      devolverPecas(List<Integer> pecas);
    Devolucao obterDadosDevolucao(int id);
    boolean   existeDevolucao(int id);
    boolean   removerDevolucao(int id);
    void      atualizarDataDevolucao(int id, LocalDateTime data);
    void      atualizarMotivoDevolucao(int id, String motivo);
    void      atualizarCodStockDevolucao(int id, int codStock);
    void      atualizarEstadoDevolucao(int id, EstadoDevolucao estado);
    boolean   validaEstadoDevolucao_PendenteDevolucao(int id);

    // ------------------- Encomenda -------------------

    int       quantidade_encomendar_peca(int id_peca);
    Encomenda criarEncomenda(List<Stock> pecas);
    Encomenda obterDadosEncomenda(int id);
    void      adicionar_PecasEncomenda_Stock(int idEncomenda, List<Stock> pecas);
    boolean   removerEncomenda(int id);
    boolean   validaEncomenda_Rascunho(int id_Encomenda);
    void      atualizarPecasEncomenda(int id, List<Stock> pecas);
    void      atualizarDataRececaoEncomenda(int id, LocalDateTime data_chegada);
    void      atualizarDataEnvioEncomenda(int id, LocalDateTime data_pedido);
    void      atualizarEstadoEncomenda(int id, EstadoEncomenda estado);
}
