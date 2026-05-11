package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;
import java.util.Map;

import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sReparacoes.Reparacao;

public record ConsertoRequest(
     int id_OS,
     Map<Integer, Integer> pecasQuantidades,
     List<Reparacao> reparacoes,
     CheckList checklist) {}
