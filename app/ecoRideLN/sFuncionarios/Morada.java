package app.ecoRideLN.sFuncionarios;

public class Morada {

    private String numero_porta;
    private String rua;
    private String localidade;
    private String codigo_postal;

    public Morada(String numero_porta, String rua, String localidade, String codigo_postal) {
        this.numero_porta = numero_porta;
        this.rua = rua;
        this.localidade = localidade;
        this.codigo_postal = codigo_postal;
    }

    public String getNumero_porta() {
        return numero_porta;
    }

    public void setNumero_porta(String numero_porta) {
        this.numero_porta = numero_porta;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }
}
