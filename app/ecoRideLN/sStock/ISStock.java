package app.ecoRideLN.sStock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISStock {

    Fornecedor registarFornecedor(String nome, String email, String telemovel);

    Optional<Fornecedor> obterDadosFornecedor(int id);

    boolean existeFornecedor(int id);

    void removerFornecedor(int id);

    void atualizarNomeFornecedor(int id, String nome);

    void atualizarTelemovelFornecedor(int id, String telemovel);

    void atualizarEmailFornecedor(int id, String email);

    Peca registarPeca(String referencia, int stock_minimo, float preco_venda, int idFornecedor);

    Optional<Peca> obterDadosPeca(int id);

    boolean existePecaPorId(int id);

    Optional<Peca> existePecaPorReferencia(String referencia);

    void removerPeca(int id);

    void atualizarReferenciaPeca(int id, String referencia);

    void atualizarStockMinimoPeca(int id, int stock_minimo);

    void atualizarPrecoVendaPeca(int id, float preco_venda);

    void atualizarIdFornecedorPeca(int id, int idFornecedor);

    void atualizarFlagDisponibilidadePeca(int id, boolean novaFlag);

    Stock registarStockPecaNormal(int idPeca, float preco_compra, LocalDateTime data);

    Stock registarStockPecaSuperior70(int idPeca, float preco_compra, LocalDateTime data,
                                       String nr_serie, int garantia);

    Optional<Stock> obterDadosStock(int id);

    boolean existeStock(int id);

    void removerStock(int id);

    void atualizaEstadoStock(int id, EstadoStock estado);

    long obterQuantidadeStockPorPecaId(int idPeca);

    long obterQuantidadeStockPorPecaReferencia(String referencia);

    void registarDefeitoEntradaStock(int idStock);

    long pecasDefeituosasStock(int idPeca);

    void atualizarPrecoCompraStock(int id, float preco_compra);

    void atualizarDataRececaoStock(int id, LocalDateTime data_chegada);

    void atualizarGarantiaStock(int id, int garantia);

    void atualizarNrSerieStock(int id, String nr_serie);

    Devolucao criarDevolucao(String motivo, int idStock);

    void devolverPecas(int idDevolucao, int quantidade);

    Optional<Devolucao> obterDadosDevolucao(int id);

    boolean existeDevolucao(int id);

    void removerDevolucao(int id);

    void atualizarDataDevolucao(int id, LocalDateTime data);

    void atualizarMotivoDevolucao(int id, String motivo);

    void atualizarCodStockDevolucao(int id, int codStock);

    void atualizarEstadoDevolucao(int id, EstadoDevolucao estado);

    boolean validaEstadoDevolucao_PendenteDevolucao(int id);

    List<Peca> obterPecasBaixoStockMinimo();

    int quantidadeAEncomendarPeca(int idPeca);

    Encomenda criarEncomenda(int idFornecedor);

    Optional<Encomenda> obterDadosEncomenda(int id);

    void adicionarPecasEncomendaStock(int idEncomenda, List<QuantidadePeca> pecas, float preco_compra);

    void removerEncomenda(int id);

    void atualizarDataRececaoEncomenda(int id, LocalDateTime data);

    void atualizarDataEnvioEncomenda(int id, LocalDateTime data);

    void atualizarEstadoEncomenda(int id, EstadoEncomenda estado);

    boolean validaEncomendaRascunho(int id);
}
