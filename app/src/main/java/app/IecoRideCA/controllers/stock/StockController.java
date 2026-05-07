package app.IecoRideCA.controllers.stock;

import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;

public class StockController {

    private final IEcoRideLN facade;

    public StockController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {
    }
}
