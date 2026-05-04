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
     public void atualizarFornecedor(int id, String nome, String telemovel, String email){
          Fornecedor fornecedor = fornecedorDAO.get(id);
          if (fornecedor != null) {
               if (nome != null && !nome.isEmpty()) {
                    fornecedor.setNome(nome);
               }
               if (telemovel != null && !telemovel.isEmpty()) {
                    fornecedor.setTelemovel(telemovel);
               }
               if (email != null && !email.isEmpty()) {
                    fornecedor.setEmail(email);
               }
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
     public void atualizarPeca(int id, String referencia, int stock_minimo, float preco_venda, int id_fornecedor, boolean ativa){
          Peca peca = pecaDAO.get(id);
          if (peca != null) {
               if (referencia != null && !referencia.isEmpty()) {
                    peca.setReferencia(referencia);
               }
               if (stock_minimo >= 0) {
                    peca.setStock_minimo(stock_minimo);
               }
               if (preco_venda >= 0) {
                    peca.setPreco_venda(preco_venda);
               }
               if (id_fornecedor >= 0) {
                    peca.setCodFornecedor(id_fornecedor);
               }
               peca.setAtiva(ativa);
               pecaDAO.put(id, peca);
          }
     }

     @Override
     public int obter_quantidade_Stock_Peca_id(int id){
          List<Stock> stocks = stockDAO.getByPecaId(id);
          int quantidadeTotal = 0;
          for (Stock s : stocks) {
               quantidadeTotal += s.getQuantidade();
          }
          return quantidadeTotal;
     }

     @Override
     public int obter_quantidade_Stock_Peca_ref(String referencia){
          Peca peca = pecaDAO.getByReferenceFull(referencia);
          if (peca != null){
               List<Stock> stocks = stockDAO.getByPecaId(peca.getId());
               if (stocks == null || stocks.isEmpty()) {
                    return 0;
               }
               int quantidadeTotal = 0;
               for (Stock s : stocks) {
                    quantidadeTotal += s.getQuantidade();
               }
               return quantidadeTotal;
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
     public Stock   registarStockComGarantia(int id_peca, float preco_compra, LocalDateTime data,
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
     public void atualizarStock(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null) {
               if (preco_compra >= 0) {
                    stock.setPreco_compra(preco_compra);
               }
               if (cod_Peca >= 0) {
                    stock.setCodPeca(cod_Peca);
               }
               if (data_rececao != null) {
                    stock.setData_chegada(data_rececao);
               }
               if (quantidade >= 0) {
                    stock.setQuantidade(quantidade);
               }
               if (estado != null) {
                    stock.setEstado(estado);
               }
               stockDAO.put(id_stock, stock);
          }
     }

     @Override
     public void atualizarStockComGarantia(int id_stock, float preco_compra, int cod_Peca, LocalDateTime data_rececao, int quantidade, EstadoStock estado, LocalDate garantia, String nr_serie){
          Stock stock = stockDAO.get(id_stock);
          if (stock != null && stock instanceof StockComGarantia) {
               StockComGarantia stockGarantia = (StockComGarantia) stock;
               if (preco_compra >= 0) {
                    stockGarantia.setPreco_compra(preco_compra);
               }
               if (cod_Peca >= 0) {
                    stockGarantia.setCodPeca(cod_Peca);
               }
               if (data_rececao != null) {
                    stockGarantia.setData_chegada(data_rececao);
               }
               if (quantidade >= 0) {
                    stockGarantia.setQuantidade(quantidade);
               }
               if (estado != null) {
                    stockGarantia.setEstado(estado);
               }
               if (garantia != null) {
                    stockGarantia.setGarantia(garantia);
               }
               if (nr_serie != null && !nr_serie.isEmpty()) {
                    stockGarantia.setNr_serie(nr_serie);
               }
               stockDAO.put(id_stock, stockGarantia);
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
     public void atualizarDevolucao(int id, LocalDateTime data_devolucao, String motivo, int id_stock, EstadoDevolucao estado){
          Devolucao devolucao = devolucaoDAO.get(id);
          if (devolucao != null) {
               if (data_devolucao != null) {
                    devolucao.setData(data_devolucao);
               }
               if (motivo != null && !motivo.isEmpty()) {
                    devolucao.setMotivo(motivo);
               }
               if (id_stock >= 0) {
                    devolucao.setCodStock(id_stock);
               }
               if (estado != null) {
                    devolucao.setEstado(estado);
               }
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
     public Encomenda criarEncomenda(List<Stock> pecas, int cod_fornecedor){
          int id = encomendaDAO.generateNewId();
          Encomenda encomenda = new Encomenda(id, cod_fornecedor, pecas.stream().map(Stock::getId).toList());
          encomendaDAO.put(id, encomenda);
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
     public void      atualizarEncomenda(int id, List<Stock> pecas, LocalDateTime data_pedido, LocalDateTime data_chegada, EstadoEncomenda estado){
          Encomenda encomenda = encomendaDAO.get(id);
          if (encomenda != null) {
               if (pecas != null && !pecas.isEmpty()) {
                    List<Integer> codPecasEncomenda = encomenda.getCodEntradasStock();
                    codPecasEncomenda.clear();
                    codPecasEncomenda.addAll(pecas.stream().map(Stock::getId).toList());
               }
               if (data_pedido != null) {
                    encomenda.setData_criacao(data_pedido);
               }
               if (data_chegada != null) {
                    encomenda.setData_rececao(data_chegada);
               }
               if (estado != null) {
                    encomenda.setEstado(estado);
               }
               encomendaDAO.put(id, encomenda);
          }
     }

}