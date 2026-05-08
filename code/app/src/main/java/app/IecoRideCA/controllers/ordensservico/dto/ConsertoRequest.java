package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

import app.ecoRideLN.sReparacoes.Reparacao;
import app.ecoRideLN.sStock.Stock;

public record ConsertoRequest(
     int id_OS, 
     List<Stock> pecas, 
     List<Reparacao> reparacoes) {}