package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ISOrdensServico {

     // ------------------- Registo -------------------

     public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador);

     public void atualizarOS(int id, String descricao, List<String> acessorios, List<Fotografia> fotografias, int id_cliente, int id_trotinete);

     public OrdemServico obterOS(int id);

     public boolean existeOS(int id);

     public List<OrdemServico> obterOSs();

     public boolean removerOS(int id);

     // ------------------- Máquina de estados -------------------

     public void alterarEstadoOS(int id, EstadoOS estado);

     public void aprovarOrcamentoOS(int id);

     public void rejeitarOrcamentoOS(int id);

     public void marcarAguardarPecasOS(int id);

     public void pecasRecebidasOS(int id);

     public void eliminarOS(int id);

     public void atribuirOS(int id, int id_funcionario);

     public void registarNotificacaoPagamentoOS(int id_OS);

     public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, float orcamento, String descricao, int id_funcionario);

     public void registarConsertoOS(int id_OS, Map<Integer, Integer> stocksUsados, List<Integer> reparacoes, float orcamento, int id_funcionario);

     public Map<Integer, Integer> obterStocksUsadosConsertoOS(int id_OS);

     public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento);

     // ------------------- Utilitários -------------------

     public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario);

     public boolean validarFotografia(Fotografia foto);
}
