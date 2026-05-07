package app.IecoRideCA.controllers.ordensservico;

import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;

public class OrdemServicoController {

    private final IEcoRideLN facade;

    public OrdemServicoController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {
    }
}
