package app.ecoRideLN.sOrdensServico;

public class Fotografia {
     private String caminho;

     public Fotografia(String caminho) {
          this.caminho = caminho;
     }

     public String getCaminho() {
          return caminho;
     }

     public void setCaminho(String caminho) {
          this.caminho = caminho;
     }

     public boolean isValid() {
          if (caminho == null || caminho.isBlank()) return false;
          String lower = caminho.toLowerCase();
          return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
     }
}
