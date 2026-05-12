package app.IecoRideCA.controllers.stock.dto;

public record PecaRequest (
     String referencia,
     String marca,
     String nome,
     String descricao,
     int stock_minimo,
     float preco_venda,
     int codFornecedor,
     boolean ativa
) {}