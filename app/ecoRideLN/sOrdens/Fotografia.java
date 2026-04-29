package app.ecoRideLN.sOrdens;

public class Fotografia {

    private int id;
    private byte[] conteudo;
    private String formato;
    private long tamanho;

    public Fotografia(int id, byte[] conteudo, String formato, long tamanho) {
        this.id = id;
        this.conteudo = conteudo;
        this.formato = formato;
        this.tamanho = tamanho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }
}
