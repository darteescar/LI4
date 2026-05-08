package app.IecoRideCA.controllers.auth.dto;

import app.ecoRideLN.sAutenticacao.Cargo;

public record RegistRequest(
     String password, 
     int idFuncionario, 
     Cargo cargo, 
     String identificador
) {}