package app.IEcoRideLN.controllers.ordensservico.dto;

import java.util.List;

public record DefeitoStockComGarantiaRequest(
     List<Integer> codStocks,
     String motivo
) {}