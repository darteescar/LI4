package app.IEcoRideLN.controllers.reparacoes.dto;

public record ReparacaoRequest(    
     String nomenclatura,
     String descricao,
     float preco,
     boolean disponivel) {}