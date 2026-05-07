package app.IecoRideCA.controllers.stock.dto;

import java.util.List;

import app.ecoRideLN.sStock.ItemEncomenda;

public record EncomendaRequest(
     int cod_fornecedor,
     List<ItemEncomenda> itens
) {
}
