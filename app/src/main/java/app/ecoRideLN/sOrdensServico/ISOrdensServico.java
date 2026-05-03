package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Stock;

public interface ISOrdensServico {

     public OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao);

     public OrdemServico registarOS_Extras(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, List<PecasOrcamento> pecasOrcamento);

     public OrdemServico obterDadosOS(int id);

     public boolean removerOS(int id);

     public void alterarDescricaoOS(int id, String Descricao);

     public void alterarAcessoriosOS(int id, List<String> acessorios);

     public void alterarFotografiasOS(int id, List<Fotografia> fotografias);

     public void alterarEstadoOS(int id, EstadoOS estado);

     public void adicionarPecas_Conserto_OS(int id, List<Stock> pecas);

     public boolean clienteNaoTemPagamentosPendentes(int id);

     public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, int id_cliente, int id_funcionario);

     public float calcularOrcamento(List<PecasOrcamento> listaPecas, List<Reparacao> reparacoes);

     public boolean pecasDiagnosticoDisponiveisReparacao(int id_OS);

     public boolean removerPecaConserto_OS(int id_OS, int is_Stock);

     public boolean validarFotografia(Fotografia foto);

     public void alterarDataCriacaoOS(int id, LocalDateTime data_criacao);

     public void alterarFuncionarioResponsavelOS(int id, int codResponsavel);

     public void alterarClienteOS(int id, int id_cliente);

     public void alterarTrotineteOS(int id, int id_trotinete);

     public List<OrdemServico> filtrarOSsPorCliente(int id_cliente);

     public List<OrdemServico> filtrarOSsPorFuncionario(int id_funcionario);

     public List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate);

     public List<OrdemServico> filtrarOSsPorClienteEIntervalo(int id_funcionario, LocalDateTime desde, LocalDateTime ate);

     public List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime desde, LocalDateTime ate);

     public List<OrdemServico> filtrarOSsPorClienteEFuncionario(int id_cliente, int id_funcionario);

     public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, String descricao, int idMecanico);

     public List<Reparacao> obterReparacoesDiagnosticoOS(int idOS);

     public List<PecasOrcamento> obterPecasQuantidadeDiagnosticoOS(int idOS);

     public void adicionarPecaConserto_OS(int id_OS, int id_Stock);

     public void registarConsertoOS(int id_OS, int idMecanico , List<Stock> pecas, List<Reparacao> reparacoes);

     public boolean validarChecklist_ConsertoOS(int idOS);

}
