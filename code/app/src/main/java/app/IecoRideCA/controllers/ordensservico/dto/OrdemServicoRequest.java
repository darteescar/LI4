package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

import app.ecoRideLN.sOrdensServico.Fotografia;

public record OrdemServicoRequest(
     int id_cliente, 
     int id_trotinete, 
     String descricao, 
     List<String> acessorios, 
     List<Fotografia> fotografias, 
     int codCriador) {}