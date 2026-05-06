package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ISStock {

     // ------------------- Fornecedor -------------------

     Fornecedor registarFornecedor(String nome, String telemovel, String email);
     void atualizarFornecedor(int id, String nome, String telemovel, String email);
     Fornecedor obterFornecedor(int id);
     boolean existeFornecedor(int id);
     boolean removerFornecedor(int id);
     List<Fornecedor> obterFornecedores();

     // ------------------- Peca -------------------

     Peca    registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor);
     void    atualizarPeca(int id, String referencia, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa);
     Peca    obterPeca(int id);
     boolean existePeca_id(int id);
     boolean existePeca_ref(String ref);
     boolean removerPeca(int id);
     List<Peca> obterTodasPecas();
     int     obter_quantidade_Stock_Peca_id(int id);
     List<Integer> obter_Pecas_baixo_Stock_minimo();

     // ------------------- Stock -------------------

     Stock   registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data, LocalDate garantia, String nr_serie);
     Stock   registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade);
     void    atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado);
     void    atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado, LocalDate garantia, String nr_serie);
     Stock   obterStock(int id);
     boolean existeStock(int id);
     boolean removerStock(int id);
     List<Stock> obterTodosStocks();
     void    atualizaEstadoStock(int id, EstadoStock estado);
     int     pecasDefeituosas_Stock(int id_peca);
     void    registar_Defeito_entradaStock(int id_Stock);

     // ------------------- Devolucao -------------------

     Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade);
     void      devolverPecas(List<Integer> pecas);
     Devolucao obterDevolucao(int id);
     List<Devolucao> obterTodasDevolucoes();
     boolean   existeDevolucao(int id);
     boolean   removerDevolucao(int id);
     void      atualizarDevolucao(int id, LocalDateTime data_devolucao, String motivo, int id_stock, EstadoDevolucao estado);
     boolean   validaEstadoDevolucao_PendenteDevolucao(int id);
     void      marcarDevolucaoComoEnviada(int id);
     void      marcarDevolucaoComoDevolvida(int id);
     void      marcarDevolucaoComoInvalida(int id);

     // ------------------- Encomenda -------------------

     int       quantidade_encomendar_peca(int id_peca);
     Encomenda criarEncomenda(List<ItemEncomenda> itens, int cod_fornecedor);
     Encomenda obterEncomenda(int id);
     List<Encomenda> obterTodasEncomendas();
     List<Encomenda> obterEncomendasPorEstado(EstadoEncomenda estado);
     void      adicionar_PecasEncomenda_Stock(int idEncomenda, List<ItemEncomenda> itens);
     boolean   removerEncomenda(int id);
     void      marcarEncomendaComoEnviada(int id);
     void      marcarEncomendaComoRecebida(int id);
     boolean   validaEncomenda_Rascunho(int id_Encomenda);
     void      atualizarEncomenda(int id, List<ItemEncomenda> itens, LocalDateTime data_pedido, LocalDateTime data_chegada, EstadoEncomenda estado);
}
