package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.List;

public interface ISOrdensServico {

     // ------------------- Registo -------------------

     public OrdemServico registarOS(int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotografias, int codCriador);

     public void atualizarOS(int id, String descricao, List<String> acessorios, List<Fotografia> fotografias, int id_cliente, int id_trotinete);

     public OrdemServico obterOS(int id);

     public boolean existeOS(int id);

     public List<OrdemServico> obterTodasOSs();

     public boolean removerOS(int id);

     // ------------------- Máquina de estados -------------------

     public void alterarEstadoOS(int id, EstadoOS estado);

     public void submeterDiagnosticoOS(int id);

     public void aprovarOrcamentoOS(int id);

     public void rejeitarOrcamentoOS(int id);

     public void marcarAguardarPecasOS(int id);

     public void pecasRecebidasOS(int id);

     public void concluirReparacaoOS(int id);

     public void pagarOS(int id);

     public void eliminarOS(int id);

     public void atribuirOS(int id, int id_funcionario);

     public void registarNotificacaoPagamentoOS(int id_OS);

     // ------------------- Diagnóstico -------------------

     public void registarDiagnosticoOS(int idOS, List<PecasOrcamento> listPecas, List<Integer> reparacoes, float orcamento, String descricao);

     public List<Integer> obterReparacoesDiagnosticoOS(int idOS);

     public List<PecasOrcamento> obterPecasDiagnosticoOS(int idOS);

     // ------------------- Conserto -------------------

     public void registarConsertoOS(int id_OS, List<PecasUsadas> pecas, List<Integer> reparacoes, float orcamento);

     public List <Integer> obterReparacoesConsertoOS(int id_OS);

     public List <PecasUsadas> obterPecasUsadasConsertoOS(int id_OS);

     public boolean validarChecklist_ConsertoOS(int idOS);

     // ------------------- Pagamento -------------------

     public void registarPagamentoOS(int id_OS, Metodo_Pagamento metodo_pagamento);

     // ------------------- Utilitários -------------------

     public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, Integer id_cliente, Integer id_funcionario);

     public boolean validarFotografia(Fotografia foto);
}
