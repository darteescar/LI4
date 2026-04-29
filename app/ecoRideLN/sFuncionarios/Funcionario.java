package app.ecoRideLN.sFuncionarios;

public class Funcionario {

    private int id;
    private String nome;
    private String telemovel;
    private String email;
    private String data_nascimento;
    private String NISS;
    private String NIF;
    private String NUS;
    private String IBAN;
    private float salario_hora;
    private float salario_liquido;
    private float salario_bruto;
    private int horas_extra;
    private String numero_porta;
    private String rua;
    private String localidade;
    private String codigo_postal;

    public Funcionario(int id, String nome, String telemovel, String email,
                       String data_nascimento, String NISS, String NIF, String NUS, String IBAN,
                       float salario_hora, float salario_bruto, float salario_liquido,
                       String numero_porta, String rua, String localidade, String codigo_postal) {
        this.id = id;
        this.nome = nome;
        this.telemovel = telemovel;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.NISS = NISS;
        this.NIF = NIF;
        this.NUS = NUS;
        this.IBAN = IBAN;
        this.salario_hora = salario_hora;
        this.salario_bruto = salario_bruto;
        this.salario_liquido = salario_liquido;
        this.horas_extra = 0;
        this.numero_porta = numero_porta;
        this.rua = rua;
        this.localidade = localidade;
        this.codigo_postal = codigo_postal;
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

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getNISS() {
        return NISS;
    }

    public void setNISS(String NISS) {
        this.NISS = NISS;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getNUS() {
        return NUS;
    }

    public void setNUS(String NUS) {
        this.NUS = NUS;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public float getSalario_hora() {
        return salario_hora;
    }

    public void setSalario_hora(float salario_hora) {
        this.salario_hora = salario_hora;
    }

    public float getSalario_liquido() {
        return salario_liquido;
    }

    public void setSalario_liquido(float salario_liquido) {
        this.salario_liquido = salario_liquido;
    }

    public float getSalario_bruto() {
        return salario_bruto;
    }

    public void setSalario_bruto(float salario_bruto) {
        this.salario_bruto = salario_bruto;
    }

    public int getHoras_extra() {
        return horas_extra;
    }

    public void setHoras_extra(int horas_extra) {
        this.horas_extra = horas_extra;
    }

    public String getNumero_porta() {
        return numero_porta;
    }

    public void setNumero_porta(String numero_porta) {
        this.numero_porta = numero_porta;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }
}
