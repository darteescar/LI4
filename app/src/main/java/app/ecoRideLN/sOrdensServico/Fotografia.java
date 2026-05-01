package app.ecoRideLN.sOrdensServico;

public class Fotografia {
     private byte[] conteudo;
     private String formato;
     private long tamanho;

     public Fotografia(byte[] conteudo, String formato, long tamanho) {
          this.conteudo = conteudo;
          this.formato = formato;
          this.tamanho = tamanho;
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
