package app.ecoRideLN.sClientes;

import java.util.List;

import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public interface ISClientes {
     Cliente registarCliente(String nome, String email, String telemovel, String nif);

     Cliente obterDadosCliente(int id);

     List<OrdemServico> obterOS_Cliente(int id);

     boolean removerCliente(int id);

     List<Trotinete> obterTrotinetes_Cliente(int id);

     boolean existeCliente(int id);

     Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);

     List<OrdemServico> obterOS_Trotinete(int id_trotinete);

     Trotinete obterDadosTrotinete(int id);

     boolean removerTrotinete(int id);

     List<Conserto> obterConsertosAnteriores(int id_trotinete);

     void atualizarNomeCliente(int id_cliente, String novo_nome);

     void atualizarEmailCliente(int id_cliente, String novo_email);

     void atualizarTelemovelCliente(int id_cliente, String novo_telemovel);

     void atualizarNIFCliente(int id_cliente, String novo_nif);

     void atualizarModeloTrotinete(int id_trotinete, String novo_modelo);

     void atualizarMarcaTrotinete(int id_trotinete, String nova_marca);

     void atualizarNumeroSerieTrotinete(int id_trotinete, int novo_num_serie);

     void atualizarTipoMotorTrotinete(int id_trotinete, String novo_tipo_motor);
}
