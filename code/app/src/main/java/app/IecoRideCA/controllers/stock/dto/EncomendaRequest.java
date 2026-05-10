package app.IecoRideCA.controllers.stock.dto;

import java.util.List;

public record EncomendaRequest(
     int cod_fornecedor,
     List<ItemEncomendaRequest> itens
) {
     public record ItemEncomendaRequest(
          int codPeca,
          int quantidade,
          float preco_compra
     ) {}
}
