package app.IecoRideCA.controllers.stock.dto;

import java.time.LocalDate;

public record SplitDefeitoRequest(
     int quantidade, 
     String motivo, 
     LocalDate data) {}