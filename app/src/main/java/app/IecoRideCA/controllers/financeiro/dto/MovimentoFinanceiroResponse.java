package app.IecoRideCA.controllers.financeiro.dto;

import java.util.List;

import app.ecoRideLN.sFinanceiro.AnaliseFinanceira;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;

public record MovimentoFinanceiroResponse(
    List<MovimentoFinanceiro> movimentos,
    AnaliseFinanceira analise
) {
}