package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ISStock {

    // ------------------- Fornecedor -------------------

    public Fornecedor registarFornecedor(String nome, String telemovel, String email);
    public Fornecedor atualizarFornecedor(int id, String nome, String telemovel, String email);
    public Fornecedor obterFornecedor(int id);
    public boolean    removerFornecedor(int id);
    public List<Fornecedor> obterFornecedores();
    public List<Peca> obterPecasDoFornecedor(int id_fornecedor);

    // ------------------- Peca -------------------

    public Peca       registarPeca(String ref, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, int garantia);
    public Peca       atualizarPeca(int id, String referencia, String marca, String nome, String descricao, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa, int garantia);
    public Peca       obterPeca(int id);
    public boolean    removerPeca(int id);
    public List<Peca> obterPecas();
    public List<Peca> obterPecasAtivas();

    public int         obter_quantidade_Stock_Peca_id(int id);

    // ------------------- Stock -------------------

    public Stock       registarStock(int id_peca, float preco_compra, LocalDate data, int quantidade);
    public Stock       atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDate data_rececao, int quantidade);
    public Stock       obterStock(int id);
    public boolean     removerStock(int id);
    public List<Stock> obterStocks();

    public Stock atualizaEstadoStock(int id, EstadoStock estado);

    public Map<Integer, Integer> atribuirStocksFIFO(int codPeca, int quantidade);

    // ------------------- Defeito -------------------

    public List<Defeito> registarDefeito(int codPeca, String motivo, int idFuncionario);

    public List<Defeito> obterDefeitos();
    public boolean       removerDefeito(int id);

    public Devolucao confirmarDefeitoComDevolucao(int idDefeito, String motivo, LocalDate data);
    public void      descartarDefeito(int idDefeito);

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

    public Encomenda       registarEncomenda(List<Integer> id_peca, List<Float> preco_compra, List<Integer> quantidade, int cod_fornecedor);
    public boolean         removerEncomenda(int id);
    public List<Encomenda> obterEncomendas();

    public Encomenda marcarEncomendaComoEnviada(int id);
    public Encomenda marcarEncomendaComoRecebida(int id);

    public Map<Integer, Map<Integer, Integer>> gerarListaAutomatica();
}
