package app.ecoRideLN.sOrdens;

public class Checklist {

    private boolean travoes;
    private boolean luzes;
    private boolean pneus;
    private boolean aceleracao;
    private boolean travagem;
    private boolean visor;
    private boolean teste_pratico;

    public Checklist(boolean travoes, boolean luzes, boolean pneus, boolean aceleracao,
                     boolean travagem, boolean visor, boolean teste_pratico) {
        this.travoes = travoes;
        this.luzes = luzes;
        this.pneus = pneus;
        this.aceleracao = aceleracao;
        this.travagem = travagem;
        this.visor = visor;
        this.teste_pratico = teste_pratico;
    }

    public boolean isTravoes() { return travoes; }
    public void setTravoes(boolean travoes) { this.travoes = travoes; }

    public boolean isLuzes() { return luzes; }
    public void setLuzes(boolean luzes) { this.luzes = luzes; }

    public boolean isPneus() { return pneus; }
    public void setPneus(boolean pneus) { this.pneus = pneus; }

    public boolean isAceleracao() { return aceleracao; }
    public void setAceleracao(boolean aceleracao) { this.aceleracao = aceleracao; }

    public boolean isTravagem() { return travagem; }
    public void setTravagem(boolean travagem) { this.travagem = travagem; }

    public boolean isVisor() { return visor; }
    public void setVisor(boolean visor) { this.visor = visor; }

    public boolean isTeste_pratico() { return teste_pratico; }
    public void setTeste_pratico(boolean teste_pratico) { this.teste_pratico = teste_pratico; }

    public boolean tudoValidado() {
        return travoes && luzes && pneus && aceleracao && travagem && visor && teste_pratico;
    }
}
