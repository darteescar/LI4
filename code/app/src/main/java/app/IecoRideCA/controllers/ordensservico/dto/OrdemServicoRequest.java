package app.IecoRideCA.controllers.ordensservico.dto;

import java.util.List;

public record OrdemServicoRequest(
     int id_cliente,
     int id_trotinete,
     String descricao,
     List<String> acessorios) {}