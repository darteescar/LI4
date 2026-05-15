package app.IEcoRideLN.controllers.stock.dto;

public record DefeitoRequest(
     int codPeca,
     String motivo,
     int idFuncionario
) {}