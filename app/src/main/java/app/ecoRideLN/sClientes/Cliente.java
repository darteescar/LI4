package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
     private int id;
     private String nome;
     private String email;
     private String telemovel;
     private String NIF;
     private List<Integer> codsTrotinetes;
     private TrotineteDAO trotineteDAO;


     public Cliente(int id, String nome, String email, String telemovel, String NIF, List<Integer> codsTrotinetes) {
          this.id = id;
          this.nome = nome;
          this.email = email;
          this.telemovel = telemovel;
          this.NIF = NIF;
          this.codsTrotinetes = new ArrayList<>(codsTrotinetes);
          this.trotineteDAO = TrotineteDAO.getInstance();
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
          return new ArrayList<>(codsTrotinetes);
     }

     public void setCodsTrotinetes(List<Integer> codsTrotinetes) {
          this.codsTrotinetes = new ArrayList<>(codsTrotinetes);
     }

     public List<Trotinete> getTrotinetes() {
          List<Trotinete> trotinetes = new ArrayList<>();
          for (Integer cod : codsTrotinetes) {
               Trotinete trotinete = trotineteDAO.getTrotineteById(cod);
               if (trotinete != null) {
                    trotinetes.add(trotinete);
               }
          }
          return trotinetes;
     }

     // Métodos adicionais para manipular a lista de códigos de trotinetes

     public boolean addTrotinete(int codTrotinete) {
          if (!codsTrotinetes.contains(codTrotinete)) {
               codsTrotinetes.add(codTrotinete);
               return true;
          }
          return false;
     }

     public boolean removeTrotinete(int codTrotinete) {
          return codsTrotinetes.remove(Integer.valueOf(codTrotinete));
     }
}
