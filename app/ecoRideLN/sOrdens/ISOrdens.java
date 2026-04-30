package app.ecoRideLN.sOrdens;

import app.ecoRideLN.sStock.PecasDoOrcamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISOrdens {

    OrdemServico registarOS(int idCliente, int idTrotinete, String descricao, int idResponsavel);

    OrdemServico registarOSExtras(int idCliente, int idTrotinete, String descricao, int idResponsavel,
                                   List<String> acessorios, List<Fotografia> fotos);

    Optional<OrdemServico> obterDadosOS(int idOS);

    boolean existeOS(int idOS);

    void removerOS(int idOS);

    void alterarDescricaoOS(int idOS, String descricao);

    void alterarAcessoriosOS(int idOS, List<String> acessorios);

    void alterarFotografiasOS(int idOS, List<Fotografia> fotos);

    void alterarEstadoOS(int idOS, EstadoOS novoEstado);

    void alterarDataCriacaoOS(int idOS, LocalDateTime data);

    void alterarFuncionarioResponsavelOS(int idOS, int codResponsavel);

    void alterarClienteOS(int idOS, int idCliente);

    void alterarTrotineteOS(int idOS, int idTrotinete);

    // Diagnóstico
    void registarDiagnosticoOS(int idOS, int idMecanico, String descricao,
                                List<Integer> codReparacoes, List<PecasDoOrcamento> listaPecas);

    List<Integer> obterReparacoesDiagnosticoOS(int idOS);

    List<PecasDoOrcamento> obterPecasQuantidadeDiagnosticoOS(int idOS);

    float calcularOrcamento(List<Integer> codReparacoes, List<PecasDoOrcamento> listaPecas);

    boolean pecasDiagnosticoDisponiveisReparacao(int idOS);

    // Conserto
    void registarConsertoOS(int idOS, int idMecanico, List<Integer> codReparacoes, List<Integer> codStocks);

    void adicionarPecaConsertoOS(int idOS, int idStock);

    void removerPecaConsertoOS(int idOS, int idStock);

    void validarChecklistConsertoOS(int idOS, Checklist checklist);

    // Pagamento
    boolean clienteNaoTemPagamentosPendentes(int idCliente, int idOS);

    void registarPagamentoOS(int idOS);

    // Filtros
    List<OrdemServico> filtrarOSs(LocalDateTime desde, LocalDateTime ate, Integer idCliente, Integer idFuncionario);

    List<OrdemServico> filtrarOSsPorCliente(int idCliente);

    List<OrdemServico> filtrarOSsPorFuncionario(int idFuncionario);

    List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorClienteEIntervalo(int idCliente, LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int idFuncionario, LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorClienteEFuncionario(int idCliente, int idFuncionario);

    boolean validarFotografia(Fotografia f);
}
