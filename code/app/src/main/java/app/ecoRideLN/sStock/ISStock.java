package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ISStock {

    // ------------------- Fornecedor -------------------

    public Fornecedor registarFornecedor(String nome, String telemovel, String email);
    public Fornecedor atualizarFornecedor(int id, String nome, String telemovel, String email);
    public Fornecedor obterFornecedor(int id);
    public boolean    existeFornecedor(int id);
    public boolean    removerFornecedor(int id);
    public List<Fornecedor> obterFornecedores();

    // ------------------- Peca -------------------

    public Peca       registarPeca(String ref, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor);
    public Peca       atualizarPeca(int id, String referencia, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa);
    public Peca       obterPeca(int id);
    public boolean    existePeca_id(int id);
    public boolean    existePeca_ref(String ref);
    public boolean    removerPeca(int id);
    public List<Peca> obterPecas();

    public int         obter_quantidade_Stock_Peca_id(int id);

    // ------------------- Stock -------------------

    public Stock       registarStockComGarantia(int id_peca, float preco_compra, LocalDate data, int garantia, String nr_serie);
    public Stock       registarStock_PecaNormal(int id_peca, float preco_compra, LocalDate data, int quantidade);
    public Stock       registarStock_Encomendada(int id_peca, float preco_compra, int quantidade);
    public Stock       atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade);
    public Stock       atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade, int garantia, String nr_serie);
    public Stock       obterStock(int id);
    public boolean     existeStock(int id);
    public boolean     removerStock(int id);
    public List<Stock> obterStocks();

    public Stock atualizaEstadoStock(int id, EstadoStock estado);

    // ------------------- Defeito -------------------

    public List<Defeito> registarDefeito(List<Integer> stockIds, String motivo, int idFuncionario);

    public Defeito       obterDefeito(int id);
    public List<Defeito> obterDefeitos();
    public boolean       removerDefeito(int id);

    public void marcarDefeitoComoPendenteDevolucao(int id);
    public void marcarDefeitoComoResolvido(int id);
    public void marcarDefeitoComoInvalido(int id);

    // ------------------- Devolucao -------------------

    public List<Devolucao> registarDevolucao(List<Integer> stockIds, String motivo, LocalDate data);

    public Devolucao       obterDevolucao(int id);
    public boolean         existeDevolucao(int id);
    public boolean         removerDevolucao(int id);
    public List<Devolucao> obterDevolucoes();

    public void marcarDevolucaoComoEnviada(int id);
    public void marcarDevolucaoComoDevolvida(int id);
    public void marcarDevolucaoComoInvalida(int id);

    // ------------------- Encomenda -------------------

    public Encomenda       registarEncomenda(List<Integer> stockIds, int cod_fornecedor);
    public Encomenda       atualizarEncomenda(int id, List<Integer> stockIds, LocalDate data_pedido, LocalDate data_chegada, EstadoEncomenda estado);
    public Encomenda       obterEncomenda(int id);
    public boolean         removerEncomenda(int id);
    public List<Encomenda> obterEncomendas();

    public Encomenda marcarEncomendaComoEnviada(int id);
    public Encomenda marcarEncomendaComoRecebida(int id);

    public int quantidade_encomendar_peca(int id_peca);
    public Map<Integer, List<Integer>> gerarListaAutomatica();
}
