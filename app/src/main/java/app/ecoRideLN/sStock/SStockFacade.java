package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sStock.DevolucaoDAO;
import app.ecoRideCD.sStock.EncomendaDAO;
import app.ecoRideCD.sStock.FornecedorDAO;
import app.ecoRideCD.sStock.PecaDAO;
import app.ecoRideCD.sStock.StockDAO;

public class SStockFacade implements ISStock {
     private final FornecedorDAO fornecedorDAO = FornecedorDAO.getInstance();
     private final EncomendaDAO encomendaDAO = EncomendaDAO.getInstance();
     private final PecaDAO pecaDAO = PecaDAO.getInstance();
     private final DevolucaoDAO devolucaoDAO = DevolucaoDAO.getInstance();
     private final StockDAO stockDAO = StockDAO.getInstance();

     @Override
     public Fornecedor registarFornecedor(String nome, String telemovel, String email){
          int id = fornecedorDAO.generateNewId();
          return fornecedorDAO.put(id, new Fornecedor(id, nome, telemovel, email));
     }

     @Override
     public Fornecedor obterDadosFornecedor(int id){
          return fornecedorDAO.get(id);
     }

     @Override
     public boolean existeFornecedor(int id){
          return fornecedorDAO.containsKey(id);
     }

     @Override
     public boolean removerFornecedor(int id){
          return fornecedorDAO.remove(id) != null;
     }

     @Override
     public void atualizarNomeFornecedor(int id, String nome){
          Fornecedor fornecedor = fornecedorDAO.get(id);
          if (fornecedor != null) {
               fornecedor.setNome(nome);
               fornecedorDAO.put(id, fornecedor);
          }
     }

     @Override
     public void atualizarTelemovelFornecedor(int id, String telemovel){
          Fornecedor fornecedor = fornecedorDAO.get(id);
          if (fornecedor != null) {
               fornecedor.setTelemovel(telemovel);
               fornecedorDAO.put(id, fornecedor);
          }
     }

     @Override
     public void atualizarEmailFornecedor(int id, String email){
          Fornecedor fornecedor = fornecedorDAO.get(id);
          if (fornecedor != null) {
               fornecedor.setEmail(email);
               fornecedorDAO.put(id, fornecedor);
          }
     }

     @Override
     public Peca registarPeca(String ref, int stock_minimo, float preco_venda, int id_fornecedor){
          int id = pecaDAO.generateNewId();
          return pecaDAO.put(id, new Peca(id, ref, stock_minimo, preco_venda, id_fornecedor, true));
     }

     @Override
     public Peca obterDadosPeca(int id){
          return pecaDAO.get(id);
     }

     @Override
     public boolean existePeca_id(int id){
          return pecaDAO.containsKey(id);
     }

     @Override
     public boolean existePeca_ref(String ref){
          return pecaDAO.getByReference(ref);
     }

     @Override
     public boolean removerPeca(int id){
          return pecaDAO.remove(id) != null;
     }

     @Override
     public void atualizarReferenciaPeca(int id, String referencia){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               peca.setReferencia(referencia);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public void atualizarStockMinimoPeca(int id, int stock_minimo){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               peca.setStock_minimo(stock_minimo);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public void atualizarPrecoVendaPeca(int id, float preco_venda){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               peca.setPreco_venda(preco_venda);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public void atualizarIdFornecedorPeca(int id, int id_fornecedor){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               peca.setCodFornecedor(id_fornecedor);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public void atualizarFlagDisponibilidadePeca(int id, boolean novaFlag){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               peca.setAtiva(novaFlag);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public int obter_quantidade_Stock_Peca_id(int id){
          List<Stock> stocks = stockDAO.getByPecaId(id);
          return stocks.size();
     }

     @Override
     public int obter_quantidade_Stock_Peca_ref(String referencia){
          Peca peca = pecaDAO.getByReferenceFull(referencia);
          if (peca != null){
               List<Stock> stocks = stockDAO.getByPecaId(peca.getId());
               return stocks.size();
          }
          return 0;
     }

     @Override
     public List<Integer> obter_Pecas_baixo_Stock_minimo(){
          List<Peca> pecas_no_sistema = pecaDAO.values().stream().toList();
          List<Integer> pecas_baixo_stock = new ArrayList<>();
          for (Peca p : pecas_no_sistema) {
               int quantidade_stock = obter_quantidade_Stock_Peca_id(p.getId());
               if (quantidade_stock < p.getStock_minimo()) {
                    pecas_baixo_stock.add(p.getId());
               }
          }
          return pecas_baixo_stock;
     }

     @Override
     public Stock   registarStock_PecaSuperior70(int id_peca, float preco_compra, LocalDateTime data,
                                         LocalDate garantia, String nr_serie){
          int id = stockDAO.generateNewId();
          return stockDAO.put(id, new StockComGarantia(id, preco_compra, id_peca, data, nr_serie, garantia));
     }

     @Override
     public Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data, int quantidade){
          int id = stockDAO.generateNewId();
          return stockDAO.put(id, new Stock(id, preco_compra, id_peca, data, quantidade));
     }

     @Override
     public Stock obterDadosStock(int id){
          return stockDAO.get(id);
     }

     @Override
     public boolean existeStock(int id){
          return stockDAO.containsKey(id);
     }

     @Override
     public boolean removerStock(int id){
          return stockDAO.remove(id) != null;
     }

     @Override
     public void atualizaEstadoStock(int id, EstadoStock estado){
          Stock stock = stockDAO.get(id);
          if (stock != null) {
               stock.setEstado(estado);
               stockDAO.put(id, stock);
          }
     }

     @Override
     public int pecasDefeituosas_Stock(int id_peca){
          List<Stock> stocks = stockDAO.getByPecaId(id_peca);
          int defeituosos = 0;
          for (Stock s : stocks) {
               if (s.getEstado() == EstadoStock.PossivelDefeito) {
                    defeituosos++;
               }
          }
          return defeituosos;
     }

     @Override
     public void registar_Defeito_entradaStock(int id_Stock){
          Stock stock = stockDAO.get(id_Stock);
          if (stock != null) {
               stock.setEstado(EstadoStock.PossivelDefeito);
               stockDAO.put(id_Stock, stock);
          }
     }

     @Override
     public void atualizarIdPecaStock(int id_stock, int id_peca){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null) {
               stock.setCodPeca(id_peca);
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public void atualizarPrecoCompraStock(int id_stock, float preco_compra){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null) {
               stock.setPreco_compra(preco_compra);
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public void atualizarDataRececaoStock(int id_stock, LocalDateTime data_rececao){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null) {
               stock.setData_chegada(data_rececao);
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public void atualizarGarantiaStock(int id_stock, LocalDate garantia){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null && stock instanceof StockComGarantia) {
               ((StockComGarantia) stock).setGarantia(garantia);
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public void atualizarNrSerieStock(int id_stock, String nr_serie){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null && stock instanceof StockComGarantia) {
               ((StockComGarantia) stock).setNr_serie(nr_serie);
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock){
          int id = devolucaoDAO.generateNewId();
          return devolucaoDAO.put(id, new Devolucao(id, data_devolucao, motivo, id_stock));
     }

      @Override
     public void devolverPecas(List<Integer> pecas){
          for (Integer id_stock : pecas) {
               Stock stock = stockDAO.get(id_stock);
               if (stock != null) {
                    stock.setEstado(EstadoStock.PendenteDevolucao);
                    stockDAO.put(id_stock, stock);
               }
          }
      }

      @Override
      public Devolucao obterDadosDevolucao(int id){
          return devolucaoDAO.get(id);
      }

      @Override
      public boolean existeDevolucao(int id){
          return devolucaoDAO.containsKey(id);
      }

      @Override
      public boolean removerDevolucao(int id){
          return devolucaoDAO.remove(id) != null;
      }

      @Override
      public void atualizarDataDevolucao(int id, LocalDateTime data){
          Devolucao devolucao = devolucaoDAO.get(id);
          if (devolucao != null) {
               devolucao.setData(data);
               devolucaoDAO.put(id, devolucao);
          }
     }
     
     @Override
     public void atualizarMotivoDevolucao(int id, String motivo){
          Devolucao devolucao = devolucaoDAO.get(id);
          if (devolucao != null) {
               devolucao.setMotivo(motivo);
               devolucaoDAO.put(id, devolucao);
          }
     }

     @Override
     public void atualizarCodStockDevolucao(int id, int codStock){
          Devolucao devolucao = devolucaoDAO.get(id);
          if (devolucao != null) {
               devolucao.setCodStock(codStock);
               devolucaoDAO.put(id, devolucao);
          }
     }

     @Override
     public void atualizarEstadoDevolucao(int id, EstadoDevolucao estado){
          Devolucao devolucao = devolucaoDAO.get(id);
          if (devolucao != null) {
               devolucao.setEstado(estado);
               devolucaoDAO.put(id, devolucao);
          }
     }

     @Override
     public boolean validaEstadoDevolucao_PendenteDevolucao(int id){
          Devolucao devolucao = devolucaoDAO.get(id);
          return devolucao != null && devolucao.getEstado() == EstadoDevolucao.PendenteDevolucao;
     }

     @Override
     public int quantidade_encomendar_peca(int id_peca){
          Peca peca = pecaDAO.get(id_peca);
          if (peca != null) {
               int stockAtual = obter_quantidade_Stock_Peca_id(id_peca);
               int stockMinimo = peca.getStock_minimo();
               return Math.max(0, stockMinimo - stockAtual);
          }
          return 0;
     }

     @Override
     public Encomenda criarEncomenda(List<Stock> pecas){
          int codFornecedor = pecas.isEmpty() ? 0 : pecaDAO.get(pecas.get(0).getCodPeca()).getCodFornecedor();
          int id = encomendaDAO.generateNewId();
          Encomenda encomenda = new Encomenda(id, codFornecedor, pecas.stream().map(Stock::getId).toList());
          encomendaDAO.put(id, encomenda);
          adicionar_PecasEncomenda_Stock(id, pecas);
          return encomenda;
     }

     @Override
     public Encomenda obterDadosEncomenda(int id){
          return encomendaDAO.get(id);
     }

     @Override
     public void adicionar_PecasEncomenda_Stock(int idEncomenda, List<Stock> pecas){
          Encomenda encomenda = encomendaDAO.get(idEncomenda);
          if (encomenda != null) {
               List<Integer> codPecasEncomenda = encomenda.getCodEntradasStock();
               codPecasEncomenda.addAll(pecas.stream().map(Stock::getId).toList());
               encomendaDAO.put(idEncomenda, encomenda);
          }
     }
     
     @Override
     public boolean removerEncomenda(int id){
          return encomendaDAO.remove(id) != null;
     }

     @Override
     public boolean   validaEncomenda_Rascunho(int id_Encomenda){
          Encomenda encomenda = encomendaDAO.get(id_Encomenda);
          return encomenda != null && encomenda.getEstado() == EstadoEncomenda.RASCUNHO;
     }

     @Override
     public void      atualizarPecasEncomenda(int id, List<Stock> pecas){
          
     }

     @Override
     public void      atualizarDataRececaoEncomenda(int id, LocalDateTime data_chegada){
          
     }

     @Override
     public void      atualizarDataEnvioEncomenda(int id, LocalDateTime data_pedido){
          
     }

     @Override
     public void      atualizarEstadoEncomenda(int id, EstadoEncomenda estado){

     }

}