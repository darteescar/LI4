package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public class SClientesFacade implements ISClientes {
     private ClienteDAO clientesDAO = ClienteDAO.getInstance();
     private TrotineteDAO trotinetesDAO = TrotineteDAO.getInstance();
     private OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();

     public SClientesFacade() {
     }

     @Override
     public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
          int id = clientesDAO.generateNewId();
          Cliente cliente = new Cliente(id, nome, email, telemovel, nif);
          clientesDAO.put(id ,cliente);
          return cliente;
     }

     @Override
     public Cliente obterDadosCliente(int id) {
          return clientesDAO.get(id);
     }

     @Override
     public List<OrdemServico> obterOS_Cliente(int id){
          Cliente cliente = clientesDAO.get(id);
          if (cliente != null) {
               List<OrdemServico> ordensServico = new ArrayList<>();
               for (Integer codTrotinete : cliente.getCodsTrotinetes()) {
                    Trotinete trotinete = trotinetesDAO.get(codTrotinete);
                    if (trotinete != null) {
                         for (Integer codOS : trotinete.getCodsOrdensServico()) {
                              OrdemServico os = ordemServicoDAO.get(codOS);
                              if (os != null) ordensServico.add(os);
                         }
                    }
               }
               return ordensServico;
          }
          else {
               throw new EcoRideException("Cliente com ID " + id + " não encontrado.");
          }
     }

     @Override
     public boolean removerCliente(int id) {
          return clientesDAO.remove(id) != null;
     }

     @Override
     public List<Trotinete> obterTrotinetes_Cliente(int id) {
          Cliente cliente = clientesDAO.get(id);
          if (cliente != null) {
               List<Trotinete> trotinetes = new ArrayList<>();
               for (Integer cod : cliente.getCodsTrotinetes()) {
                    Trotinete t = trotinetesDAO.get(cod);
                    if (t != null) trotinetes.add(t);
               }
               return trotinetes;
          }
          else {
               throw new EcoRideException("Cliente com ID " + id + " não encontrado.");
          }
     }

     @Override
     public boolean existeCliente(int id) {
          return clientesDAO.containsKey(id);
     }

     @Override
     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               int id = trotinetesDAO.generateNewId();
               Trotinete trotinete = new Trotinete(id, modelo, marca, num_serie, tipo_motor, id_cliente);
               trotinetesDAO.put(id, trotinete);
               return trotinete;
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public List<OrdemServico> obterOS_Trotinete(int id_trotinete){
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               List<OrdemServico> ordensServico = new ArrayList<>();
               for (Integer codOS : trotinete.getCodsOrdensServico()) {
                    OrdemServico os = ordemServicoDAO.get(codOS);
                    if (os != null) ordensServico.add(os);
               }
               return ordensServico;
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public Trotinete obterDadosTrotinete(int id){
          return trotinetesDAO.get(id);
     }

     @Override
     public boolean existeTrotinete(int id) {
          return trotinetesDAO.containsKey(id);
     }

     @Override
     public boolean removerTrotinete(int id) {
          return trotinetesDAO.remove(id) != null;
     }

     @Override
     public void atualizarDadosTrotinete(int id, String modelo, String marca, int num_serie, String tipo_motor) {
          Trotinete t = trotinetesDAO.get(id);
          if (t != null) {
               if (modelo != null) t.setModelo(modelo);
               if (marca != null) t.setMarca(marca);
               if (num_serie > 0) t.setNum_serie(num_serie);
               if (tipo_motor != null) t.setTipo_motor(tipo_motor);
               trotinetesDAO.put(id, t);
          }
     }

     @Override
     public List<Conserto> obterConsertosAnteriores(int id_trotinete) {
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               List<Conserto> consertos = new ArrayList<>();
               for (Integer codOS : trotinete.getCodsOrdensServico()) {
                    OrdemServico os = ordemServicoDAO.get(codOS);
                    if (os != null && os.getConserto() != null) {
                         consertos.add(os.getConserto());
                    }
               }
               return consertos;
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override 
     public void atualizarDadosCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               if (novo_nome != null){
                    cliente.setNome(novo_nome);
               }
               if (novo_email != null){
                    cliente.setEmail(novo_email);
               }
               if (novo_telemovel != null){
                    cliente.setTelemovel(novo_telemovel);
               }
               if (novo_nif != null){
                    cliente.setNIF(novo_nif);
               } 
               clientesDAO.put(id_cliente, cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

}