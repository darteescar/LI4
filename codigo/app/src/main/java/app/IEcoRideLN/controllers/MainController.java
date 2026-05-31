package app.IEcoRideLN.controllers;

import app.ecoRideLN.IEcoRideController;
import io.javalin.Javalin;

public class MainController {
     private final IEcoRideController facade;

     public MainController(IEcoRideController facade) {
        this.facade = facade;
     }

     public void register(Javalin app) {
          app.get("/", ctx -> {

               // get dados necessarios para dashboard

               ctx.status(200).result("Bem-vindo ao IecoRideCA API!");
          });
     }

}
