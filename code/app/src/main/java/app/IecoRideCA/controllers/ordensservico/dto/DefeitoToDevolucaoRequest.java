package app.IecoRideCA.controllers.ordensservico.dto;

import java.time.LocalDate;

public record DefeitoToDevolucaoRequest(
     int idDefeito, 
     String motivo, 
     LocalDate data
) {}