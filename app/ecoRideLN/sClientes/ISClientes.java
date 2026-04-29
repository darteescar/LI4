package app.ecoRideLN.sClientes;

import java.util.List;
import java.util.Optional;

public interface ISClientes {

    Cliente registarCliente(String nome, String email, String telemovel, String nif);

    Optional<Cliente> obterDadosCliente(int id);

    boolean existeCliente(int id);

    void removerCliente(int id);

    List<Trotinete> obterTrotinetesCliente(int idCliente);

    void atualizarNomeCliente(int id, String nome);

    void atualizarEmailCliente(int id, String email);

    void atualizarTelemovelCliente(int id, String telemovel);

    void atualizarNIFCliente(int id, String nif);

    Trotinete registarTrotinete(String modelo, String marca, String numero_serie, String tipo_motor, int idCliente);

    Optional<Trotinete> obterDadosTrotinete(int id);

    boolean existeTrotinete(int id);

    void removerTrotinete(int id);

    void atualizarModeloTrotinete(int id, String modelo);

    void atualizarMarcaTrotinete(int id, String marca);

    void atualizarNumeroSerieTrotinete(int id, String numero_serie);

    void atualizarTipoMotorTrotinete(int id, String tipo_motor);
}
