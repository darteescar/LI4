package app.ecoRideLN;

import app.ecoRideLN.sAutenticacao.ISAutenticacao;
import app.ecoRideLN.sAutenticacao.SAutenticacaoFacade;
import app.ecoRideLN.sClientes.ISClientes;
import app.ecoRideLN.sClientes.SClientesFacade;
import app.ecoRideLN.sFinanceiro.ISFinanceiro;
import app.ecoRideLN.sFinanceiro.SFinanceiroFacade;
import app.ecoRideLN.sFuncionarios.ISFuncionarios;
import app.ecoRideLN.sFuncionarios.SFuncionariosFacade;
import app.ecoRideLN.sNotificacoes.ISNotificacoes;
import app.ecoRideLN.sNotificacoes.SNotificacoesFacade;
import app.ecoRideLN.sOrdensServico.ISOrdensServico;
import app.ecoRideLN.sOrdensServico.SOrdensServicoFacade;
import app.ecoRideLN.sReparacoes.ISReparacoes;
import app.ecoRideLN.sReparacoes.SReparacoesFacade;
import app.ecoRideLN.sStock.ISStock;
import app.ecoRideLN.sStock.SStockFacade;

public class EcoRideLN implements IEcoRideLN {

     private ISNotificacoes sNotificacoes;
     private ISAutenticacao sAutenticacao;
     private ISClientes sClientes;
     private ISFinanceiro sFinanceiro;
     private ISFuncionarios sFuncionarios;
     private ISOrdensServico sOrdensServico;
     private ISStock sStock;
     private ISReparacoes sReparacoes;

     public EcoRideLN() {
          this.sNotificacoes = new SNotificacoesFacade();
          this.sAutenticacao = new SAutenticacaoFacade();
          this.sClientes = new SClientesFacade();
          this.sFinanceiro = new SFinanceiroFacade();
          this.sFuncionarios = new SFuncionariosFacade();
          this.sOrdensServico = new SOrdensServicoFacade();
          this.sStock = new SStockFacade();
          this.sReparacoes = new SReparacoesFacade();
     }


     // ------------------- NOTIFICAÇÕES -------------------
     // ------------------- ORDENS SERVICO -------------------
     // ------------------- CLIENTES -------------------
     // ------------------- TROTINETES -------------------
     // ------------------- REPARACÕES -------------------
     // ------------------- PEÇAS -------------------
     // ------------------- STOCK -------------------
     // ------------------- DEVOLUÇÕES -------------------
     // ------------------- ENCOMENDAS -------------------
     // ------------------- FORNECEDORES -------------------
     // ------------------- FUNCIONÁRIOS -------------------
     // ------------------- FINANCEIRO -------------------

}
