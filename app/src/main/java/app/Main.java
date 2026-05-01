package app;

import app.ecoRideCD.DAOconfig.DAOconfig;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.SAutenticacaoFacade;
import app.ecoRideLN.sAutenticacao.Utilizador;

public class Main {
    public static void main(String[] args) {
        DAOconfig.CreateBD();

        UtilizadorDAO.getInstance().save(new Utilizador(1, "admin", 1, Cargo.Gerente));

        SAutenticacaoFacade sAut = new SAutenticacaoFacade();
        System.out.println("login admin/admin   -> " + sAut.autenticar(1, "admin"));
        System.out.println("login admin/errada  -> " + sAut.autenticar(1, "errada"));
        System.out.println("cargo do 1          -> " + sAut.cargoDe(1));
    }
}
