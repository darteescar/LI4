package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telemovel;
    private String NIF;
    private final List<Integer> codsTrotinetes;

    public Cliente(int id, String nome, String email, String telemovel, String NIF) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telemovel = telemovel;
        this.NIF = NIF;
        this.codsTrotinetes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public List<Integer> getCodsTrotinetes() {
        return codsTrotinetes;
    }
}
