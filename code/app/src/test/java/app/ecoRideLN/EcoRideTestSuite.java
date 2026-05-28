package app.ecoRideLN;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("EcoRide — Todos os testes")
@SelectClasses({
    TesteValidacoes.class,
    TesteFuncionarios.class,
    TesteClientes.class,
    TesteStock.class,
    TesteEncomendas.class,
    TesteOrdensServico.class,
    TesteAutenticacao.class,
    TesteReparacoes.class,
    TesteNotificacoes.class,
    TesteFinanceiro.class,
    // Testes de integração
    TesteIntegracaoOS.class,
    TesteIntegracaoStock.class
})
public class EcoRideTestSuite {}
