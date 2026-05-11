package app.IecoRideCA.controllers;

import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;

public class ExtrasController {
     private final IEcoRideLN facade;

     public ExtrasController(IEcoRideLN facade) {
        this.facade = facade;
     }

     public void register(Javalin app) {
          app.get("/", ctx -> {
               ctx.status(200).result("Bem-vindo ao IecoRideCA API!");
          });
     }

}
