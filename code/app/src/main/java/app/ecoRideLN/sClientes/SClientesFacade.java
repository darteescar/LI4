package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;

public class SClientesFacade implements ISClientes {
     private final ClienteDAO     clientesDAO;
     private final TrotineteDAO   trotinetesDAO;

     public SClientesFacade() {
          this.clientesDAO   = ClienteDAO.getInstance();
          this.trotinetesDAO = TrotineteDAO.getInstance();
     }

     public SClientesFacade(ClienteDAO clientesDAO, TrotineteDAO trotinetesDAO) {
          this.clientesDAO   = clientesDAO;
          this.trotinetesDAO = trotinetesDAO;
     }

     // ------------------- Cliente -------------------

     @Override
     public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
          validaDadosCliente(nome, email, telemovel, nif);
          Cliente cliente = new Cliente(0, nome, email, telemovel, nif);
          clientesDAO.insert(cliente);
          return cliente;
     }

     @Override
     public Cliente atualizarCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               validaDadosCliente(novo_nome, novo_email, novo_telemovel, novo_nif);
               cliente.setNome(novo_nome);
               cliente.setEmail(novo_email);
               cliente.setTelemovel(novo_telemovel);
               cliente.setNIF(novo_nif);
               clientesDAO.put(id_cliente, cliente);
               return cliente;
          } else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
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
     public List<Cliente> obterClientes() {
          return new ArrayList<>(clientesDAO.values());
     }

     // ------------------- Trotinete -------------------

     @Override
     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, String num_serie, String tipo_motor) {
          validaDadosTrotinete(modelo, marca, num_serie, tipo_motor);
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               Trotinete trotinete = new Trotinete(0, modelo, marca, num_serie, tipo_motor, id_cliente);
               trotinetesDAO.insert(trotinete);
               return trotinete;
          } else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public Trotinete atualizarTrotinete(int id, int id_cliente, String modelo, String marca, String num_serie, String tipo_motor) {
          Trotinete t = trotinetesDAO.get(id);
          if (t != null) {
               validaDadosTrotinete(modelo, marca, num_serie, tipo_motor);
               t.setCod_cliente(id_cliente);
               if (modelo != null)    t.setModelo(modelo);
               if (marca != null)     t.setMarca(marca);
               if (num_serie != null && !num_serie.isBlank()) t.setNum_serie(num_serie);
               if (tipo_motor != null) t.setTipo_motor(tipo_motor);
               trotinetesDAO.put(id, t);
               return t;
          } else {
               throw new EcoRideException("Trotinete com ID " + id + " não encontrada.");
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
     public List<Trotinete> obterTrotinetes() {
          return new ArrayList<>(trotinetesDAO.values());
     }

     // Utilitários

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

     // Utilitários

     private void validaDadosCliente(String nome, String email, String telemovel, String nif) {
          Validacoes.naoVazio(nome, "Nome");
          Validacoes.emailValido(email);
          Validacoes.telemovel(telemovel);
          Validacoes.nif(nif);
     }

     private void validaDadosTrotinete(String modelo, String marca, String num_serie, String tipo_motor) {
          Validacoes.naoVazio(modelo, "Modelo");
          Validacoes.naoVazio(marca, "Marca");
          Validacoes.naoVazio(num_serie, "Número de série");
          Validacoes.naoVazio(tipo_motor, "Tipo de motor");
     }
}
