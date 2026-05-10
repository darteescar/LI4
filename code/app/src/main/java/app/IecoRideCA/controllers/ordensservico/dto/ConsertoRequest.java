package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

import app.ecoRideLN.sReparacoes.Reparacao;

public record ConsertoRequest(
     int id_OS,
     List<Integer> pecas,
     List<Reparacao> reparacoes) {}
