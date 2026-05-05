package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.List;

import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Stock;

public interface ISOrdensServico {

     // ------------------- Registo -------------------

     OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao);

     OrdemServico registarOS_Extras(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias);

     // ------------------- Consulta -------------------

     OrdemServico obterOS(int id);

     List<OrdemServico> obterTodasOSs();

     boolean removerOS(int id);

     // ------------------- Alterações de campos -------------------

     void alterarDescricaoOS(int id, String descricao);

     void alterarAcessoriosOS(int id, List<String> acessorios);

     void alterarFotografiasOS(int id, List<Fotografia> fotografias);

     void alterarDataCriacaoOS(int id, LocalDateTime data_criacao);

     void alterarFuncionarioResponsavelOS(int id, int codResponsavel);

     void alterarClienteOS(int id, int id_cliente);

     void alterarTrotineteOS(int id, int id_trotinete);

     // ------------------- Máquina de estados -------------------

     void alterarEstadoOS(int id, EstadoOS estado);

     void submeterDiagnosticoOS(int id);

     void aprovarOrcamentoOS(int id);

     void rejeitarOrcamentoOS(int id);

     void marcarAguardarPecasOS(int id);

     void pecasRecebidasOS(int id);

     void concluirReparacaoOS(int id);

     void pagarOS(int id);

     void eliminarOS(int id);

     // ------------------- Diagnóstico -------------------

     void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Reparacao> reparacoes, String descricao, int idMecanico);

     List<Reparacao> obterReparacoesDiagnosticoOS(int idOS);

     List<PecasOrcamento> obterPecasQuantidadeDiagnosticoOS(int idOS);

     // ------------------- Conserto -------------------

     void registarConsertoOS(int id_OS, int idMecanico, List<Stock> pecas, List<Reparacao> reparacoes);

     void adicionarPecas_Conserto_OS(int id, List<Stock> pecas);

     void adicionarPecaConserto_OS(int id_OS, int id_Stock);

     boolean removerPecaConserto_OS(int id_OS, int id_Stock);

     boolean validarChecklist_ConsertoOS(int idOS);

     // ------------------- Filtragem -------------------

     List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario);

     List<OrdemServico> filtrarOSsPorCliente(int id_cliente);

     List<OrdemServico> filtrarOSsPorFuncionario(int id_funcionario);

     List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate);

     List<OrdemServico> filtrarOSsPorClienteEIntervalo(int id_cliente, LocalDateTime desde, LocalDateTime ate);

     List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime desde, LocalDateTime ate);

     List<OrdemServico> filtrarOSsPorClienteEFuncionario(int id_cliente, int id_funcionario);

     // ------------------- Utilitários -------------------

     boolean clienteTemApenasUmPagamentoPendente(int id);

     float calcularOrcamento(List<PecasOrcamento> listaPecas, List<Reparacao> reparacoes);

     boolean validarFotografia(Fotografia foto);
}
