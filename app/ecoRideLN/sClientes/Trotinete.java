package app.ecoRideLN.sClientes;

public class Trotinete {

    private int id;
    private String modelo;
    private String marca;
    private String num_serie;
    private String tipo_motor;
    private int codCliente;

    public Trotinete(int id, String modelo, String marca, String num_serie, String tipo_motor, int codCliente) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.num_serie = num_serie;
        this.tipo_motor = tipo_motor;
        this.codCliente = codCliente;
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

    public String getNum_serie() {
        return num_serie;
    }

    public void setNum_serie(String num_serie) {
        this.num_serie = num_serie;
    }

    public String getTipo_motor() {
        return tipo_motor;
    }

    public void setTipo_motor(String tipo_motor) {
        this.tipo_motor = tipo_motor;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }
}
