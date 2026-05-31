package app.IEcoRideLN.controllers.ordensservico.dto;

import java.util.List;
import java.util.Map;

public record DiagnosticoRequest(
     Map<Integer, Integer> pecasQuantidades,
     List<Integer> reparacoes,
     String descricao
) {}