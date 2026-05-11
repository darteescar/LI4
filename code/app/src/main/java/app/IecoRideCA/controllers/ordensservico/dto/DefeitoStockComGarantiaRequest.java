package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

public record DefeitoStockComGarantiaRequest(
     int idOS, 
     List<Integer> codStocks, 
     String motivo, 
     int idFuncionario
) {}