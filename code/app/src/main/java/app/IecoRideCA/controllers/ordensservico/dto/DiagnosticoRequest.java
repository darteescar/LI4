package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;
import java.util.Map;

import app.ecoRideLN.sReparacoes.Reparacao;

public record DiagnosticoRequest(
     Map<Integer, Integer> pecasQuantidades,
     List<Reparacao> reparacoes,
     String descricao
) {}