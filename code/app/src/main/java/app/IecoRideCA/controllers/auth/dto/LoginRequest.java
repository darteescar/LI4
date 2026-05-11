package app.IecoRideCA.controllers.auth.dto;

public record LoginRequest (

    String identificador,
    String password

) {}