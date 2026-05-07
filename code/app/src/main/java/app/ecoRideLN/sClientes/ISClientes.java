package app.ecoRideLN.sClientes;

import java.util.List;

public interface ISClientes {

     // ------------------- Cliente -------------------

     public Cliente registarCliente(String nome, String email, String telemovel, String nif);

     public Cliente atualizarCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);

     public Cliente obterCliente(int id);

     public boolean existeCliente(int id);

     public boolean removerCliente(int id);

     public List<Cliente> obterClientes();

     // ------------------- Trotinete -------------------

     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, String num_serie, String tipo_motor);

     public Trotinete atualizarTrotinete(int id, int id_cliente, String modelo, String marca, String num_serie, String tipo_motor);

     public Trotinete obterTrotinete(int id);

     public boolean existeTrotinete(int id);

     public boolean removerTrotinete(int id);

     public List<Trotinete> obterTrotinetes();

     // Utilitários

     public List<Trotinete> obterTrotinetes_Cliente(int id);

}
