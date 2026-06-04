package app.IEcoRideLN.controllers.stock.dto;

import java.time.LocalDate;

public record StockRequest(
    int codPeca,
    float preco,
    int quantidade,
    LocalDate dataChegada
) {}