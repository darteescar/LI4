package app.ecoRideLN.sStock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.ecoRideCD.sStock.DevolucaoDAO;
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
          return pecaDAO.getByReference(ref) != null;
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
          Peca peca = pecaDAO.getByReference(referencia);
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

}