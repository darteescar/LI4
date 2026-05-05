package app.ecoRideLN.sClientes;

import java.util.List;

import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public interface ISClientes {

     // ------------------- Cliente -------------------

     Cliente registarCliente(String nome, String email, String telemovel, String nif);

     Cliente obterCliente(int id);

     boolean existeCliente(int id);

     boolean removerCliente(int id);

     void atualizarDadosCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);

     List<Cliente> obterTodosClientes();

     // ------------------- Trotinete -------------------

     Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);

     Trotinete obterTrotinete(int id);

     boolean existeTrotinete(int id);

     boolean removerTrotinete(int id);

     void atualizarDadosTrotinete(int id, String modelo, String marca, int num_serie, String tipo_motor);

     // ------------------- Consultas cruzadas -------------------

     List<OrdemServico> obterOSs_Cliente(int id);

     List<Trotinete> obterTrotinetes_Cliente(int id);

     List<OrdemServico> obterOS_Trotinete(int id_trotinete);

     List<Conserto> obterConsertosAnteriores(int id_trotinete);
}
