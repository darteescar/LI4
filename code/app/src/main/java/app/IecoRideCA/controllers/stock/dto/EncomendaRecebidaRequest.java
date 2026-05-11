package app.IecoRideCA.controllers.stock.dto;

import java.util.List;

public record EncomendaRecebidaRequest(
     List<String> numeros_serie,
     List<Integer> garantias
) {}
