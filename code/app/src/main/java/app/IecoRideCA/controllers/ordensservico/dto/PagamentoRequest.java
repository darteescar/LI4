package app.IecoRideCA.controllers.ordensservico.dto;

import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;

public record PagamentoRequest(
    Metodo_Pagamento metodo_pagamento
) {}