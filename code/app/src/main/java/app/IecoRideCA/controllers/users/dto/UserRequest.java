package app.IecoRideCA.controllers.users.dto;

import app.ecoRideLN.sAutenticacao.Cargo;

public record UserRequest(
    String password, 
    int idFuncionario, 
    Cargo cargo, 
    String identificador 
) {

}
