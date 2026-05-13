package app.IecoRideCA;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.auth.SessaoUtilizador;
import app.IecoRideCA.controllers.MainController;
import app.IecoRideCA.controllers.auth.AuthController;
import app.IecoRideCA.controllers.clientes.ClientesController;
import app.IecoRideCA.controllers.financeiro.FinanceiroController;
import app.IecoRideCA.controllers.funcionarios.FuncionariosController;
import app.IecoRideCA.controllers.notificacoes.NotificacoesController;
import app.IecoRideCA.controllers.ordensservico.OrdemServicoController;
import app.IecoRideCA.controllers.reparacoes.ReparacoesController;
import app.IecoRideCA.controllers.stock.StockController;
import app.IecoRideCA.controllers.users.UsersController;
import app.ecoRideCD.DAOconfig.DBInitializer;
import app.ecoRideLN.EcoRideLN;
import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.json.JavalinJackson;

public class EcoRideAPI {

    public static void main(String[] args) {
        DBInitializer.run();

        IEcoRideLN    facade        = new EcoRideLN();
        GestorSessoes gestorSessoes = new GestorSessoes();

        Javalin app = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.jsonMapper(new JavalinJackson().updateMapper(m ->
                m.registerModule(new JavaTimeModule())
                 .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            ));
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(it -> it.allowHost("http://localhost:8080"))
            );
        }).start(7000);

        // Middleware — todas as rotas /api/* exigem sessão válida
        app.before("/api/*", ctx -> {
            String token = ctx.header("Authorization");
            if (token == null) throw new UnauthorizedResponse("Token em falta");
            SessaoUtilizador sessao = gestorSessoes.validar(token);
            ctx.attribute("sessao", sessao);
        });

        GlobalExceptionHandler.register(app);

        // Registo de controllers
        new AuthController(facade, gestorSessoes).register(app);
        new UsersController(facade).register(app);
        new ClientesController(facade).register(app);
        new OrdemServicoController(facade).register(app);
        new StockController(facade).register(app);
        new FuncionariosController(facade).register(app);
        new ReparacoesController(facade).register(app);
        new NotificacoesController(facade).register(app);
        new FinanceiroController(facade).register(app);
        new MainController(facade).register(app);
    }

}
