package app.IEcoRideLN.controllers.clientes.dto;

public record TrotineteRequest (
     int cod_cliente,
     String marca,
     String modelo,
     String num_serie,
     String motor
) {}
