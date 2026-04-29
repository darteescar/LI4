package app.ecoRideLN.sAutenticacao;

public class Utilizador {

    private int id;
    private String password;
    private int idFuncionario;
    private Cargo cargo;

    public Utilizador(int id, String password, int idFuncionario, Cargo cargo) {
        this.id = id;
        this.password = password;
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
}
