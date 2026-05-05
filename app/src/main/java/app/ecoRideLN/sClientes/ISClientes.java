package app.ecoRideLN.sClientes;

import java.util.List;

import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public interface ISClientes {

     Cliente registarCliente(String nome, String email, String telemovel, String nif);

     Cliente obterDadosCliente(int id);

     boolean removerCliente(int id);

     boolean existeCliente(int id);

     void atualizarDadosCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);

     List<OrdemServico> obterOS_Cliente(int id);

     List<Trotinete> obterTrotinetes_Cliente(int id);

     Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);

     Trotinete obterDadosTrotinete(int id);

     boolean existeTrotinete(int id);

     boolean removerTrotinete(int id);

     void atualizarDadosTrotinete(int id, String modelo, String marca, int num_serie, String tipo_motor);

     List<OrdemServico> obterOS_Trotinete(int id_trotinete);

     List<Conserto> obterConsertosAnteriores(int id_trotinete);
}
