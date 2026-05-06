package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideCD.sOrdensServico.OrdemServicoDAO;

public class SClientesFacade implements ISClientes {
     private final ClienteDAO     clientesDAO     = ClienteDAO.getInstance();
     private final TrotineteDAO   trotinetesDAO   = TrotineteDAO.getInstance();
     private final OrdemServicoDAO ordemServicoDAO = OrdemServicoDAO.getInstance();

     public SClientesFacade() {}

     // ------------------- Cliente -------------------

     @Override
     public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
          int id = clientesDAO.generateNewId();
          Cliente cliente = new Cliente(id, nome, email, telemovel, nif);
          clientesDAO.put(id, cliente);
          return cliente;
     }

     @Override
     public Cliente obterCliente(int id) {
          return clientesDAO.get(id);
     }

     @Override
     public boolean existeCliente(int id) {
          return clientesDAO.containsKey(id);
     }

     @Override
     public boolean removerCliente(int id) {
          return clientesDAO.remove(id) != null;
     }

     @Override
     public void atualizarDadosCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               if (novo_nome != null)      cliente.setNome(novo_nome);
               if (novo_email != null)     cliente.setEmail(novo_email);
               if (novo_telemovel != null) cliente.setTelemovel(novo_telemovel);
               if (novo_nif != null)       cliente.setNIF(novo_nif);
               clientesDAO.put(id_cliente, cliente);
          } else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public List<Cliente> obterTodosClientes() {
          return new ArrayList<>(clientesDAO.values());
     }

     // ------------------- Trotinete -------------------

     @Override
     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               int id = trotinetesDAO.generateNewId();
               Trotinete trotinete = new Trotinete(id, modelo, marca, num_serie, tipo_motor, id_cliente);
               trotinetesDAO.put(id, trotinete);
               return trotinete;
          } else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public Trotinete obterTrotinete(int id) {
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
               if (modelo != null)    t.setModelo(modelo);
               if (marca != null)     t.setMarca(marca);
               if (num_serie > 0)     t.setNum_serie(num_serie);
               if (tipo_motor != null) t.setTipo_motor(tipo_motor);
               trotinetesDAO.put(id, t);
          } else {
               throw new EcoRideException("Trotinete com ID " + id + " não encontrada.");
          }
     }

     @Override
     public List<Trotinete> obterTodasTrotinetes() {
          return new ArrayList<>(trotinetesDAO.values());
     }

     // ------------------- Consultas cruzadas -------------------

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
          } else {
               throw new EcoRideException("Cliente com ID " + id + " não encontrado.");
          }
     }
}
