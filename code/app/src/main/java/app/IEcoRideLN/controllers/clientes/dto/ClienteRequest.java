package app.IEcoRideLN.controllers.clientes.dto;

public record ClienteRequest (

    String nome,
    String email,
    String telemovel,
    String nif,
    int num_trotinetes

) {}