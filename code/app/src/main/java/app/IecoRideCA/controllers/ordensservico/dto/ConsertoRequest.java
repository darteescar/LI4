package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;
import java.util.Map;

import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sReparacoes.Reparacao;

public record ConsertoRequest(
     Map<Integer, Integer> pecasQuantidades,
     List<Reparacao> reparacoes,
     CheckList checklist) {}
