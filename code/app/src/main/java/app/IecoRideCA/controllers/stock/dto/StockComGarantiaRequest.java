package app.IecoRideCA.controllers.stock.dto;

import java.time.LocalDate;

public record StockComGarantiaRequest(
     int codPeca,
     float preco,
     int quantidade,
     LocalDate dataChegada,
     int garantia, 
     String nr_serie
) {}