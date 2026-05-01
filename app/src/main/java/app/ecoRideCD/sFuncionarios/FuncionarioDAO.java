package app.ecoRideCD.sFuncionarios;

public class FuncionarioDAO {
     private static FuncionarioDAO instance;

     private FuncionarioDAO() {}

     public static FuncionarioDAO getInstance() {
          if (instance == null) instance = new FuncionarioDAO();
          return instance;
     }

     
     
}
