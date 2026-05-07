package app.IecoRideCA.controllers.reparacoes.dto;

public record ReparacaoRequest(    
     String nomenclatura,
     String descricao,
     float preco,
     boolean disponivel) {

}
