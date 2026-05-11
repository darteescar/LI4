package app.IecoRideCA.controllers.auth.dto;

public record LoginResponse (

    String token,
    String cargo,
    int idFuncionario,
    int idUtilizador

) {}