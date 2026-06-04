package app.IEcoRideLN.controllers.ordensservico.dto;

import java.time.LocalDate;

public record DefeitoToDevolucaoRequest(
     String motivo,
     LocalDate data
) {}