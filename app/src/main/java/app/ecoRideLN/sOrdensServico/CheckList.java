package app.ecoRideLN.sOrdensServico;

public class CheckList {
     private boolean luzes = false;
     private boolean pneus = false;
     private boolean aceleracao = false;
     private boolean travagem = false;
     private boolean visor = false;
     private boolean teste_pratico = false;

     public CheckList() {
     }

     public CheckList(CheckList checkList) {
          this.luzes = checkList.luzes;
          this.pneus = checkList.pneus;
          this.aceleracao = checkList.aceleracao;
          this.travagem = checkList.travagem;
          this.visor = checkList.visor;
          this.teste_pratico = checkList.teste_pratico;
     }

     public boolean getLuzes() {
          return luzes;
     }

     public void setLuzes(boolean luzes) {
          this.luzes = luzes;
     }

     public boolean getPneus() {
          return pneus;
     }

     public void setPneus(boolean pneus) {
          this.pneus = pneus;
     }

     public boolean getAceleracao() {
          return aceleracao;
     }

     public void setAceleracao(boolean aceleracao) {
          this.aceleracao = aceleracao;
     }

     public boolean getTravagem() {
          return travagem;
     }

     public void setTravagem(boolean travagem) {
          this.travagem = travagem;
     }

     public boolean getVisor() {
          return visor;
     }

     public void setVisor(boolean visor) {
          this.visor = visor;
     }

     public boolean getTeste_pratico() {
          return teste_pratico;
     }

     public void setTeste_pratico(boolean teste_pratico) {
          this.teste_pratico = teste_pratico;
     }

     // Métodos a mais

     public boolean isCheckListComplete() {
          return luzes && pneus && aceleracao && travagem && visor && teste_pratico;
     }
}
