package app.ecoRideLN.sAutenticacao;

public class Utilizador {

    private int id;
    private String password;
    private int idFuncionario;
    private Cargo cargo;
    private String identificador;

    public Utilizador(int id, String password, int idFuncionario, Cargo cargo, String identificador) {
        this.id = id;
        this.identificador = identificador;
        this.password = password;
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdFuncionario() {
        return this.idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Cargo getCargo() {
        return this.cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
