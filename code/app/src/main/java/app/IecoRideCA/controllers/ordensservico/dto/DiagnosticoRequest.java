package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

import app.ecoRideLN.sOrdensServico.PecasOrcamento;
import app.ecoRideLN.sReparacoes.Reparacao;

public record DiagnosticoRequest(
     int idOS, 
     List<PecasOrcamento> listPecas, 
     List<Reparacao> reparacoes, 
     String descricao
) {}