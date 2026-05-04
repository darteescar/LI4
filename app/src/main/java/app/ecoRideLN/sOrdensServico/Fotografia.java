package app.ecoRideLN.sOrdensServico;

public class Fotografia {
     private byte[] conteudo;
     private String formato;
     private long tamanho;

     public Fotografia(byte[] conteudo, String formato, long tamanho) {
          this.conteudo = conteudo == null ? null : conteudo.clone();
          this.formato = formato;
          this.tamanho = tamanho;
     }

     public byte[] getConteudo() {
          return conteudo == null ? null : conteudo.clone();
     }

     public void setConteudo(byte[] conteudo) {
          this.conteudo = conteudo == null ? null : conteudo.clone();
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

     public boolean isValid() {
          // Simple validation: check if the format is supported and size is within limits
          String[] formatosSuportados = {"jpg", "jpeg", "png"};
          boolean formatoValido = false;
          for (String f : formatosSuportados) {
               if (formato.equalsIgnoreCase(f)) {
                    formatoValido = true;
                    break;
               }
          }
          return formatoValido && tamanho > 0 && tamanho < 5 * 1024 * 1024; // Max 5MB
     }

}
