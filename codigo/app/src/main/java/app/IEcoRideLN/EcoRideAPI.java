package app.IEcoRideLN;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import app.IEcoRideLN.auth.GestorSessoes;
import app.IEcoRideLN.auth.SessaoUtilizador;
import app.IEcoRideLN.controllers.MainController;
import app.IEcoRideLN.controllers.auth.AuthController;
import app.IEcoRideLN.controllers.clientes.ClientesController;
import app.IEcoRideLN.controllers.financeiro.FinanceiroController;
import app.IEcoRideLN.controllers.funcionarios.FuncionariosController;
import app.IEcoRideLN.controllers.notificacoes.NotificacoesController;
import app.IEcoRideLN.controllers.ordensservico.OrdemServicoController;
import app.IEcoRideLN.controllers.reparacoes.ReparacoesController;
import app.IEcoRideLN.controllers.stock.StockController;
import app.IEcoRideLN.controllers.users.UsersController;
import app.ecoRideCD.DAOconfig.DBInitializer;
import app.ecoRideLN.EcoRideController;
import app.ecoRideLN.IEcoRideController;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.json.JavalinJackson;

public class EcoRideAPI {

    public static void main(String[] args) {
        DBInitializer.run();

        IEcoRideController    facade        = new EcoRideController();
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

        // Middleware — todas as rotas /api/* exigem sessão válida (ignorado em DEV_MODE)
        boolean devMode = "true".equalsIgnoreCase(System.getenv("DEV_MODE"));
        if (devMode) {
            System.out.println("[EcoRide] DEV_MODE activo — autenticação desligada");
            app.before("/api/*", ctx -> {
                // Injeta sessão de Gerente para que verifica_cargo não lance 403
                ctx.attribute("sessao", SessaoUtilizador.devSession());
            });
        } else {
            app.before("/api/*", ctx -> {
                String token = ctx.header("Authorization");
                if (token == null) throw new UnauthorizedResponse("Token em falta");
                SessaoUtilizador sessao = gestorSessoes.validar(token);
                ctx.attribute("sessao", sessao);
            });
        }

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
