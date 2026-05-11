package app.IecoRideCA.controllers.ordensservico.dto;

public record DefeitoStockRequest(
     int idOS, 
     int codPeca, 
     String motivo, 
     int idFuncionario
) {}