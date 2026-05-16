package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ISOrdensServico {

     // ------------------- Registo -------------------

     public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, int codCriador);

     public void atualizarOS(int id, String descricao, List<String> acessorios, int id_cliente, int id_trotinete);

     public OrdemServico obterOS(int id);

     public List<OrdemServico> obterOSs();

     public List<OrdemServico> obterOSsAtivas();

     public List<OrdemServico> obterOSsDisponiveis();

     public boolean removerOS(int id);

     // ------------------- Máquina de estados -------------------

     public boolean alterarEstadoOS(int id, EstadoOS estado);

     public boolean aprovarOrcamentoOS(int id);

     public boolean rejeitarOrcamentoOS(int id);

     public boolean marcarAguardarPecasOS(int id, int id_funcionario);

     public boolean pecasRecebidasOS(int id);

     public boolean eliminarOS(int id);

     public boolean atribuirOS(int id, int id_funcionario);

     public boolean registarNotificacaoPagamentoOS(int id_OS);

     public Diagnostico registarDiagnosticoOS(int idOS, Map<Integer, Integer> pecasQuantidades, List<Integer> reparacoes, float orcamento, String descricao, int id_funcionario);

     public Conserto registarConsertoOS(int id_OS, Map<Integer, Integer> stocksUsados, List<Integer> reparacoes, float valor, int id_funcionario);

     public Map<Integer, Integer> obterStocksUsadosConsertoOS(int id_OS);

     public boolean registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento);

     // ------------------- Utilitários -------------------

     public List<OrdemServico> obterOSs_Trotinete(int id_trotinete);

     public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario);   
}
