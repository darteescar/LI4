package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public class SClientesFacade implements ISClientes {
     private ClienteDAO clientesDAO = ClienteDAO.getInstance();
     private TrotineteDAO trotinetesDAO = TrotineteDAO.getInstance();

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
               List<Trotinete> trotinetes = cliente.getTrotinetes();
               List<OrdemServico> ordensServico = new ArrayList<>();
               for (Trotinete trotinete : trotinetes) {
                    ordensServico.addAll(trotinete.getOSs());
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
               return cliente.getTrotinetes();
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
               return trotinete.getOSs();
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
     public boolean removerTrotinete(int id) {
          Trotinete trotinete = trotinetesDAO.get(id);
          if (trotinete != null) {
               return (trotinetesDAO.remove(id, trotinete));
          }
          return false;
     }

     @Override
     public List<Conserto> obterConsertosAnteriores(int id_trotinete) {
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               return trotinete.getConsertos();
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarNomeCliente(int id_cliente, String novo_nome) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               cliente.setNome(novo_nome);
               clientesDAO.put(id_cliente, cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarEmailCliente(int id_cliente, String novo_email) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               cliente.setEmail(novo_email);
               clientesDAO.put(id_cliente, cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarTelemovelCliente(int id_cliente, String novo_telemovel) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               cliente.setTelemovel(novo_telemovel);
               clientesDAO.put(id_cliente, cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarNIFCliente(int id_cliente, String novo_nif) {
          Cliente cliente = clientesDAO.get(id_cliente);
          if (cliente != null) {
               cliente.setNIF(novo_nif);
               clientesDAO.put(id_cliente, cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarModeloTrotinete(int id_trotinete, String novo_modelo){
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               trotinete.setModelo(novo_modelo);
               trotinetesDAO.put(id_trotinete, trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarMarcaTrotinete(int id_trotinete, String nova_marca){
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               trotinete.setMarca(nova_marca);
               trotinetesDAO.put(id_trotinete, trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarNumeroSerieTrotinete(int id_trotinete, int novo_num_serie){
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               trotinete.setNum_serie(novo_num_serie);
               trotinetesDAO.put(id_trotinete, trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarTipoMotorTrotinete(int id_trotinete, String novo_tipo_motor){
          Trotinete trotinete = trotinetesDAO.get(id_trotinete);
          if (trotinete != null) {
               trotinete.setTipo_motor(novo_tipo_motor);
               trotinetesDAO.put(id_trotinete, trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

}