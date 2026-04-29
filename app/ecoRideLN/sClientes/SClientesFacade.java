package app.ecoRideLN.sClientes;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sClientes.ClienteDAO;
import app.ecoRideCD.sClientes.TrotineteDAO;

import java.util.List;
import java.util.Optional;

public class SClientesFacade implements ISClientes {

    private final ClienteDAO clientesDAO = new ClienteDAO();
    private final TrotineteDAO trotinetesDAO = new TrotineteDAO();

    private int idCliente = 1;
    private int idTrotinete = 1;

    @Override
    public Cliente registarCliente(String nome, String email, String telemovel, String nif) {
        Validacoes.naoVazio(nome, "Nome");
        Validacoes.emailValido(email);
        Validacoes.telemovel(telemovel);
        Validacoes.nif(nif);

        if (clientesDAO.obterPorNif(nif).isPresent())
            throw new EcoRideException("Já existe um cliente com este NIF.");

        Cliente c = new Cliente(idCliente++, nome, email, telemovel, nif);
        clientesDAO.put(c.getId(), c);
        return c;
    }

    @Override
    public Optional<Cliente> obterDadosCliente(int id) {
        return clientesDAO.obterPorId(id);
    }

    @Override
    public boolean existeCliente(int id) {
        return clientesDAO.containsKey(id);
    }

    @Override
    public void removerCliente(int id) {
        if (!existeCliente(id))
            throw new EcoRideException("Cliente não encontrado.");
        // Remove trotinetes associadas
        for (Trotinete t : trotinetesDAO.obterPorCliente(id))
            trotinetesDAO.remove(t.getId());
        clientesDAO.remove(id);
    }

    @Override
    public List<Trotinete> obterTrotinetesCliente(int idCliente) {
        if (!existeCliente(idCliente))
            throw new EcoRideException("Cliente não encontrado.");
        return trotinetesDAO.obterPorCliente(idCliente);
    }

    private Cliente obterClienteOuFalhar(int id) {
        return clientesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Cliente não encontrado."));
    }

    @Override
    public void atualizarNomeCliente(int id, String nome) {
        Validacoes.naoVazio(nome, "Nome");
        Cliente c = obterClienteOuFalhar(id);
        c.setNome(nome);
        clientesDAO.put(c.getId(), c);
    }

    @Override
    public void atualizarEmailCliente(int id, String email) {
        Validacoes.emailValido(email);
        Cliente c = obterClienteOuFalhar(id);
        c.setEmail(email);
        clientesDAO.put(c.getId(), c);
    }

    @Override
    public void atualizarTelemovelCliente(int id, String telemovel) {
        Validacoes.telemovel(telemovel);
        Cliente c = obterClienteOuFalhar(id);
        c.setTelemovel(telemovel);
        clientesDAO.put(c.getId(), c);
    }

    @Override
    public void atualizarNIFCliente(int id, String nif) {
        Validacoes.nif(nif);
        Cliente c = obterClienteOuFalhar(id);
        if (clientesDAO.obterPorNif(nif).filter(o -> o.getId() != id).isPresent())
            throw new EcoRideException("Já existe um cliente com este NIF.");
        c.setNIF(nif);
        clientesDAO.put(c.getId(), c);
    }

    @Override
    public Trotinete registarTrotinete(String modelo, String marca, String numero_serie, String tipo_motor, int idCliente) {
        Validacoes.naoVazio(modelo, "Modelo");
        Validacoes.naoVazio(marca, "Marca");
        Validacoes.naoVazio(numero_serie, "Número de série");
        Validacoes.naoVazio(tipo_motor, "Tipo de motor");
        if (!existeCliente(idCliente))
            throw new EcoRideException("Cliente não encontrado.");

        Trotinete t = new Trotinete(idTrotinete++, modelo, marca, numero_serie, tipo_motor, idCliente);
        trotinetesDAO.put(t.getId(), t);
        clientesDAO.obterPorId(idCliente).ifPresent(c -> c.getCodsTrotinetes().add(t.getId()));
        return t;
    }

    @Override
    public Optional<Trotinete> obterDadosTrotinete(int id) {
        return trotinetesDAO.obterPorId(id);
    }

    @Override
    public boolean existeTrotinete(int id) {
        return trotinetesDAO.containsKey(id);
    }

    @Override
    public void removerTrotinete(int id) {
        Trotinete t = trotinetesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Trotinete não encontrada."));
        clientesDAO.obterPorId(t.getCodCliente())
                .ifPresent(c -> c.getCodsTrotinetes().remove(Integer.valueOf(id)));
        trotinetesDAO.remove(id);
    }

    private Trotinete obterTrotineteOuFalhar(int id) {
        return trotinetesDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Trotinete não encontrada."));
    }

    @Override
    public void atualizarModeloTrotinete(int id, String modelo) {
        Validacoes.naoVazio(modelo, "Modelo");
        Trotinete t = obterTrotineteOuFalhar(id);
        t.setModelo(modelo);
        trotinetesDAO.put(t.getId(), t);
    }

    @Override
    public void atualizarMarcaTrotinete(int id, String marca) {
        Validacoes.naoVazio(marca, "Marca");
        Trotinete t = obterTrotineteOuFalhar(id);
        t.setMarca(marca);
        trotinetesDAO.put(t.getId(), t);
    }

    @Override
    public void atualizarNumeroSerieTrotinete(int id, String numero_serie) {
        Validacoes.naoVazio(numero_serie, "Número de série");
        Trotinete t = obterTrotineteOuFalhar(id);
        t.setNum_serie(numero_serie);
        trotinetesDAO.put(t.getId(), t);
    }

    @Override
    public void atualizarTipoMotorTrotinete(int id, String tipo_motor) {
        Validacoes.naoVazio(tipo_motor, "Tipo de motor");
        Trotinete t = obterTrotineteOuFalhar(id);
        t.setTipo_motor(tipo_motor);
        trotinetesDAO.put(t.getId(), t);
    }
}
