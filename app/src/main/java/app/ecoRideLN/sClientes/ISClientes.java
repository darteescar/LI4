package app.ecoRideLN.sClientes;

import java.util.List;

import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.OrdemServico;

public interface ISClientes {
     public Cliente registarCliente(String nome, String email, String telemovel, String nif);

     public Cliente obterDadosCliente(int id);

     public List<OrdemServico> obterOS_Cliente(int id);

     public boolean removerCliente(int id);

     public List<Trotinete> obterTrotinetes_Cliente(int id);

     public boolean existeCliente(int id);

     public Trotinete registarTrotinete(int id_cliente, String modelo, String marca, int num_serie, String tipo_motor);

     public List<OrdemServico> obterOS_Trotinete(int id_trotinete);

     public Trotinete obterDadosTrotinete(int id);

     public boolean removerTrotinete(int id);

     public List<Conserto> obterConsertosAnteriores(int id_trotinete);

     public void atualizarDadosCliente(int id_cliente, String novo_nome, String novo_email, String novo_telemovel, String novo_nif);
}
