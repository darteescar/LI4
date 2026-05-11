package app.IecoRideCA.controllers;

import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;

public class MainController {
     private final IEcoRideLN facade;

     public MainController(IEcoRideLN facade) {
        this.facade = facade;
     }

     public void register(Javalin app) {
          app.get("/", ctx -> {

               // get dados necessarios para dashboard


               ctx.status(200).result("Bem-vindo ao IecoRideCA API!");
          });
     }

}
