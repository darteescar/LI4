package app.ecoRideLN.sClientes;

import java.util.List;

public interface ISClientes {

     // ------------------- Cliente -------------------

     Cliente registarCliente(String nome, String email, String telemovel, String nif);

     void atualizarCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);

     Cliente obterCliente(int id);

     boolean existeCliente(int id);

     boolean removerCliente(int id);

     List<Cliente> obterClientes();

     // ------------------- Trotinete -------------------

     Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);

     void atualizarTrotinete(int id, String modelo, String marca, int num_serie, String tipo_motor);

     Trotinete obterTrotinete(int id);

     boolean existeTrotinete(int id);

     boolean removerTrotinete(int id);

     List<Trotinete> obterTrotinetes();

     // Utilitários

     List<Trotinete> obterTrotinetes_Cliente(int id);

}
