package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     List<Peca> obterPecas();

     // Utilitários

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
     List<Stock> obterStocks();

     // Utilitários

     void    atualizaEstadoStock(int id, EstadoStock estado);
     int     pecasDefeituosas_Stock(int id_peca);
     void    registar_Defeito_entradaStock(int id_Stock);

     // ------------------- Devolucao -------------------

     Devolucao registarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade);
     void      atualizarDevolucao(int id, LocalDateTime data_devolucao, String motivo, int id_stock, int quantidade);
     void      devolverPecas(List<Integer> pecas);
     Devolucao obterDevolucao(int id);
     boolean   existeDevolucao(int id);
     boolean   removerDevolucao(int id);
     List<Devolucao> obterDevolucoes();

     // Utilitários

     boolean   validaEstadoDevolucao_PendenteDevolucao(int id);
     void      marcarDevolucaoComoEnviada(int id);
     void      marcarDevolucaoComoDevolvida(int id);
     void      marcarDevolucaoComoInvalida(int id);

     // ------------------- Encomenda -------------------

     Encomenda registarEncomenda(List<ItemEncomenda> itens, int cod_fornecedor);
     void      atualizarEncomenda(int id, List<ItemEncomenda> itens, LocalDateTime data_pedido, LocalDateTime data_chegada, EstadoEncomenda estado);
     Encomenda obterEncomenda(int id);
     boolean   removerEncomenda(int id);
     List<Encomenda> obterEncomendas();

     // Utilitários

     void      marcarEncomendaComoEnviada(int id);
     void      marcarEncomendaComoRecebida(int id);
     boolean   validaEncomenda_Rascunho(int id_Encomenda);
     int       quantidade_encomendar_peca(int id_peca);
     public Map<Integer, List<ItemEncomenda>> gerarListaAutomatica();
}
