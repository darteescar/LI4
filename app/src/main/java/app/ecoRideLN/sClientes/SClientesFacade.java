package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

import app.common.EcoRideException;

public class SClientesFacade implements ISClientes {
     private ClientesDAO clientesDAO;
     private TrotinetesDAO trotinetesDAO;

     public SClientesFacade() {
          this.clientesDAO = ClientesDAO.getInstance();
          this.trotinetesDAO = TrotinetesDAO.getInstance();
     }

     @Override
     public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
          Cliente cliente = new Cliente(clientesDAO.generateNewId(), nome, email, telemovel, nif);
          clientesDAO.add(cliente);
          return cliente;
     }

     @Override
     public Cliente obterDadosCliente(int id) {
          return clientesDAO.getById(id);
     }

     @Override
     public List<OrdemServico> obterOS_Cliente(int id){
          Cliente cliente = clientesDAO.getById(id);
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
          return clientesDAO.remove(id);
     }

     @Override
     public List<Trotinete> obterTrotinetes_Cliente(int id) {
          Cliente cliente = clientesDAO.getById(id);
          if (cliente != null) {
               return cliente.getTrotinetes();
          }
          else {
               throw new EcoRideException("Cliente com ID " + id + " não encontrado.");
          }
     }

     @Override
     public boolean existeCliente(int id) {
          return clientesDAO.exists(id);
     }

     @Override
     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor) {
          Cliente cliente = clientesDAO.getById(id_cliente);
          if (cliente != null) {
               Trotinete trotinete = new Trotinete(trotinetesDAO.generateNewId(), modelo, marca, num_serie, tipo_motor);
               if (cliente.addTrotinete(trotinete.getId())) {
                    clientesDAO.update(cliente);
                    trotinetesDAO.add(trotinete);
                    return trotinete;
               }
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public List<OrdemServico> obterOS_Trotinete(int id_trotinete){
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               return trotinete.getOSs();
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public Trotinete obterDadosTrotinete(int id){
          return trotinetesDAO.getById(id);
     }

     @Override
     public boolean removerTrotinete(int id) {
          Trotinete trotinete = trotinetesDAO.getById(id);
          if (trotinete != null) {
               Cliente cliente = clientesDAO.getById(trotinete.getCod_cliente());
               if (cliente != null) {
                    if (cliente.removeTrotinete(id)) {
                         clientesDAO.update(cliente);
                    }
               }
               return trotinetesDAO.remove(id);
          }
          return false;
     }

     @Override
     public List<Conserto> obterConsertosAnteriores(int id_trotinete) {
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               return trotinete.getConsertos();
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarNomeCliente(int id_cliente, String novo_nome) {
          Cliente cliente = clientesDAO.getById(id_cliente);
          if (cliente != null) {
               cliente.setNome(novo_nome);
               clientesDAO.update(cliente);}
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarEmailCliente(int id_cliente, String novo_email) {
          Cliente cliente = clientesDAO.getById(id_cliente);
          if (cliente != null) {
               cliente.setEmail(novo_email);
               clientesDAO.update(cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarTelemovelCliente(int id_cliente, String novo_telemovel) {
          Cliente cliente = clientesDAO.getById(id_cliente);
          if (cliente != null) {
               cliente.setTelemovel(novo_telemovel);
               clientesDAO.update(cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarNIFCliente(int id_cliente, String novo_nif) {
          Cliente cliente = clientesDAO.getById(id_cliente);
          if (cliente != null) {
               cliente.setNIF(novo_nif);
               clientesDAO.update(cliente);
          }
          else {
               throw new EcoRideException("Cliente com ID " + id_cliente + " não encontrado.");
          }
     }

     @Override
     public void atualizarModeloTrotinete(int id_trotinete, String novo_modelo){
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               trotinete.setModelo(novo_modelo);
               trotinetesDAO.update(trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarMarcaTrotinete(int id_trotinete, String nova_marca){
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               trotinete.setMarca(nova_marca);
               trotinetesDAO.update(trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarNumeroSerieTrotinete(int id_trotinete, int novo_num_serie){
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               trotinete.setNum_serie(novo_num_serie);
               trotinetesDAO.update(trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

     @Override
     public void atualizarTipoMotorTrotinete(int id_trotinete, String novo_tipo_motor){
          Trotinete trotinete = trotinetesDAO.getById(id_trotinete);
          if (trotinete != null) {
               trotinete.setTipo_motor(novo_tipo_motor);
               trotinetesDAO.update(trotinete);
          }
          else {
               throw new EcoRideException("Trotinete com ID " + id_trotinete + " não encontrada.");
          }
     }

}