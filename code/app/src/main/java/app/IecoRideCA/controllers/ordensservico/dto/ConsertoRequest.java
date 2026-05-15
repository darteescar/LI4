package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;
import java.util.Map;

import app.ecoRideLN.sOrdensServico.CheckList;

public record ConsertoRequest(
     Map<String, Integer> pecasQuantidades,
     List<Integer> reparacoes,
     CheckList checklist) {}
