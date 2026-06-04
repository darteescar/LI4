package app.ecoRideLN.sStock;

public class Fornecedor {
     private int id;
     private String nome;
     private String email;
     private String telemovel;

     public Fornecedor(int id, String nome, String telemovel, String email) {
          this.id = id;
          this.nome = nome;
          this.telemovel = telemovel;
          this.email = email;
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

}
