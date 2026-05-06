package app.ecoRideLN.sClientes;

import java.util.ArrayList;
import java.util.List;

public class Trotinete {

     private int id;
     private String modelo;
     private String marca;
     private int num_serie;
     private String tipo_motor;
     private int cod_cliente;
     private List<Integer> codsOrdensServico;

     public Trotinete(int id, String modelo, String marca, int num_serie, String tipo_motor, int cod_cliente) {
          this.id = id;
          this.modelo = modelo;
          this.marca = marca;
          this.num_serie = num_serie;
          this.tipo_motor = tipo_motor;
          this.cod_cliente = cod_cliente;
          this.codsOrdensServico = new ArrayList<>();
     }

     public Trotinete(int id, String modelo, String marca, int num_serie, String tipo_motor, int cod_cliente, List<Integer> codsOrdensServico) {
          this.id = id;
          this.modelo = modelo;
          this.marca = marca;
          this.num_serie = num_serie;
          this.tipo_motor = tipo_motor;
          this.cod_cliente = cod_cliente;
          this.codsOrdensServico = new ArrayList<>(codsOrdensServico);
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getModelo() {
          return modelo;
     }

     public void setModelo(String modelo) {
          this.modelo = modelo;
     }

     public String getMarca() {
          return marca;
     }

     public void setMarca(String marca) {
          this.marca = marca;
     }

     public int getNum_serie() {
          return num_serie;
     }

     public void setNum_serie(int num_serie) {
          this.num_serie = num_serie;
     }

     public String getTipo_motor() {
          return tipo_motor;
     }

     public void setTipo_motor(String tipo_motor) {
          this.tipo_motor = tipo_motor;
     }

     public int getCod_cliente() {
          return cod_cliente;
     }

     public void setCod_cliente(int cod_cliente) {
          this.cod_cliente = cod_cliente;
     }

     public List<Integer> getCodsOrdensServico() {
          return new ArrayList<>(codsOrdensServico);
     }

     public void setCodsOrdensServico(List<Integer> codsOrdensServico) {
          this.codsOrdensServico = new ArrayList<>(codsOrdensServico);
     }
}
